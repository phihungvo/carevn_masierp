alter table "MasiEmployee".public.time_keeping
    drop constraint fk_time_keeping__employee_id;
alter table "MasiEmployee".public.time_keeping
    add constraint fk_time_keeping__employee_id foreign key (employee_id) references "MasiEmployee".public.employee (id) on delete cascade;
alter table "MasiEmployee".public.leave_request
    drop constraint fk_leave_request__substitute_id;
alter table "MasiEmployee".public.leave_request
    add constraint fk_leave_request__substitute_id foreign key (substitute_id) references "MasiEmployee".public.employee (id) on delete cascade;




alter table "MasiEmployee".public.time_keeping_violation
    drop constraint fk_time_keeping_violation__employee_id;
alter table "MasiEmployee".public.time_keeping_violation
    add constraint fk_time_keeping_violation__employee_id foreign key (employee_id) references "MasiEmployee".public.employee (id) on delete cascade;


alter table "MasiEmployee".public.leave_request_review
    drop constraint fk_leave_request_review__reviewer_id;
alter table "MasiEmployee".public.leave_request_review
    add constraint fk_leave_request_review__reviewer_id foreign key (reviewer_id) references "MasiEmployee".public.employee (id) on delete cascade;

-- violates foreign key constraint "fk_personal_monthly_timesheet__employee_id" on table "personal_monthly_timesheet"
--                                                                                 DETAIL:  Key (id)=(f68c676b-7720-41e1-ab17-46fd001e6ca7) is still referenced from table "personal_monthly_timesheet".

alter table "MasiEmployee".public.personal_monthly_timesheet
    drop constraint fk_personal_monthly_timesheet__employee_id;
alter table "MasiEmployee".public.personal_monthly_timesheet
    add constraint fk_personal_monthly_timesheet__employee_id foreign key (employee_id) references "MasiEmployee".public.employee (id) on delete cascade;

-- ERROR:  update or delete on table "employee" violates foreign key constraint "fk_time_keeping_record__employee_id" on table "time_keeping_record"
--     DETAIL:  Key (id)=(f9def996-2c8d-48b6-bdb5-930969766c48) is still referenced from table "time_keeping_record".
alter table "MasiEmployee".public.time_keeping_record
    drop constraint fk_time_keeping_record__employee_id;
alter table "MasiEmployee".public.time_keeping_record
    add constraint fk_time_keeping_record__employee_id foreign key (employee_id) references "MasiEmployee".public.employee (id) on delete cascade;

-- violates foreign key constraint "fk_leave_request__employee_id" on table "leave_request"
--                                                                    DETAIL:  Key (id)=(f9def996-2c8d-48b6-bdb5-930969766c48) is still referenced from table "leave_request".

alter table "MasiEmployee".public.leave_request
    drop constraint fk_leave_request__employee_id;
alter table "MasiEmployee".public.leave_request
    add constraint fk_leave_request__employee_id foreign key (employee_id) references "MasiEmployee".public.employee (id) on delete cascade;

-- ERROR:  update or delete on table "leave_request" violates foreign key constraint "fk_leave_request_review__leave_request_id" on table "leave_request_review"
--     DETAIL:  Key (id)=(a9b44fa3-593a-4354-86bb-cd8bc2dce2b5) is still referenced from table "leave_request_review".

alter table "MasiEmployee".public.leave_request_review
    drop constraint fk_leave_request_review__leave_request_id;
alter table "MasiEmployee".public.leave_request_review
    add constraint fk_leave_request_review__leave_request_id foreign key (leave_request_id) references "MasiEmployee".public.leave_request (id) on delete cascade;
-- ERROR:  null value in column "start_work_date" of relation "employee_profile" violates not-null constraint
alter table "MasiEmployee".public.employee_profile
    alter column start_work_date drop not null;
alter table "MasiEmployee".public.employee_profile
    alter column start_work_date set default now();
alter table "MasiEmployee".public.employee_profile
    alter column contract_number drop not null;
alter table "MasiEmployee".public.employee_profile
    alter column contract_number set default 'N/A';
delete
from "MasiEmployee".public.employee
where id in (select id from "MasiEmployee".public.employee_profile where company = 'MMS');
update "MasiEmployee".public.employee
set is_active = false
where workspace_id in (select id from "MasiEmployee".public.workspace where company = 'MMS');
delete
from "MasiEmployee".public.employee_profile
where company = 'MMS';
INSERT INTO workspace (id, name, workspace_type, description, is_active, created_at, company, can_delete,
                       normalized_name)
VALUES ('b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 'Văn phòng_MMS', 'OFFICE', '', True, '2024-09-17T16:18:37.151320',
        'MMS', False, 'VAN_PHONGMMS')
on conflict do nothing;

INSERT INTO employee_profile (id, employee_code, full_name, gender, workspace_id, citizen_id, citizen_issue_date,
                              citizen_issue_place, residence_address, temporary_address, birthday, phone, tax_code,
                              start_work_date, role, position, bank_code, bank_number, contract_type, contract_term,
                              contract_number, contract_date, contract_end_date, level, parking_card, insurance_card,
                              referrer_id, referrer_date, email, note, status, created_at, updated_at, is_deleted,
                              is_active, insurance_payment_level, official_work_type_duration, probation_date_from,
                              probation_date_to,company)
VALUES ('d6cd8ee1-1dec-4db8-9f2c-7a68ef9a338c'::uuid, 'M000002', 'Nguyễn Bảo Nhựt Minh', 'MALE',
        'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 75083015073, '2022-09-21T00:00:00', 'CTCCSQLHCVTTXH',
        'Tổ 4, Khu Phố 5A, Trảng Dài, Thành Phố Biên Hòa, Đồng Nai',
        'Tổ 4, Khu Phố 5A, Trảng Dài, Thành Phố Biên Hòa, Đồng Nai', '1983-07-22T00:00:00', 362966611, NULL, now(),
        'NVVP', 'DIRECTOR', NULL, NULL, 'FIXED_TERM', NULL, NULL, now(), NULL, 5, NULL, NULL, NULL, NULL,
        'minh.nguyen@masi.vn', NULL, 'WORKING', '2024-09-17T16:20:06.295472', '2024-09-17T16:20:06.295472', False, True,
        10000000.0, NULL, '2024-02-19T00:00:00', '2024-03-19T00:00:00','MMS'),
       ('7569db99-a59b-4394-aabb-945ac5c877e6'::uuid, 'M000003', 'Trần Trung Huỳnh', 'MALE',
        'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 75087002491, '2021-04-19T00:00:00', 'CTCCSQLHCVTTXH',
        '738/100/24/64 KP8, Tam Hiệp, TP Biên Hòa, Đồng Nai', '738/100/24/64 KP8, Tam Hiệp, TP Biên Hòa, Đồng Nai',
        '1987-06-20T00:00:00', 916682439, 8546580740.0, now(), 'NVVP', 'TEAM_LEADER', 'Sacombank', 50041240447.0,
        'FIXED_TERM', NULL, NULL, now(), NULL, 3, NULL, NULL, NULL, NULL, 'huynh.tran@masi.vn', NULL, 'WORKING',
        '2024-09-17T16:20:06.301487', '2024-09-17T16:20:06.301487', False, True, NULL, NULL, NULL, NULL,'MMS'),
       ('4977b7e2-d83f-4f10-9734-a207de592708'::uuid, 'M000004', 'Nguyễn Hữu Đức', 'MALE',
        'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 79095008973, '2021-11-22T00:00:00', 'TP HCM',
        '030 Lô U, Cư Xá Thanh Đa, P.27, Bình Thạnh, TP HCM', NULL, '1995-12-17T00:00:00', 902811712, NULL, now(),
        'NVVP', 'TEAM_LEADER', NULL, NULL, 'FIXED_TERM', NULL, NULL, now(), NULL, 3, NULL, NULL, NULL, NULL,
        'duc.nguyen@masi.vn', NULL, 'WORKING', '2024-09-17T16:20:06.305512', '2024-09-17T16:20:06.305512', False, True,
        NULL, NULL, NULL, NULL,'MMS'),
       ('1ea21b81-5130-44b3-8baa-cd243ebba8bb'::uuid, 'F000001', 'Nguyễn Thị Thiên Lý', 'FEMALE',
        'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 66199016799, '2021-07-08T00:00:00', 'Đak Lắk',
        'Tổ Dân phố 3  Thị Trấn Krong Năng, Đăk Lăk', '87/57/22 Lê Văn Duyệt, phường 3, quận Bình Thạnh, Tp.HCM',
        '1999-06-19T00:00:00', 336444203, 8586205827.0, now(), 'NVVP', 'EMPLOYEE', 'BIDV', 5601135280.0, 'FIXED_TERM',
        NULL, NULL, now(), NULL, 2, NULL, NULL, NULL, NULL, 'ly.nguyen@masi.vn', NULL, 'WORKING',
        '2024-09-17T16:20:06.310316', '2024-09-17T16:20:06.310316', False, True, NULL, NULL, NULL, NULL,'MMS'),
       ('ae6d06a1-8e11-4e2a-aab6-1da0ee8c61fe'::uuid, 'M000005', 'Đoàn Trần Cao Danh', 'MALE',
        'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 77200007877, '2021-06-28T00:00:00', 'CTCCSQLHCVTTXH',
        '28/107 Tô 5, KP3, Tam Hòa, TP Biên Hòa, Đồng Nai', '28/107 Tổ 5, KP3, Tam Hòa, TP Biên Hòa, Đồng Nai',
        '2000-12-02T00:00:00', 762594445, 8577588796.0, '2023-09-23T00:00:00', 'NVVP', 'EMPLOYEE', 'VBAAVNVX',
        5990205783878.0, 'FIXED_TERM', NULL, NULL, '2023-09-23T00:00:00', NULL, 1, NULL, NULL, NULL, NULL, 'danh.doan@masi.vn', NULL,
        'WORKING', '2024-09-17T16:20:06.314605', '2024-09-17T16:20:06.314605', False, True, 7000000.0, NULL,
        '2023-09-23T00:00:00', '2023-12-23T00:00:00','MMS')
on conflict do nothing;

INSERT INTO employee (id, first_name, last_name, email, phone_number, hire_date, salary, commission_pct, is_active,
                      workspace_id, full_name)
VALUES ('d6cd8ee1-1dec-4db8-9f2c-7a68ef9a338c'::uuid, 'Minh', 'Nguyễn Bảo Nhựt', 'minh.nguyen@masi.vn', 362966611, NULL,
        0, 0, True, 'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 'Nguyễn Bảo Nhựt Minh'),
       ('7569db99-a59b-4394-aabb-945ac5c877e6'::uuid, 'Huỳnh', 'Trần Trung', 'huynh.tran@masi.vn', 916682439, NULL, 0,
        0, True, 'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 'Trần Trung Huỳnh'),
       ('4977b7e2-d83f-4f10-9734-a207de592708'::uuid, 'Đức', 'Nguyễn Hữu', 'duc.nguyen@masi.vn', 902811712, NULL, 0, 0,
        True, 'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 'Nguyễn Hữu Đức'),
       ('1ea21b81-5130-44b3-8baa-cd243ebba8bb'::uuid, 'Lý', 'Nguyễn Thị Thiên', 'ly.nguyen@masi.vn', 336444203, NULL, 0,
        0, True, 'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 'Nguyễn Thị Thiên Lý'),
       ('ae6d06a1-8e11-4e2a-aab6-1da0ee8c61fe'::uuid, 'Danh', 'Đoàn Trần Cao', 'danh.doan@masi.vn', 762594445,
        '2023-09-23T00:00:00', 0, 0, True, 'b4f430bc-e3d4-4d50-9d04-7b88dd8e99d3'::uuid, 'Đoàn Trần Cao Danh')
on conflict do nothing;
-- INSERT INTO "public"."masi_user_authority" ("user_id", "user_authority")
-- VALUES ('d6cd8ee1-1dec-4db8-9f2c-7a68ef9a338c', 'ROLE_ADMIN'),
--        ('7569db99-a59b-4394-aabb-945ac5c877e6', 'ROLE_USER'),
--        ('4977b7e2-d83f-4f10-9734-a207de592708', 'ROLE_USER'),
--        ('1ea21b81-5130-44b3-8baa-cd243ebba8bb', 'ROLE_USER'),
--        ('ae6d06a1-8e11-4e2a-aab6-1da0ee8c61fe', 'ROLE_USER');
update "MasiEmployee".public.employee_profile set company = 'MMS' where id in
                                                ('d6cd8ee1-1dec-4db8-9f2c-7a68ef9a338c',
                                                 '7569db99-a59b-4394-aabb-945ac5c877e6',
                                                 '4977b7e2-d83f-4f10-9734-a207de592708',
                                                 '1ea21b81-5130-44b3-8baa-cd243ebba8bb',
                                                 'ae6d06a1-8e11-4e2a-aab6-1da0ee8c61fe');
update "MasiEmployee".public.employee_id_sequence
set current_sequence = 10
where workspace_id = 'MMS';
