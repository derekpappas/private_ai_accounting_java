ALTER TABLE credit_cards DROP COLUMN IF EXISTS cvv;
ALTER TABLE credit_cards DROP COLUMN IF EXISTS expiration_date;
ALTER TABLE credit_cards DROP COLUMN IF EXISTS card_number;
ALTER TABLE credit_cards DROP COLUMN IF EXISTS cardholder_name; 