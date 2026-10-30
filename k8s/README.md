# finco on minikube

`kubectl apply -k finco/k8s` deploys, in namespace `finco`:

| App | Image | Port | Public path |
|---|---|---|---|
| finco (Spring + MySQL 8) | `leonard20/finco:latest` | 8086 | `http://localhost/finco-api/v1` |
| authco (Spring Authorization Server + Postgres 17) | `leonard20/authco:latest` | 8087 | `http://localhost/authco` |
| finco-site (Angular SPA on nginx) | `leonard20/finco-site:latest` | 80 | `http://localhost/` |

One nginx Ingress (`localhost`) routes by path prefix, so everything is same-origin.
No path rewrites: each app already serves under its own context path.

## 1. Start minikube and the ingress controller

```sh
minikube start
minikube addons enable ingress
```

## 2. Build the images inside minikube

Deployments use `imagePullPolicy: IfNotPresent`, so images must exist in minikube's runtime.

```sh
minikube image build -t leonard20/finco:latest      -f dockerfile finco
minikube image build -t leonard20/authco:latest     -f Dockerfile authco
minikube image build -t leonard20/finco-site:latest -f Dockerfile finco-site
```

Alternative: `eval $(minikube docker-env)` (fish: `minikube docker-env | source`) and run `docker build` as usual.

## 3. Expose the ingress on 127.0.0.1

```sh
sudo kubectl --kubeconfig ~/.kube/config -n ingress-nginx \
  port-forward --address 127.0.0.1,::1 svc/ingress-nginx-controller 80:80
```

Keep it running in its own terminal (port 80 needs root). `minikube tunnel` does NOT work here:
the ingress addon service is a NodePort, and the tunnel only exposes LoadBalancer services. No `/etc/hosts` entry is needed: browsers always resolve
`*.localhost` to loopback and treat it as a secure context over plain http, which the SPA
needs for WebCrypto (PKCE). For curl: `curl --resolve localhost:80:127.0.0.1 ...`.

## 4. Set the secrets

`config/secret.yml` ships base64 **placeholders**. Replace them before the first deploy
(`echo -n 'value' | base64 -w0`):

| Secret | Key | Value |
|---|---|---|
| `finco-secret` | `openai-key` | OpenAI API key |
| `finco-secret`, `mysql-secret` | `password` | MySQL root password (keep both equal) |
| `postgres-secret` | `username`, `password` | Postgres credentials used by authco |
| `authco-secret` | `google_client_id`, `google_client_secret` | Google OAuth client |
| `authco-secret` | `master_key` | `openssl rand -base64 32` (base64 of 32 bytes) |

Changing a secret after deploy: `kubectl -n finco rollout restart deploy/<name>`.
Database passwords only apply on the first init of the volume.

Google OAuth: the client's authorized redirect URIs must include
`http://localhost/authco/login/oauth2/code/google`.

## 5. Deploy

```sh
kubectl apply -k finco/k8s
kubectl -n finco get pods -w
```

The authco admin API password is generated on first startup and printed once:

```sh
kubectl -n finco logs deploy/authco | rg -A3 'admin API credentials'
```

## finco-site login (OAuth 2.1 Authorization Code + PKCE)

finco-site is a public client of authco. authco seeds it on every startup from the
`authco` ConfigMap (`AUTHCO_SPA_CLIENT_*`): PKCE (S256) required, no consent screen,
15 min access tokens, no refresh tokens (an expired session re-runs the authorization
request, silently while the authco session is alive). The authco session lasts
`SERVER_SERVLET_SESSION_TIMEOUT` (`session_timeout: 8h` in the `authco` ConfigMap).

1. `/auth/login` → `http://localhost/authco/oauth2/authorize` with
   `resource=http://localhost/finco-api/v1` (must be in `authco_resources`).
2. Google login at authco → back to `http://localhost/auth/callback` (nginx SPA fallback).
3. The SPA POSTs the code to `/authco/oauth2/token`, keeps the access token in
   sessionStorage and loads the user from `GET /finco-api/v1/users/me`
   (finco JIT-provisions it). Roles come from the token `scope` claim (`finco:premium`, `finco:admin`).
4. Log out → `/authco/connect/logout?id_token_hint=...&post_logout_redirect_uri=http://localhost/`.

The redirect URIs use plain http only because `localhost` is a loopback host;
any public host must use https.

finco-site reads optional runtime env vars `FINCO_API_URL`, `AUTHCO_URL`, `OAUTH_CLIENT_ID`
and `FINCO_RESOURCE`. They stay unset here, so the SPA uses same-origin defaults
(`/finco-api/v1`, `/authco`, `finco-site`, `<origin>/finco-api/v1`); see the commented example
in `deploys/deployment-finco-site.yaml`.

### Local development (ng serve + finco + authco on the host)

| Where | Variable | Value |
|---|---|---|
| authco | `AUTHCO_SPA_CLIENT_ID` | `finco-site` |
| authco | `AUTHCO_SPA_CLIENT_REDIRECT_URIS` | `http://localhost:4200/auth/callback` |
| authco | `AUTHCO_SPA_CLIENT_POST_LOGOUT_REDIRECT_URIS` | `http://localhost:4200/` |
| authco | `AUTHCO_RESOURCES` (first entry = default audience) | `http://localhost:8086/finco-api/v1` |
| authco | `CORS_ALLOWED_ORIGINS` | `http://localhost:4200` (the code exchange is a cross-origin POST locally) |
| finco | `AUTHCO_ISSUER` | `http://localhost:8087/authco` |
| finco | `FINCO_RESOURCE_ID` | `http://localhost:8086/finco-api/v1` |
| finco | `FRONTEND_URL` | `http://localhost:4200` (finco CORS) |

authco's `docker-compose.yml` already defaults to these values.

## Notes

- Data lives in hostPath PVs inside the minikube node: `/var/lib/mysql`, `/var/lib/postgresql-authco`.
- Render without applying: `kubectl kustomize finco/k8s`.
