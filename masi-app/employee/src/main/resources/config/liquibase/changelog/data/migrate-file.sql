ALTER table documentary
    drop column if exists attachments_content_type;
ALTER table documentary
    drop column if exists attachments_content_name;
ALTER table documentary
    drop column if exists approval_sign;
ALTER table documentary
    drop column if exists approval_sign_content_type;
ALTER table documentary
    add column if not exists attachments_content_file varchar(100);
ALTER table documentary
    add column if not exists approval_sign_file varchar(100);


ALTER TABLE leave_regime_request
    DROP COLUMN IF EXISTS attachment;
ALTER TABLE leave_regime_request
    DROP COLUMN IF EXISTS attachment_content_type;
ALTER TABLE leave_regime_request
    DROP COLUMN IF EXISTS attachment_name;
ALTER TABLE process_leave_regime_request
    add column if not exists file_id varchar(100);
ALTER TABLE process_leave_regime_request
    add column if not exists file_name varchar(250);

ALTER TABLE recruitment_review_request
    DROP COLUMN IF EXISTS approval_sign;
ALTER TABLE recruitment_review_request
    DROP COLUMN IF EXISTS approval_sign_content_type;
ALTER TABLE recruitment_review_request
    ADD COLUMN IF NOT EXISTS approval_sign_file VARCHAR(100);
ALTER TABLE leave_regime_request
    add column if not exists file_id varchar(100);
ALTER TABLE leave_regime_request
    add column if not exists file_name varchar(250);
ALTER TABLE uniform_release
    add column if not exists file_name varchar(250);
ALTER TABLE uniform_order_process
    add column if not exists file_name varchar(250);


ALTER table leave_request_review
    add column if not exists file_id varchar(100);
ALTER table leave_request
    add column if not exists file_id varchar(100);
ALTER table leave_request_review
    add column if not exists file_name varchar(255);
ALTER table uniform_order_process
    add column if not exists file_name varchar(255);



alter table monthly_time_sheet_review
    drop column if exists signature;
alter table monthly_time_sheet_review
    drop column if exists signature_content_type;
alter table monthly_time_sheet_review
    add column if not exists signature_file varchar(100);



alter table employee_profile
    add column if not exists probation_date_from date;
alter table employee_profile
    add column if not exists probation_date_to date;
alter table employee_profile
    add column if not exists official_work_type varchar(50);
alter table employee_profile
    add column if not exists official_work_type_duration float;
alter table employee_profile
    add column if not exists insurance_payment_level float;


alter table interview_schedule
    drop column if exists cv_file;
alter table interview_schedule
    drop column if exists cv_file_content_type;
alter table interview_schedule
    drop column if exists cv_file_name;

alter table interview_schedule
    add column if not exists cv_file varchar(100);

alter table uniform_release
    add column if not exists remaining int not null default 0;

