CREATE OR REPLACE FUNCTION check_grade_value()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.grade < 0 OR NEW.grade > 100 THEN
        RAISE EXCEPTION 'Grade must be between 0 and 100';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_grade
    BEFORE INSERT OR UPDATE ON grades
    FOR EACH ROW
EXECUTE FUNCTION check_grade_value();

CREATE OR REPLACE FUNCTION set_processed_at()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status IN ('APPROVED', 'REJECTED') THEN
        NEW.processed_at = CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_processed_at
    BEFORE UPDATE ON requests
    FOR EACH ROW
EXECUTE FUNCTION set_processed_at();