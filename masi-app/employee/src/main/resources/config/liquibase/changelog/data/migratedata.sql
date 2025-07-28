DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT
            FROM   pg_catalog.pg_database
            WHERE  datname = 'MasiUtility'
        ) THEN
            PERFORM dblink_exec('dbname=' || current_database() || ' user=' || current_user, 'CREATE DATABASE
"MasiUtility"');
        END IF;
    END
$$;

DO $$
BEGIN
        IF NOT EXISTS (
            SELECT
            FROM   pg_catalog.pg_database
            WHERE  datname = 'MasiLogistic'
        ) THEN
            PERFORM dblink_exec('dbname=' || current_database() || ' user=' || current_user, 'CREATE DATABASE "MasiLogistic"');
END IF;
END
$$;


ALTER TABLE employee_profile ADD COLUMN IF NOT EXISTS company varchar(255);
ALTER TABLE employee_profile ADD COLUMN IF NOT EXISTS department varchar(255);
ALTER TABLE recruitment_request ADD COLUMN IF NOT EXISTS company varchar(255);
ALTER TABLE workspace ADD COLUMN IF NOT EXISTS workspace_type varchar(100);
ALTER TABLE workspace ADD COLUMN IF NOT EXISTS company varchar(255);
ALTER TABLE time_keeping ADD COLUMN IF NOT EXISTS type varchar(100) DEFAULT 'HOUR';
ALTER TABLE personal_monthly_timesheet ADD COLUMN IF NOT EXISTS type varchar(100) DEFAULT 'HOUR';
ALTER TABLE time_keeping DROP CONSTRAINT IF EXISTS ux_time_keeping_date_employee_id;
ALTER TABLE time_keeping DROP CONSTRAINT IF EXISTS ux_time_keeping_date_employee_id_type;
ALTER TABLE time_keeping ADD CONSTRAINT ux_time_keeping_date_employee_id_type UNIQUE ("date", employee_id, "type");
ALTER  TABLE interview_schedule ADD COLUMN IF NOT EXISTS email varchar(255);
ALTER  TABLE interview_schedule ADD COLUMN IF NOT EXISTS phone_number varchar(255);
ALTER TABLE interview_schedule ADD COLUMN IF NOT EXISTS cv_file_name varchar(255);
-- UPDATE employee_profile SET company = 'KIM_LONG' ;
-- UPDATE  recruitment_request SET company = 'KIM_LONG' ;
-- UPDATE workspace SET company = 'KIM_LONG' ;
ALTER TABLE recruitment_request ADD COLUMN IF NOT EXISTS salary_unit varchar(50);
ALTER TABLE recruitment_request ADD COLUMN IF NOT EXISTS replace_for_id uuid;
ALTER TABLE recruitment_request RENAME COLUMN request_description TO department_id;
ALTER TABLE recruitment_request ALTER COLUMN department_id SET DATA TYPE uuid USING department_id::uuid;

