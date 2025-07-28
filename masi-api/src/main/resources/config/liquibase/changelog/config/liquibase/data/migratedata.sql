
        /* pl/pgsql here */

        INSERT INTO "company" ("id", "name", "description", "parent_id", "normalized_name")
        VALUES ('324b3d1e-8f9f-42be-acb4-2f4908be6b72', 'Masi', 'Công ty mẹ Masi', NULL, 'MASI')
        ON CONFLICT DO NOTHING;
        INSERT INTO "company" ("id", "name", "description", "parent_id", "normalized_name")
        VALUES ('95545ce2-c3a2-4b50-8c61-c4ea2f5b302d', 'Kim Long', NULL, '324b3d1e-8f9f-42be-acb4-2f4908be6b72',
                'KIM_LONG')
        ON CONFLICT DO NOTHING;
        INSERT INTO "company" ("id", "name", "description", "parent_id", "normalized_name")
        VALUES ('bc0056b8-a1ab-45c3-8285-7e1a8e520cb1', 'MMS', NULL, '324b3d1e-8f9f-42be-acb4-2f4908be6b72', 'MMS')
        ON CONFLICT DO NOTHING;

        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('PERMISSION.GROUP.CREATE', 'Quyền tạo mới bộ phận')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('PERMISSION.GROUP.READ', 'Quyền xem bộ phận')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('PERMISSION.GROUP.UPDATE', 'Quyền cập nhật bộ phận')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('PERMISSION.GROUP.DELETE', 'Quyền xóa bộ phận')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('ROLE_ADMIN', 'Toàn quyền')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('ROLE_USER', '...')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('ROLE_DIRECTOR', 'Giám đốc của công ty trực thuộc')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('ROLE_DEPARTMENT_MANAGER', 'trưởng bộ phận')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('ROLE_WORKER', 'Nhân viên nhà máy')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_authority" ("name", "description")
        VALUES ('ROLE_STAFF', 'Nhân viên văn phòng')
        ON CONFLICT DO NOTHING;
        DELETE FROM masi_authority WHERE name ilike 'PERMISSION.%';
        alter table masi_group_authority
            drop constraint IF EXISTS fk_group_authority_group_authority;
        alter table masi_group_authority
            drop constraint IF EXISTS fk_group_authority_group_id;
        alter table masi_group_user
            drop constraint IF EXISTS fk_group_user_group_id;
        alter table masi_group_user
            drop constraint IF EXISTS fk_group_user_user_id;
        alter table masi_user_authority
            drop constraint IF EXISTS fk_authority_name;
        alter table masi_user_authority
            drop constraint IF EXISTS fk_user_id;

        alter table masi_group_authority
            add constraint fk_group_authority_group_authority FOREIGN KEY (group_authority) REFERENCES masi_authority (name) ON DELETE CASCADE;
        alter table masi_group_authority
            add constraint fk_group_authority_group_id FOREIGN KEY (group_id) REFERENCES masi_group (id) ON DELETE CASCADE;
        alter table masi_group_user
            add constraint fk_group_user_group_id FOREIGN KEY (group_id) REFERENCES masi_group (id) ON DELETE CASCADE;
        alter table masi_group_user
            add constraint fk_group_user_user_id FOREIGN KEY (user_id) REFERENCES masi_user (id) ON DELETE CASCADE;
        alter table masi_user_authority
            add constraint fk_authority_name FOREIGN KEY (user_authority) REFERENCES masi_authority (name) ON DELETE CASCADE;
        alter table masi_user_authority
            add constraint fk_user_id FOREIGN KEY (user_id) REFERENCES masi_user (id) ON DELETE CASCADE;
        UPDATE masi_authority set resource = 'ROLE' where name ilike 'ROLE_%';
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_VIOLATION.CREATE', 'Quyền Tạo mới Vi phạm chấm công', 'CREATE',
                'time_keeping_violation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_VIOLATION.READ', 'Quyền Xem tất cả Vi phạm chấm công', 'READ',
                'time_keeping_violation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_VIOLATION.UPDATE', 'Quyền Cập nhật Vi phạm chấm công', 'UPDATE',
                'time_keeping_violation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_VIOLATION.DELETE', 'Quyền Xóa Vi phạm chấm công', 'DELETE',
                'time_keeping_violation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_VIOLATION.REVIEW', 'Quyền Xét duyệt Vi phạm chấm công', 'REVIEW',
                'time_keeping_violation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EXPLANATION_REVIEW.CREATE', 'Quyền Tạo mới Giải trình chấm công', 'CREATE',
                'explanation_review')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EXPLANATION_REVIEW.READ', 'Quyền Xem tất cả Giải trình chấm công', 'READ',
                'explanation_review')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EXPLANATION_REVIEW.UPDATE', 'Quyền Cập nhật Giải trình chấm công', 'UPDATE',
                'explanation_review')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EXPLANATION_REVIEW.DELETE', 'Quyền Xóa Giải trình chấm công', 'DELETE',
                'explanation_review')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EXPLANATION_REVIEW.REVIEW', 'Quyền Xét duyệt Giải trình chấm công', 'REVIEW',
                'explanation_review')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERSONAL_MONTHLY_TIMESHEET.CREATE', 'Quyền Tạo mới Bảng chấm công tháng', 'CREATE',
                'personal_monthly_timesheet')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERSONAL_MONTHLY_TIMESHEET.READ', 'Quyền Xem tất cả Bảng chấm công tháng', 'READ',
                'personal_monthly_timesheet')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERSONAL_MONTHLY_TIMESHEET.UPDATE', 'Quyền Cập nhật Bảng chấm công tháng', 'UPDATE',
                'personal_monthly_timesheet')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERSONAL_MONTHLY_TIMESHEET.DELETE', 'Quyền Xóa Bảng chấm công tháng', 'DELETE',
                'personal_monthly_timesheet')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERSONAL_MONTHLY_TIMESHEET.REVIEW', 'Quyền Xét duyệt Bảng chấm công tháng', 'REVIEW',
                'personal_monthly_timesheet')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_EXPLANATION.CREATE', 'Quyền Tạo mới Giải trình chấm công', 'CREATE',
                'time_keeping_explanation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_EXPLANATION.READ', 'Quyền Xem tất cả Giải trình chấm công', 'READ',
                'time_keeping_explanation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_EXPLANATION.UPDATE', 'Quyền Cập nhật Giải trình chấm công', 'UPDATE',
                'time_keeping_explanation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_EXPLANATION.DELETE', 'Quyền Xóa Giải trình chấm công', 'DELETE',
                'time_keeping_explanation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING_EXPLANATION.REVIEW', 'Quyền Xét duyệt Giải trình chấm công', 'REVIEW',
                'time_keeping_explanation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING.CREATE', 'Quyền Tạo mới Chấm công', 'CREATE', 'time_keeping')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING.READ', 'Quyền Xem tất cả Chấm công', 'READ', 'time_keeping')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING.UPDATE', 'Quyền Cập nhật Chấm công', 'UPDATE', 'time_keeping')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING.DELETE', 'Quyền Xóa Chấm công', 'DELETE', 'time_keeping')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING.REVIEW', 'Quyền Xét duyệt Chấm công', 'REVIEW', 'time_keeping')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REQUEST.CREATE', 'Quyền Tạo mới Đơn xin nghỉ phép', 'CREATE', 'leave_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REQUEST.READ', 'Quyền Xem tất cả Đơn xin nghỉ phép', 'READ', 'leave_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REQUEST.UPDATE', 'Quyền Cập nhật Đơn xin nghỉ phép', 'UPDATE', 'leave_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REQUEST.DELETE', 'Quyền Xóa Đơn xin nghỉ phép', 'DELETE', 'leave_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REQUEST.REVIEW', 'Quyền Xét duyệt Đơn xin nghỉ phép', 'REVIEW', 'leave_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CUSTOMER.CREATE', 'Quyền Tạo mới Khách hàng', 'CREATE', 'customer')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CUSTOMER.READ', 'Quyền Xem tất cả Khách hàng', 'READ', 'customer')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CUSTOMER.UPDATE', 'Quyền Cập nhật Khách hàng', 'UPDATE', 'customer')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CUSTOMER.DELETE', 'Quyền Xóa Khách hàng', 'DELETE', 'customer')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.QUOTATION.CREATE', 'Quyền Tạo mới Báo giá', 'CREATE', 'quotation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.QUOTATION.READ', 'Quyền Xem tất cả Báo giá', 'READ', 'quotation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.QUOTATION.UPDATE', 'Quyền Cập nhật Báo giá', 'UPDATE', 'quotation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.QUOTATION.DELETE', 'Quyền Xóa Báo giá', 'DELETE', 'quotation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.QUOTATION.REVIEW', 'Quyền Xét duyệt Báo giá', 'REVIEW', 'quotation')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CONTRACT.CREATE', 'Quyền Tạo mới Hợp đồng', 'CREATE', 'contract')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CONTRACT.READ', 'Quyền Xem tất cả Hợp đồng', 'READ', 'contract')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CONTRACT.UPDATE', 'Quyền Cập nhật Hợp đồng', 'UPDATE', 'contract')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CONTRACT.DELETE', 'Quyền Xóa Hợp đồng', 'DELETE', 'contract')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.CONTRACT.REVIEW', 'Quyền Xét duyệt Hợp đồng', 'REVIEW', 'contract')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PURCHASE_REQUEST.CREATE', 'Quyền Tạo mới Đề nghị thu mua', 'CREATE', 'purchase_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PURCHASE_REQUEST.READ', 'Quyền Xem tất cả Đề nghị thu mua', 'READ', 'purchase_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PURCHASE_REQUEST.UPDATE', 'Quyền Cập nhật Đề nghị thu mua', 'UPDATE', 'purchase_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PURCHASE_REQUEST.DELETE', 'Quyền Xóa Đề nghị thu mua', 'DELETE', 'purchase_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PURCHASE_REQUEST.REVIEW', 'Quyền Xét duyệt Đề nghị thu mua', 'REVIEW', 'purchase_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ORDER.CREATE', 'Quyền Tạo mới Đơn đặt hàng', 'CREATE', 'order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ORDER.READ', 'Quyền Xem tất cả Đơn đặt hàng', 'READ', 'order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ORDER.UPDATE', 'Quyền Cập nhật Đơn đặt hàng', 'UPDATE', 'order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ORDER.DELETE', 'Quyền Xóa Đơn đặt hàng', 'DELETE', 'order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ORDER.REVIEW', 'Quyền Xét duyệt Đơn đặt hàng', 'REVIEW', 'order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EMPLOYEE.CREATE', 'Quyền Tạo mới Nhân viên', 'CREATE', 'employee')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EMPLOYEE.READ', 'Quyền Xem tất cả Nhân viên', 'READ', 'employee')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EMPLOYEE.UPDATE', 'Quyền Cập nhật Nhân viên', 'UPDATE', 'employee')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.EMPLOYEE.DELETE', 'Quyền Xóa Nhân viên', 'DELETE', 'employee')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REGIME_REQUEST.CREATE', 'Quyền Tạo mới Đăng ký nghỉ chế độ', 'CREATE',
                'leave_regime_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REGIME_REQUEST.READ', 'Quyền Xem tất cả Đăng ký nghỉ chế độ', 'READ',
                'leave_regime_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REGIME_REQUEST.UPDATE', 'Quyền Cập nhật Đăng ký nghỉ chế độ', 'UPDATE',
                'leave_regime_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REGIME_REQUEST.DELETE', 'Quyền Xóa Đăng ký nghỉ chế độ', 'DELETE',
                'leave_regime_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.LEAVE_REGIME_REQUEST.REVIEW', 'Quyền Xét duyệt Đăng ký nghỉ chế độ', 'REVIEW',
                'leave_regime_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ANNUAL_LEAVE.CREATE', 'Quyền Tạo mới Nghỉ phép hàng năm', 'CREATE', 'annual_leave')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ANNUAL_LEAVE.READ', 'Quyền Xem tất cả Nghỉ phép hàng năm', 'READ', 'annual_leave')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.ANNUAL_LEAVE.UPDATE', 'Quyền Cập nhật Nghỉ phép hàng năm', 'UPDATE', 'annual_leave')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.RECRUITMENT_REQUEST.CREATE', 'Quyền Tạo mới Yêu cầu tuyển dụng', 'CREATE',
                'recruitment_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.RECRUITMENT_REQUEST.READ', 'Quyền Xem tất cả Yêu cầu tuyển dụng', 'READ',
                'recruitment_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.RECRUITMENT_REQUEST.UPDATE', 'Quyền Cập nhật Yêu cầu tuyển dụng', 'UPDATE',
                'recruitment_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.RECRUITMENT_REQUEST.DELETE', 'Quyền Xóa Yêu cầu tuyển dụng', 'DELETE',
                'recruitment_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.RECRUITMENT_REQUEST.REVIEW', 'Quyền Xét duyệt Yêu cầu tuyển dụng', 'REVIEW',
                'recruitment_request')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.INTERVIEW_SCHEDULE.CREATE', 'Quyền Tạo mới Lịch phỏng vấn', 'CREATE', 'interview_schedule')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.INTERVIEW_SCHEDULE.READ', 'Quyền Xem tất cả Lịch phỏng vấn', 'READ', 'interview_schedule')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.INTERVIEW_SCHEDULE.UPDATE', 'Quyền Cập nhật Lịch phỏng vấn', 'UPDATE', 'interview_schedule')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.INTERVIEW_SCHEDULE.DELETE', 'Quyền Xóa Lịch phỏng vấn', 'DELETE', 'interview_schedule')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.INTERVIEW_SCHEDULE.REVIEW', 'Quyền Xét duyệt Lịch phỏng vấn', 'REVIEW',
                'interview_schedule')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER.CREATE', 'Quyền Tạo mới Đơn mua đồng phục', 'CREATE', 'uniform_order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER.READ', 'Quyền Xem tất cả Đơn mua đồng phục', 'READ', 'uniform_order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER.UPDATE', 'Quyền Cập nhật Đơn mua đồng phục', 'UPDATE', 'uniform_order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER.DELETE', 'Quyền Xóa Đơn mua đồng phục', 'DELETE', 'uniform_order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER.REVIEW', 'Quyền Xét duyệt Đơn mua đồng phục', 'REVIEW', 'uniform_order')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER_STOCK.CREATE', 'Quyền Tạo mới Kho đồng phục', 'CREATE', 'uniform_order_stock')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER_STOCK.READ', 'Quyền Xem tất cả Kho đồng phục', 'READ', 'uniform_order_stock')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER_STOCK.UPDATE', 'Quyền Cập nhật Kho đồng phục', 'UPDATE',
                'uniform_order_stock')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER_STOCK.DELETE', 'Quyền Xóa Kho đồng phục', 'DELETE', 'uniform_order_stock')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_ORDER_STOCK.REVIEW', 'Quyền Xét duyệt Kho đồng phục', 'REVIEW',
                'uniform_order_stock')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_RELEASE.CREATE', 'Quyền Tạo mới Xuất đồng phục', 'CREATE', 'uniform_release')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_RELEASE.READ', 'Quyền Xem tất cả Xuất đồng phục', 'READ', 'uniform_release')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_RELEASE.UPDATE', 'Quyền Cập nhật Xuất đồng phục', 'UPDATE', 'uniform_release')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_RELEASE.DELETE', 'Quyền Xóa Xuất đồng phục', 'DELETE', 'uniform_release')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.UNIFORM_RELEASE.REVIEW', 'Quyền Xét duyệt Xuất đồng phục', 'REVIEW', 'uniform_release')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.REPORT.READ', 'Quyền Xem tất cả Báo cáo', 'READ', 'REPORT')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERMISION.READ', 'Quyền Xem tất cả Quyền', 'READ', 'PERMISION')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERMISION.CREATE', 'Quyền Tạo mới Quyền', 'CREATE', 'PERMISION')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERMISION.UPDATE', 'Quyền Cập nhật Quyền', 'UPDATE', 'PERMISION')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERMISION.DELETE', 'Quyền Xóa Quyền', 'DELETE', 'PERMISION')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.WORKSPACE.READ', 'Quyền Xem tất cả Workspace', 'READ', 'WORKSPACE')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.WORKSPACE.CREATE', 'Quyền Tạo mới Workspace', 'CREATE', 'WORKSPACE')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.WORKSPACE.UPDATE', 'Quyền Cập nhật Workspace', 'UPDATE', 'WORKSPACE')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.WORKSPACE.DELETE', 'Quyền Xóa Workspace', 'DELETE', 'WORKSPACE')
        on conflict(name) do nothing;

        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING.CREATE_MANY', 'Quyền được chấm công hàng loạt', 'CREATE_MANY', 'time_keeping')
        on conflict(name) do nothing;

        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.PERSONAL_MONTHLY_TIMESHEET.EXPORT', 'Quyền xuất bảng chấm công tháng', 'EXPORT', 'personal_monthly_timesheet')
        on conflict(name) do nothing;
        INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
        VALUES ('PERMISSION.TIME_KEEPING.EXPORT', 'Quyền xuất bảng chấm công', 'EXPORT', 'time_keeping')
        on conflict(name) do nothing;

        truncate table masi_group cascade;

        INSERT INTO "masi_group" ("id", "name", "description", "created_by", "created_date", "last_modified_by",
                                  "last_modified_date", "activated", "company_id", "normalized_name")
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'Bộ phận kinh doanh', 'Bộ phận kinh doanh', 'system',
                '2024-07-17 00:00:00', NULL, '2024-07-17 00:00:00', 't', '95545ce2-c3a2-4b50-8c61-c4ea2f5b302d',
                'SALE');
        INSERT INTO "masi_group" ("id", "name", "description", "created_by", "created_date", "last_modified_by",
                                  "last_modified_date", "activated", "company_id", "normalized_name")
        VALUES ('21839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'Bộ phận logistics', 'Bộ phận logistics', 'system',
                '2024-07-17 00:00:00', NULL, '2024-07-17 00:00:00', 't', '95545ce2-c3a2-4b50-8c61-c4ea2f5b302d',
                'LOGPUR')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_group" ("id", "name", "description", "created_by", "created_date", "last_modified_by",
                                  "last_modified_date", "activated", "company_id", "normalized_name")
        VALUES ('5e8a3e53-4d6f-4a49-8b6b-6c4c14dd2b2b', 'Bộ phận Sale', 'Bộ phận Sale', 'system', '2024-07-17 00:00:00',
                NULL,
                '2024-07-17 00:00:00', 't', '95545ce2-c3a2-4b50-8c61-c4ea2f5b302d', 'SALES')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_group" ("id", "name", "description", "created_by", "created_date", "last_modified_by",
                                  "last_modified_date", "activated", "company_id", "normalized_name")
        VALUES ('9f9c3e53-8d8e-4a4b-bd9d-8e4c14dd2b2d', 'Bộ phận kiểm thử', 'Bộ phận kiểm thử', 'system',
                '2024-07-17 00:00:00',
                NULL, '2024-07-17 00:00:00', 't', '95545ce2-c3a2-4b50-8c61-c4ea2f5b302d', 'TEST')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_group" ("id", "name", "description", "created_by", "created_date", "last_modified_by",
                                  "last_modified_date", "activated", "company_id", "normalized_name")
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'Bộ phận hành chính nhân sự', 'Bộ phận hành chính nhân sự',
                'system',
                '2024-07-17 00:00:00', NULL, '2024-07-17 00:00:00', 't', '95545ce2-c3a2-4b50-8c61-c4ea2f5b302d', 'HR')
        ON CONFLICT DO NOTHING;
        INSERT INTO "masi_group" ("id", "name", "description", "created_by", "created_date", "last_modified_by",
                                  "last_modified_date", "activated", "company_id", "normalized_name")
        VALUES ('803b110e-5804-424b-bd22-7aecde0e46f5', 'Đội sản xuất', 'Đội sản xuất', 'system', '2024-07-18 15:18:29',
                NULL,
                '2024-07-19 15:18:32', 't', '95545ce2-c3a2-4b50-8c61-c4ea2f5b302d', 'WORKER')
        ON CONFLICT DO NOTHING;


        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.EMPLOYEE.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.EMPLOYEE.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.EMPLOYEE.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.EMPLOYEE.DELETE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.DELETE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.REVIEW')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.LEAVE_REGIME_REQUEST.REVIEW')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.ANNUAL_LEAVE.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.QUOTATION.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.QUOTATION.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.QUOTATION.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.QUOTATION.DELETE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.QUOTATION.REVIEW')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.ORDER.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.ORDER.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.ORDER.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.ORDER.DELETE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.ORDER.REVIEW')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.LEAVE_REGIME_REQUEST.REVIEW')
        ON CONFLICT DO NOTHING;

-- INSERT INTO masi_group_authority(group_id,group_authority) VALUES ('9f9c3e53-8d8e-4a4b-bd9d-8e4c14dd2b2d','PERMISSION.SAMPLE_DISPOSAL.REVIEW')  ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.CONTRACT.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.CONTRACT.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.CONTRACT.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', 'PERMISSION.CONTRACT.DELETE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('803b110e-5804-424b-bd22-7aecde0e46f5', 'PERMISSION.PURCHASE_REQUEST.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('803b110e-5804-424b-bd22-7aecde0e46f5', 'PERMISSION.PURCHASE_REQUEST.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('803b110e-5804-424b-bd22-7aecde0e46f5', 'PERMISSION.PURCHASE_REQUEST.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('803b110e-5804-424b-bd22-7aecde0e46f5', 'PERMISSION.PURCHASE_REQUEST.DELETE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('803b110e-5804-424b-bd22-7aecde0e46f5', 'PERMISSION.PURCHASE_REQUEST.REVIEW')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.DELETE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.RECRUITMENT_REQUEST.REVIEW')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.LEAVE_REGIME_REQUEST.REVIEW')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'PERMISSION.ANNUAL_LEAVE.UPDATE')
        ON CONFLICT DO NOTHING;
-- INSERT INTO masi_group_authority(group_id,group_authority) VALUES ('9f9c3e53-8d8e-4a4b-bd9d-8e4c14dd2b2d','PERMISSION.ANNUAL_LEAVE.REVIEW')  ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('9f9c3e53-8d8e-4a4b-bd9d-8e4c14dd2b2d', 'PERMISSION.ANNUAL_LEAVE.CREATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('9f9c3e53-8d8e-4a4b-bd9d-8e4c14dd2b2d', 'PERMISSION.ANNUAL_LEAVE.READ')
        ON CONFLICT DO NOTHING;
        INSERT INTO masi_group_authority(group_id, group_authority)
        VALUES ('9f9c3e53-8d8e-4a4b-bd9d-8e4c14dd2b2d', 'PERMISSION.ANNUAL_LEAVE.UPDATE')
        ON CONFLICT DO NOTHING;
        INSERT INTO public.masi_user (id, user_name, password_hash, first_name, last_name, activated, email, created_by,
                                      is_super_admin, employee_id, company_id)
        VALUES ('664dc7ad-3ab9-479c-832c-28efc9f69bcf', 'pham.ky',
                '$2b$10$8UYdL741Z/tXwjBC6/Dx2utHghXAoGTFSXyGO/XTIk7/5SVwXQYlG', 'Kỳ', 'Phạm Đức', 't',
                'MyUyen_Ha14@yahoo.com',
                'system', 'f', '664dc7ad-3ab9-479c-832c-28efc9f69bcf', 'KIM_LONG'),
               ('f6d7228d-600c-4c6c-92e7-abf3bb55e355', 'mai.duong',
                '$2b$10$dNkcfLUvGPWnzbF6jQ0HTuLL85NyveLQ8oaC9iCZzakqNvfMg4iKu', 'Dưỡng', 'Mai Thanh', 't',
                'VietTan_Duong@gmail.com', 'system', 'f', 'f6d7228d-600c-4c6c-92e7-abf3bb55e355', 'KIM_LONG'),
               ('bfaf9de5-ab76-4a40-a05c-e5f518a265a3', 'to.djem',
                '$2b$10$1eRlhHizhmwbJ.zmwpg8FuWigkTtHmP9Hqstlj8lfJDb.HtkymJVy', 'Đém', 'Tô Văn', 't',
                'HaiThao.Phung@yahoo.com',
                'system', 'f', 'bfaf9de5-ab76-4a40-a05c-e5f518a265a3', 'KIM_LONG'),
               ('dfbe6ed2-2834-452f-add7-28e14722b9f7', 'ham.trang',
                '$2b$10$q4UejW3icqIbrEQ9OSgWxOwUpfcktqbQeu5Hbz6jFXAltNtJ5AlG2', 'Trạng', 'Hàm Quang', 't',
                'SonDuong.Ly54@gmail.com', 'system', 'f', 'dfbe6ed2-2834-452f-add7-28e14722b9f7', 'KIM_LONG'),
               ('819bf0b5-6eb9-4039-8b8c-8e30830ea81b', 'thach.linh',
                '$2b$10$hCDfg.mqhUx7uqtn79XnIOZvJMjuh.wa2maiT5IFPZJZGsStgkwNu', 'Linh', 'Thạch', 't',
                'LienKiet0@hotmail.com',
                'system', 'f', '819bf0b5-6eb9-4039-8b8c-8e30830ea81b', 'KIM_LONG'),
               ('62fcad84-8663-44f1-a454-d48eae376277', 'thach.an',
                '$2b$10$2nMVce0kKf5EnaG0NyAg8OJvMIld8QdnZnLvvs6vgBJakj511VhYG', 'An', 'Thạch Ngọc', 't',
                '7kinhPhu_Nguyen@yahoo.com', 'system', 'f', '62fcad84-8663-44f1-a454-d48eae376277', 'KIM_LONG'),
               ('1363cf19-4302-47c1-b2eb-d137acefee9f', 'thach.minh',
                '$2b$10$fkIOfTskqqr0LTIR3n1RPeD3MzoPZ6UU6v0.303eRh5UgAVoxHyFG', 'Minh', 'Thạch', 't',
                '7kinhLuc_Ho@hotmail.com',
                'system', 'f', '1363cf19-4302-47c1-b2eb-d137acefee9f', 'KIM_LONG'),
               ('d37c0c9c-fc92-4f6a-b754-eed1c60fea03', 'thach.khang',
                '$2b$10$7r0M1xVR4ww2oaC4hLRPzuGCoYBnyXm0/LWmWv1SYGQXFgblnWhWy', 'Khang', 'Thạch Huỳnh', 't',
                'HuyenTran42@yahoo.com', 'system', 'f', 'd37c0c9c-fc92-4f6a-b754-eed1c60fea03', 'KIM_LONG'),
               ('2a272402-4f51-4ab1-95c8-c7d98ca20431', 'thach.sang',
                '$2b$10$N2S.Osq3RzIFABYosf9nb.0HbJNf1qq6MmlndbrHvuwUUbePj4v6.', 'Sang', 'Thạch Kim', 't',
                'ChienThang.Vu87@yahoo.com', 'system', 'f', '2a272402-4f51-4ab1-95c8-c7d98ca20431', 'KIM_LONG'),
               ('f73b7906-f323-421d-ac3b-cdeef17631a0', 'son.ni',
                '$2b$10$d4Y4UYlrL2eyjV0bOTHel.uMoeop8wT6sdhRpPqhF3DCERrAXfrha', 'Ni', 'Sơn Đa', 't',
                'VietYen.7ko94@yahoo.com',
                'system', 'f', 'f73b7906-f323-421d-ac3b-cdeef17631a0', 'KIM_LONG'),
               ('567732e6-1df5-4779-bf2f-abf1f8098e12', 'son.ri',
                '$2b$10$vvptB.TJ32gYnAK4hI6W3eDshLD8IMikd3iRQbpdSwkHemZKfs3ee', 'Ri', 'Sơn Sa', 't',
                'VinhLong97@hotmail.com',
                'system', 'f', '567732e6-1df5-4779-bf2f-abf1f8098e12', 'KIM_LONG'),
               ('58edd4cf-0f39-4409-a76d-9f5c0912d3f3', 'son.banh',
                '$2b$10$KDmcxQ1L4bl9luWcMkxs2O8kgK3OSxBWRPIAWIlLs0urOwYme1xOy', 'Banh', 'Sơn Sa', 't',
                'NgocHa_Ha@hotmail.com',
                'system', 'f', '58edd4cf-0f39-4409-a76d-9f5c0912d3f3', 'KIM_LONG'),
               ('af709352-717c-4b6a-a669-1f58395f98a3', 'thach.sin',
                '$2b$10$9iiN4CxTIEoj5Ar3oG2Uvu.tm/IM9Lqq.PUEDUhUhCtWgP8yZ7LWO', 'Sin', 'Thạch', 't',
                'PhungViet_7kang70@gmail.com', 'system', 'f', 'af709352-717c-4b6a-a669-1f58395f98a3', 'KIM_LONG'),
               ('db55140f-cfde-4444-9298-424c158b82af', 'thach.h.linh',
                '$2b$10$EalSv2ojSzatubqOqKpZ9uj5xUVyLHGF0IwClTHxZgtVPUuvfIlLe', 'H.Linh', 'Thạch Ngô', 't',
                'NgocTho87@hotmail.com', 'system', 'f', 'db55140f-cfde-4444-9298-424c158b82af', 'KIM_LONG'),
               ('9f956056-7698-4112-804b-5e6d42170299', 'giang.mel',
                '$2b$10$A0Ul72Ed6TfHEDHiena.De9Kvy8QbdZ.9XXYZFZJWA4CdPmkoQzMS', 'Mel', 'Giang Văn', 't',
                'ThanhNhan8@hotmail.com', 'system', 'f', '9f956056-7698-4112-804b-5e6d42170299', 'KIM_LONG'),
               ('4533d741-a93a-4691-b91d-849c6b879cac', 'kim.giang',
                '$2b$10$9lVHq1PXSfoilZZIT3SW6uiKh8TVDTubLSZ16Y8IxO65lKCNEp7OK', 'Giang', 'Kim Hoàng', 't',
                'TuyetNhung0@yahoo.com', 'system', 'f', '4533d741-a93a-4691-b91d-849c6b879cac', 'KIM_LONG'),
               ('931854e9-78ed-4e81-a59c-206d09b52bd8', 'bien.quang',
                '$2b$10$U5fZI9bBoWwwvJ8VD3rKPO.WlarkjynUoS1acJW8gO4mPAEThg0Au', 'Quang', 'Biện Minh', 't',
                'NgocAnh89@yahoo.com', 'system', 'f', '931854e9-78ed-4e81-a59c-206d09b52bd8', 'KIM_LONG'),
               ('ee471d2b-475a-46e5-af65-a03feba72b13', 'tran.quy',
                '$2b$10$tZ2DaOC90DB5AMSDZcDBuOLDHp6UcVSbM/ekp2lZMMCFXNcgC7xwS', 'Quý', 'Trần Ngọc', 't',
                'NguyenPhong_7koan65@hotmail.com', 'system', 'f', 'ee471d2b-475a-46e5-af65-a03feba72b13', 'KIM_LONG'),
               ('f946d9ae-d7ee-4361-b67c-3d896fd95d47', 'nguyen.truong',
                '$2b$10$NXipQ35Qt6b5BUAaWd6omeuYCjfQC6/qL5LnJJjMCgPXP7j8DFexu', 'Trường', 'Nguyễn Mạnh', 't',
                'CaoPhong.Vuong33@yahoo.com', 'system', 'f', 'f946d9ae-d7ee-4361-b67c-3d896fd95d47', 'KIM_LONG'),
               ('825de780-bfcb-4e45-bc9c-0e8b194f396b', 'bui.nam',
                '$2b$10$VLBQf55LPmOhRa/FMEemi.WZfONvdBU8RK5CNJqsG9G7wNGnQiOzO', 'Nam', 'Bùi Văn', 't',
                'TueNhi_7kang94@gmail.com', 'system', 'f', '825de780-bfcb-4e45-bc9c-0e8b194f396b', 'KIM_LONG'),
               ('c739d521-2815-4d17-a522-ed642636fbd9', 'kien.ri',
                '$2b$10$Bpw.tiDjCRTJPzm9U6Mbw.mzYGrYlYIZx0aeClFRuuDpMez5SzWhq', 'Ri', 'Kiên Thị Sa', 't',
                'KhaiHa41@gmail.com',
                'system', 'f', 'c739d521-2815-4d17-a522-ed642636fbd9', 'KIM_LONG'),
               ('d0ee7b3d-7859-4a2f-a079-4cc82b24de6d', 'djao.thao',
                '$2b$10$gwt0s2.aXiuw68cKsf56auH9VyTEkhuUXay0yXNX6JyNt/ScG0KNW', 'Thảo', 'Đào Thị', 't',
                'BichHau_Ha69@gmail.com', 'system', 'f', 'd0ee7b3d-7859-4a2f-a079-4cc82b24de6d', 'KIM_LONG'),
               ('400a7cae-922c-4c03-96d9-3aa908dd7bb5', 'le.djong',
                '$2b$10$FQG.0G2rMreihGjVxeadAuEZ2vdM7erMpiJD7CZAU2ofL0b/63Rsm', 'Đông', 'Lê Thị', 't',
                'HongKhue.Phan42@yahoo.com', 'system', 'f', '400a7cae-922c-4c03-96d9-3aa908dd7bb5', 'KIM_LONG'),
               ('eab4fb9a-74a8-4a8b-b285-f4d1d0edc812', 'thach.thi',
                '$2b$10$7KejFjlYnNU2Ra7dS4gKreN.lmKOGww8Gn2zYN5Il5IsLUAm68ET6', 'Thỉ', 'Thạch Thị', 't',
                'Thien7kuc_Lam@gmail.com', 'system', 'f', 'eab4fb9a-74a8-4a8b-b285-f4d1d0edc812', 'KIM_LONG'),
               ('c667d584-aa1c-43ba-bf8d-e568e30ae71b', 'son.thai',
                '$2b$10$D5OEC9DXsfkkGXT9Oq5xbemE9urnknjRnkWds4eDBuHUcah3g7/3y', 'Thai', 'Sơn Thị', 't',
                'ThuanPhuong.Duong@hotmail.com', 'system', 'f', 'c667d584-aa1c-43ba-bf8d-e568e30ae71b', 'KIM_LONG'),
               ('af23439e-f1fc-43aa-a85f-eb2d538af0eb', 'truong.thao',
                '$2b$10$YoNaUWarPyXSve4pAb.ek.vs2bUywmX9U1qoMBl/UmqQMdA59riG2', 'Thảo', 'Trương Nữ Phương', 't',
                'LinhDuyen_Le71@yahoo.com', 'system', 'f', 'af23439e-f1fc-43aa-a85f-eb2d538af0eb', 'KIM_LONG'),
               ('fb58bcfa-cbdd-4d7e-b609-8c62abb05515', 'chau.uyen',
                '$2b$10$Sd3vuUph/X3qPTrUhyPLAeDPqsr8WZgmB6UGtUx6.5ocdtYZHp33.', 'Uyên', 'Châu Lê Mỹ', 't',
                'QuocThong90@yahoo.com', 'system', 'f', 'fb58bcfa-cbdd-4d7e-b609-8c62abb05515', 'KIM_LONG'),
               ('b8678dc6-f733-4ad6-b38b-763ef67ca73f', 'tran.loan',
                '$2b$10$lXjMIUm26H4eZ6R8a1mtu.z8MSJ92gvIB/KbWMlOc1gB8Fins8NMe', 'Loan', 'Trần Thị Kim', 't',
                'KimThanh32@hotmail.com', 'system', 'f', 'b8678dc6-f733-4ad6-b38b-763ef67ca73f', 'KIM_LONG'),
               ('ab2f0c6f-26ba-40c9-b994-397bb7cdaec2', 'phan.ha',
                '$2b$10$u4PrXJHbC43hb4151Uk2MeU5PLj/dYuyutf6/pR8bw3.9WtMND9di', 'Hà', 'Phan Thị Bích', 't',
                'haptb@yahoo.com',
                'system', 'f', 'ab2f0c6f-26ba-40c9-b994-397bb7cdaec2', 'KIM_LONG'),
               ('999e1416-a92a-459a-add7-611278b61b35', 'quang.linh',
                '$2b$10$HkFNzQ98h6E.iVZZ5VICR..vBUTJasvVJXpMH9qhaJOPC6H15eCdK', 'Linh', 'Quảng Thị Thùy', 't',
                'linhqtt@hotmail.com', 'system', 'f', '999e1416-a92a-459a-add7-611278b61b35', 'KIM_LONG'),
               ('84b36e89-155e-4c4c-9b61-85cbc8f73589', 'hoang.phuong',
                '$2b$10$bX4v9vwgek.xgzkHFTdu4.O3U4XSPqCy7eL.CWLQ9YAiXuzYoXJJG', 'Phương', 'Hoàng Thị Thu', 't',
                'DiemChi_Pham68@yahoo.com', 'system', 'f', '84b36e89-155e-4c4c-9b61-85cbc8f73589', 'KIM_LONG'),
               ('63946348-aea6-410d-894b-7924e14a1492', 'thach.ni',
                '$2b$10$e7NNZyZGCbeKkPFkQydWMexZ0v7NSoFvcPOyWKz8VzWV3Y3z2.U9y', 'Ni', 'Thạch Kiên Mi', 't',
                'ni.thach-kien-mi366@yahoo.com', 'system', 'f', '63946348-aea6-410d-894b-7924e14a1492', 'KIM_LONG'),
               ('ed3c4f9b-44c7-44ce-b03c-7bb28b91adf2', 'thach.na',
                '$2b$10$TrJx1T7gaLBw1Q4atgEwnOqvpJoK7DY41DUrEb72ojc.JQDzh3FdK', 'Na', 'Thạch', 't',
                'na.thach668@hotmail.com',
                'system', 'f', 'ed3c4f9b-44c7-44ce-b03c-7bb28b91adf2', 'KIM_LONG'),
               ('3250f8d2-ba45-4806-9100-4cfc3feb3076', 'thach.duong',
                '$2b$10$C9UtPemYo/1uqS7.DnbuSODD.5mt/Tgq9KEZTQ1Oi9wsorEfZMVTq', 'Dương', 'Thạch', 't',
                'duong.thach204@outlook.com', 'system', 'f', '3250f8d2-ba45-4806-9100-4cfc3feb3076', 'KIM_LONG'),
               ('29a2e0e4-04ef-4acd-b9a6-7590bde1ca54', 'son.hong',
                '$2b$10$SFM8mVnyZyrhBTJ6egMpDepgV.t5/yuaw2xjyO/mM0Uxpv3TcQd.q', 'Hồng', 'Sơn', 't',
                'hong.son711@gmail.com',
                'system', 'f', '29a2e0e4-04ef-4acd-b9a6-7590bde1ca54', 'KIM_LONG'),
               ('c51d98b8-5099-4b4b-badc-b6cbeabc45d2', 'son.rine',
                '$2b$10$ywKZvW0bZk02lSpbhTjzc.wDIXHGKxaLpRz1pYUJwwbcsrllzCiD2', 'Rine', 'Sơn Sa', 't',
                'rine.son-sa717@outlook.com', 'system', 'f', 'c51d98b8-5099-4b4b-badc-b6cbeabc45d2', 'KIM_LONG'),
               ('73feaef5-7f0a-4124-8898-b079914f05f8', 'nguyen.quynh',
                '$2b$10$vP1ikPRKOh05snv9zwp8u.WtLzSkW4KjYWSlFp2UfOWOBQaGdv6wi', 'Quỳnh', 'Nguyễn Cao', 't',
                'quynh.nguyen-cao146@outlook.com', 'system', 'f', '73feaef5-7f0a-4124-8898-b079914f05f8', 'KIM_LONG')
        ON CONFLICT DO NOTHING;
        INSERT INTO public.masi_user_authority (user_id, user_authority)
        VALUES ('664dc7ad-3ab9-479c-832c-28efc9f69bcf', 'ROLE_USER'),
               ('f6d7228d-600c-4c6c-92e7-abf3bb55e355', 'ROLE_USER'),
               ('bfaf9de5-ab76-4a40-a05c-e5f518a265a3', 'ROLE_USER'),
               ('dfbe6ed2-2834-452f-add7-28e14722b9f7', 'ROLE_USER'),
               ('819bf0b5-6eb9-4039-8b8c-8e30830ea81b', 'ROLE_USER'),
               ('62fcad84-8663-44f1-a454-d48eae376277', 'ROLE_USER'),
               ('1363cf19-4302-47c1-b2eb-d137acefee9f', 'ROLE_USER'),
               ('d37c0c9c-fc92-4f6a-b754-eed1c60fea03', 'ROLE_USER'),
               ('2a272402-4f51-4ab1-95c8-c7d98ca20431', 'ROLE_USER'),
               ('f73b7906-f323-421d-ac3b-cdeef17631a0', 'ROLE_USER'),
               ('567732e6-1df5-4779-bf2f-abf1f8098e12', 'ROLE_USER'),
               ('58edd4cf-0f39-4409-a76d-9f5c0912d3f3', 'ROLE_USER'),
               ('af709352-717c-4b6a-a669-1f58395f98a3', 'ROLE_USER'),
               ('db55140f-cfde-4444-9298-424c158b82af', 'ROLE_USER'),
               ('9f956056-7698-4112-804b-5e6d42170299', 'ROLE_USER'),
               ('4533d741-a93a-4691-b91d-849c6b879cac', 'ROLE_USER'),
               ('931854e9-78ed-4e81-a59c-206d09b52bd8', 'ROLE_USER'),
               ('ee471d2b-475a-46e5-af65-a03feba72b13', 'ROLE_USER'),
               ('f946d9ae-d7ee-4361-b67c-3d896fd95d47', 'ROLE_USER'),
               ('825de780-bfcb-4e45-bc9c-0e8b194f396b', 'ROLE_USER'),
               ('c739d521-2815-4d17-a522-ed642636fbd9', 'ROLE_USER'),
               ('d0ee7b3d-7859-4a2f-a079-4cc82b24de6d', 'ROLE_USER'),
               ('400a7cae-922c-4c03-96d9-3aa908dd7bb5', 'ROLE_USER'),
               ('eab4fb9a-74a8-4a8b-b285-f4d1d0edc812', 'ROLE_USER'),
               ('c667d584-aa1c-43ba-bf8d-e568e30ae71b', 'ROLE_USER'),
               ('af23439e-f1fc-43aa-a85f-eb2d538af0eb', 'ROLE_USER'),
               ('fb58bcfa-cbdd-4d7e-b609-8c62abb05515', 'ROLE_USER'),
               ('b8678dc6-f733-4ad6-b38b-763ef67ca73f', 'ROLE_USER'),
               ('ab2f0c6f-26ba-40c9-b994-397bb7cdaec2', 'ROLE_USER'),
               ('999e1416-a92a-459a-add7-611278b61b35', 'ROLE_USER'),
               ('84b36e89-155e-4c4c-9b61-85cbc8f73589', 'ROLE_USER'),
               ('63946348-aea6-410d-894b-7924e14a1492', 'ROLE_USER'),
               ('ed3c4f9b-44c7-44ce-b03c-7bb28b91adf2', 'ROLE_USER'),
               ('3250f8d2-ba45-4806-9100-4cfc3feb3076', 'ROLE_USER'),
               ('29a2e0e4-04ef-4acd-b9a6-7590bde1ca54', 'ROLE_USER'),
               ('c51d98b8-5099-4b4b-badc-b6cbeabc45d2', 'ROLE_USER'),
               ('999e1416-a92a-459a-add7-611278b61b35', 'ROLE_DIRECTOR'),
               ('63946348-aea6-410d-894b-7924e14a1492', 'ROLE_DEPARTMENT_MANAGER'),
               ('b8678dc6-f733-4ad6-b38b-763ef67ca73f', 'ROLE_DEPARTMENT_MANAGER'),
               ('73feaef5-7f0a-4124-8898-b079914f05f8', 'ROLE_USER')
        ON CONFLICT DO NOTHING;

        INSERT INTO masi_group_user(group_id, user_id)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', '63946348-aea6-410d-894b-7924e14a1492'),
               ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', 'b8678dc6-f733-4ad6-b38b-763ef67ca73f')
        ON CONFLICT DO NOTHING;


        INSERT INTO public.masi_user (id, user_name, password_hash, first_name, last_name, activated, email, created_by,
                                      is_super_admin, employee_id, company_id)
        VALUES ('164dc7ad-3ab9-479c-832c-28efc9f69bca', 'pham.ky.mms',
                '$2b$10$8UYdL741Z/tXwjBC6/Dx2utHghXAoGTFSXyGO/XTIk7/5SVwXQYlG', 'Kỳ MMS', 'Phạm Đức', 't',
                'MyUyen_Ha14mms@yahoo.com',
                'system', 'f', '164dc7ad-3ab9-479c-832c-28efc9f69bca', 'MMS'),
               ('199e1416-a92a-459a-add7-611278b61b11', 'quang.linh.mms',
                '$2b$10$HkFNzQ98h6E.iVZZ5VICR..vBUTJasvVJXpMH9qhaJOPC6H15eCdK', 'Linh MMS', 'Quảng Thị Thùy', 't',
                'linhqttmms@hotmail.com', 'system', 'f', '199e1416-a92a-459a-add7-611278b61b11', 'MMS')
        on conflict do nothing;
        INSERT INTO public.masi_user_authority (user_id, user_authority)
        VALUES ('164dc7ad-3ab9-479c-832c-28efc9f69bca', 'ROLE_USER'),
               ('199e1416-a92a-459a-add7-611278b61b11', 'ROLE_USER'),
               ('199e1416-a92a-459a-add7-611278b61b11', 'ROLE_DIRECTOR'),
               ('164dc7ad-3ab9-479c-832c-28efc9f69bca', 'ROLE_DEPARTMENT_MANAGER'),
               ('199e1416-a92a-459a-add7-611278b61b11', 'ROLE_DEPARTMENT_MANAGER')
        on conflict do nothing;
        INSERT INTO masi_group_user(group_id, user_id)
        VALUES ('3d839e53-2e0d-4a48-8af4-7a3c14dd2b2a', '164dc7ad-3ab9-479c-832c-28efc9f69bca'),
               ('7f8b3e53-6c7d-4a4a-9c8c-7d4c14dd2b2c', '199e1416-a92a-459a-add7-611278b61b11')
        on conflict do nothing;
