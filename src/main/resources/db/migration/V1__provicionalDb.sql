-- Table for Users
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for Accounts
CREATE TABLE Accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- e.g., 'savings', 'checking', 'credit', 'investment', 'cash', 'other'
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    isDefault BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Table for Goals
CREATE TABLE Goals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    target_amount DECIMAL(10, 2) NOT NULL,
    deadline DATE,
    description TEXT,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    saved_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);

-- Table for Transactions
CREATE TABLE Transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'income', 'expense', 'transfer'
    amount DECIMAL(10, 2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    category VARCHAR(255),
    goal_id INT,
    transfer_account_id INT, -- To link transfer transactions to the other account involved
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (account_id) REFERENCES Accounts(id),
    FOREIGN KEY (goal_id) REFERENCES Goals(id),
    FOREIGN KEY (transfer_account_id) REFERENCES Accounts(id)
);

-- Table for Assets
CREATE TABLE Assets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    estimated_value DECIMAL(10, 2) NOT NULL,
    acquisition_date DATE,
    interest_rate DECIMAL(5, 2),
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Table for Liabilities
CREATE TABLE Liabilities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    pending_balance DECIMAL(10, 2) NOT NULL,
    interest_rate DECIMAL(5, 2),
    start_date DATE,
    end_date DATE,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Table for Goal Allocations
CREATE TABLE Goal_Allocations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    goal_id INT NOT NULL,
    source_account_id INT NOT NULL,
    allocated_amount DECIMAL(10, 2) NOT NULL,
    allocation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (goal_id) REFERENCES Goals(id),
    FOREIGN KEY (source_account_id) REFERENCES Accounts(id)
);

-- Table for Roles
CREATE TABLE Roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- e.g., 'ROLE_USER', 'ROLE_ADMIN'
);

-- Table for Users_Roles (Join Table)
CREATE TABLE Users_Roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id), -- Composite primary key
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (role_id) REFERENCES Roles(id)
);