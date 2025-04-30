ALTER TABLE transactions
ADD COLUMN transaction_type VARCHAR(20);

-- Set default value for existing records
UPDATE transactions SET transaction_type = 'OTHER' WHERE transaction_type IS NULL; 