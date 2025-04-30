CREATE OR REPLACE FUNCTION cast_to_corporation_type(text) RETURNS corporation_type AS $$
BEGIN
    RETURN $1::corporation_type;
EXCEPTION WHEN others THEN
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

ALTER TABLE companies 
ALTER COLUMN corporation_type TYPE corporation_type 
USING cast_to_corporation_type(corporation_type::text); 