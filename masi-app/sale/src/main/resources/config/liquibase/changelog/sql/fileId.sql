alter table quotation
    drop column if exists approval_sign;
alter table quotation
    drop column if exists approval_sign_content_type;
alter table quotation
    add column if not exists approval_sign_file varchar(100);

-- created_by,updated_by,company
alter table purchase_request
    add column if not exists created_by varchar(100);
alter table purchase_request
    add column if not exists updated_by varchar(100);
alter table purchase_request
    add column if not exists company varchar(100);


alter table purchase_review
    drop column if exists approval_status_sign;
alter table purchase_review
    drop column if exists approval_status_sign_content_type;

alter table purchase_review
    add column if not exists approval_status_sign_file varchar(100);



alter table order_review
    drop column if exists approval_status_sign;
alter table order_review
    drop column if exists approval_status_sign_content_type;

alter table order_review
    add column if not exists approval_sign_file varchar(100);

alter table contract
    drop column if exists approval_sign;
alter table contract
    drop column if exists approval_sign_content_type;
alter table contract
    add column if not exists approval_sign_file varchar(100);


alter table masi_order
    add column if not exists created_by varchar(100);
alter table masi_order
    add column if not exists updated_by varchar(100);
alter table masi_order
    add column if not exists company varchar(100);


