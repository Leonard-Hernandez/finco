-- Update accounts table
ALTER TABLE accounts 
MODIFY COLUMN balance DECIMAL(19,2) NOT NULL DEFAULT 0.00;

-- Update goals table
ALTER TABLE goals 
MODIFY COLUMN target_amount DECIMAL(19,2) NOT NULL,
MODIFY COLUMN saved_amount DECIMAL(19,2) NOT NULL DEFAULT 0.00;

-- Update transactions table
ALTER TABLE transactions 
MODIFY COLUMN amount DECIMAL(19,2) NOT NULL;
