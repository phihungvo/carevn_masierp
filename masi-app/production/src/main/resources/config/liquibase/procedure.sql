
CREATE OR REPLACE PROCEDURE update_timekeeping_timesheets()
LANGUAGE plpgsql
AS $$
DECLARE
    rec RECORD;
    existing_timesheet_id UUID;
BEGIN
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
    FOR rec IN SELECT * FROM time_keeping WHERE time_keeping.personal_monthly_timesheet_id is NULL LOOP
        -- Find existing personal_monthly_timesheet
        SELECT id INTO existing_timesheet_id
        FROM personal_monthly_timesheet
        WHERE employee_id = rec.employee_id
                AND EXTRACT(YEAR FROM month) = EXTRACT(YEAR FROM rec.date)
                AND EXTRACT(MONTH FROM month) = EXTRACT(MONTH FROM rec.date)
              
        LIMIT 1;

        -- If no existing timesheet, create a new one
        IF existing_timesheet_id IS NULL THEN
            INSERT INTO personal_monthly_timesheet (id, month, status, created_date, last_updated, employee_id)
            VALUES (
                uuid_generate_v4(), -- Generate a new UUID
                DATE_TRUNC('month', rec.date) + INTERVAL '0 day', -- First day of the month
                'PENDING',
                NOW(),
                NOW(),
                rec.employee_id
            )
            RETURNING id INTO existing_timesheet_id;
        END IF;

        -- Update the time_keeping record with the personal_monthly_timesheet_id
        UPDATE time_keeping
        SET personal_monthly_timesheet_id = existing_timesheet_id
        WHERE id = rec.id;
    END LOOP;
END;
$$;

-- Call the procedure
CALL update_timekeeping_timesheets();
