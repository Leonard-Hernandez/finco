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