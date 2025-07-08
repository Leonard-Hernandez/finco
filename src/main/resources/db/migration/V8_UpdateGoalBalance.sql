ALTER TABLE goals
DROP COLUMN saved_amount;

CREATE TABLE goal_account_balance (
    id BIGSERIAL PRIMARY KEY,
    goal_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uq_goal_account UNIQUE (goal_id, account_id),
    
    CONSTRAINT fk_goal
        FOREIGN KEY (goal_id) 
        REFERENCES goal(id)
        ON DELETE CASCADE,
        
    CONSTRAINT fk_account
        FOREIGN KEY (account_id) 
        REFERENCES account(id)
        ON DELETE CASCADE
);