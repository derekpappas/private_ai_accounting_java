CREATE TYPE corporation_type AS ENUM ('LLC', 'C_CORP', 'S_CORP');
CREATE TYPE card_type AS ENUM ('VISA', 'MASTERCARD', 'AMEX', 'DISCOVER', 'OTHER');
CREATE TYPE account_type AS ENUM ('CHECKING', 'SAVINGS', 'BUSINESS', 'CREDIT_CARD');

CREATE TABLE contact_persons (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    corporation_type corporation_type NOT NULL,
    contact_person_id BIGINT REFERENCES contact_persons(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bank_info (
    id BIGSERIAL PRIMARY KEY,
    bank_name VARCHAR(255) NOT NULL,
    account_type account_type NOT NULL,
    last_4_digits CHARACTER VARYING(4) NOT NULL,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(company_id, last_4_digits)
);

CREATE TABLE credit_cards (
    id BIGSERIAL PRIMARY KEY,
    card_type card_type NOT NULL,
    last_4_digits VARCHAR(4) NOT NULL,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(company_id, last_4_digits)
);

CREATE TABLE files (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    file_path VARCHAR(1024) NOT NULL,
    bank_info_id BIGINT REFERENCES bank_info(id),
    credit_card_id BIGINT REFERENCES credit_cards(id),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (
        (bank_info_id IS NOT NULL AND credit_card_id IS NULL) OR
        (bank_info_id IS NULL AND credit_card_id IS NOT NULL)
    )
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    description TEXT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    additional_data TEXT,
    file_id BIGINT NOT NULL REFERENCES files(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_contact_persons_email ON contact_persons(email);
CREATE INDEX idx_companies_name ON companies(name);
CREATE INDEX idx_bank_info_company_id ON bank_info(company_id);
CREATE INDEX idx_credit_cards_company_id ON credit_cards(company_id);
CREATE INDEX idx_files_bank_info_id ON files(bank_info_id);
CREATE INDEX idx_files_credit_card_id ON files(credit_card_id);
CREATE INDEX idx_transactions_file_id ON transactions(file_id);
CREATE INDEX idx_transactions_date ON transactions(date); 