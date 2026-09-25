alter table users add column authco_user_id varchar(36) UNIQUE null;
ALTER TABLE users DROP COLUMN password;

DROP TABLE users_roles;
DROP TABLE roles;