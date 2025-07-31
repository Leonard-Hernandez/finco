-- Table for Users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enable BOOLEAN DEFAULT 1,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for Roles
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- e.g., 'ROLE_USER', 'ROLE_ADMIN'
);

-- Table for Users_Roles (Join Table)
CREATE TABLE users_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id), -- Composite primary key
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Table for Accounts
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- e.g., 'savings', 'checking', 'credit', 'investment', 'cash', 'other'
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'COP',
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    is_Default BOOLEAN,
    enable BOOLEAN DEFAULT 1,
    version INT DEFAULT 0,
    deposit_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    withdraw_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Table for Goals
CREATE TABLE goals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    target_amount DECIMAL(19, 2) NOT NULL,
    deadline DATE,
    description TEXT,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enable BOOLEAN DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE goal_account_balance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    goal_id INT NOT NULL,
    account_id INT NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (goal_id) REFERENCES goals(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Table for Transactions
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'income', 'expense', 'transfer'
    amount DECIMAL(19, 2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    category VARCHAR(255),
    goal_id INT,
    transfer_account_id INT, -- To link transfer transactions to the other account involved
    fee DECIMAL(10, 2) DEFAULT 0.00,
    exchange_rate DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (goal_id) REFERENCES goals(id),
    FOREIGN KEY (transfer_account_id) REFERENCES accounts(id)
);

-- Table for Assets
CREATE TABLE assets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    estimated_value DECIMAL(10, 2) NOT NULL,
    acquisition_date DATE,
    interest_rate DECIMAL(5, 2),
    description TEXT,
    enable BOOLEAN DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Table for Liabilities
CREATE TABLE liabilities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    pending_balance DECIMAL(10, 2) NOT NULL,
    interest_rate DECIMAL(5, 2),
    start_date DATE,
    end_date DATE,
    description TEXT,
    enable BOOLEAN DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO Finco.roles (name) VALUES('ROLE_USER');
INSERT INTO Finco.roles (name) VALUES('ROLE_ADMIN');

-- Default Admin User
INSERT INTO Finco.users (name, email, password) VALUES('admin', 'admin@admin.com', '$2a$10$5cGN.GA9uKOLHouhpbdLq.DthmfFRNkXPib8n1vyv6gfKHe7mmTze');

INSERT INTO Finco.users_roles (user_id, role_id) VALUES(1, 1);
INSERT INTO Finco.users_roles (user_id, role_id) VALUES(1, 2);
