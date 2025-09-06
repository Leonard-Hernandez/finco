-- Update exchange_rate column to have more decimal places
ALTER TABLE transactions 
MODIFY COLUMN exchange_rate DECIMAL(10, 6) NULL;