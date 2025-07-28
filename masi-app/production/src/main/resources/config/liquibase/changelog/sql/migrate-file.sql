alter table sample_disposal
    drop column if exists reviewer_sign;
alter table sample_disposal
    drop column if exists reviewer_sign_content_type;
alter table sample_disposal
    add column if not exists reviewer_sign_file varchar(100);
