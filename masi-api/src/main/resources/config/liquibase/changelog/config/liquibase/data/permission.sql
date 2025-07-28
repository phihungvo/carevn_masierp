INSERT INTO "masi_authority" ("name", "description", "resource", action)
VALUES ('PERMISSION.ACCOUNT.READ', 'Quyền xem tài khoản', 'ACCOUNT', 'READ')
on conflict do nothing;
INSERT INTO "masi_authority" ("name", "description", "resource", action)
VALUES ('PERMISSION.ACCOUNT.CREATE', 'Quyền tạo tài khoản', 'ACCOUNT', 'CREATE')
on conflict do nothing;
INSERT INTO "masi_authority" ("name", "description", "resource", action)
VALUES ('PERMISSION.ACCOUNT.UPDATE', 'Quyền cập nhật tài khoản', 'ACCOUNT', 'UPDATE')
on conflict do nothing;
INSERT INTO "masi_authority" ("name", "description", "resource", action)
VALUES ('PERMISSION.ACCOUNT.DELETE', 'Quyền xóa tài khoản', 'ACCOUNT', 'DELETE')
on conflict do nothing;

INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.MANUFACTURER_ORDER.CREATE', 'Quyền Tạo mới Lệnh sản xuất', 'CREATE', 'manufacturer_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.MANUFACTURER_ORDER.READ', 'Quyền Xem tất cả Lệnh sản xuất', 'READ', 'manufacturer_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.MANUFACTURER_ORDER.UPDATE', 'Quyền Cập nhật Lệnh sản xuất', 'UPDATE', 'manufacturer_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.MANUFACTURER_ORDER.DELETE', 'Quyền Xóa Lệnh sản xuất', 'DELETE', 'manufacturer_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.MANUFACTURER_ORDER.REVIEW', 'Quyền Xét duyệt Lệnh sản xuất', 'REVIEW', 'manufacturer_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_ORDER.CREATE', 'Quyền Tạo mới Công đoạn sản xuất', 'CREATE', 'work_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_ORDER.READ', 'Quyền Xem tất cả Công đoạn sản xuất', 'READ', 'work_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_ORDER.UPDATE', 'Quyền Cập nhật Công đoạn sản xuất', 'UPDATE', 'work_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_ORDER.DELETE', 'Quyền Xóa Công đoạn sản xuất', 'DELETE', 'work_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_ORDER.REVIEW', 'Quyền Xét duyệt Công đoạn sản xuất', 'REVIEW', 'work_order')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_PACKAGE.CREATE', 'Quyền Tạo mới Bao bì sản phẩm', 'CREATE', 'product_package')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_PACKAGE.READ', 'Quyền Xem tất cả Bao bì sản phẩm', 'READ', 'product_package')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_PACKAGE.UPDATE', 'Quyền Cập nhật Bao bì sản phẩm', 'UPDATE', 'product_package')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_PACKAGE.DELETE', 'Quyền Xóa Bao bì sản phẩm', 'DELETE', 'product_package')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_PACKAGE.REVIEW', 'Quyền Xét duyệt Bao bì sản phẩm', 'REVIEW', 'product_package')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.QUALITY_CHECK_SAMPLE.CREATE', 'Quyền Tạo mới Kiểm tra chất lượng', 'CREATE', 'quality_check_sample')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.QUALITY_CHECK_SAMPLE.READ', 'Quyền Xem tất cả Kiểm tra chất lượng', 'READ', 'quality_check_sample')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.QUALITY_CHECK_SAMPLE.UPDATE', 'Quyền Cập nhật Kiểm tra chất lượng', 'UPDATE',
        'quality_check_sample')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.QUALITY_CHECK_SAMPLE.DELETE', 'Quyền Xóa Kiểm tra chất lượng', 'DELETE', 'quality_check_sample')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.QUALITY_CHECK_SAMPLE.REVIEW', 'Quyền Xét duyệt Kiểm tra chất lượng', 'REVIEW',
        'quality_check_sample')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.SAMPLE_DISPOSAL.CREATE', 'Quyền Tạo mới Hủy mẫu kiểm thử', 'CREATE', 'sample_disposal')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.SAMPLE_DISPOSAL.READ', 'Quyền Xem tất cả Hủy mẫu kiểm thử', 'READ', 'sample_disposal')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.SAMPLE_DISPOSAL.UPDATE', 'Quyền Cập nhật Hủy mẫu kiểm thử', 'UPDATE', 'sample_disposal')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.SAMPLE_DISPOSAL.DELETE', 'Quyền Xóa Hủy mẫu kiểm thử', 'DELETE', 'sample_disposal')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.SAMPLE_DISPOSAL.REVIEW', 'Quyền Xét duyệt Hủy mẫu kiểm thử', 'REVIEW', 'sample_disposal')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_MAINTENANCE.CREATE', 'Quyền Tạo mới Bảo trì sản phẩm', 'CREATE', 'product_maintenance')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_MAINTENANCE.READ', 'Quyền Xem tất cả Bảo trì sản phẩm', 'READ', 'product_maintenance')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_MAINTENANCE.UPDATE', 'Quyền Cập nhật Bảo trì sản phẩm', 'UPDATE', 'product_maintenance')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_MAINTENANCE.DELETE', 'Quyền Xóa Bảo trì sản phẩm', 'DELETE', 'product_maintenance')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_MAINTENANCE.REVIEW', 'Quyền Xét duyệt Bảo trì sản phẩm', 'REVIEW', 'product_maintenance')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_ROUTING.CREATE', 'Quyền Tạo mới Định tuyến sản xuất', 'CREATE', 'product_routing')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_ROUTING.READ', 'Quyền Xem tất cả Định tuyến sản xuất', 'READ', 'product_routing')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_ROUTING.UPDATE', 'Quyền Cập nhật Định tuyến sản xuất', 'UPDATE', 'product_routing')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_ROUTING.DELETE', 'Quyền Xóa Định tuyến sản xuất', 'DELETE', 'product_routing')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_ROUTING.REVIEW', 'Quyền Xét duyệt Định tuyến sản xuất', 'REVIEW', 'product_routing')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_STANDARD.CREATE', 'Quyền Tạo mới Định mức sản xuất', 'CREATE', 'product_standard')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_STANDARD.READ', 'Quyền Xem tất cả Định mức sản xuất', 'READ', 'product_standard')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_STANDARD.UPDATE', 'Quyền Cập nhật Định mức sản xuất', 'UPDATE', 'product_standard')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_STANDARD.DELETE', 'Quyền Xóa Định mức sản xuất', 'DELETE', 'product_standard')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.PRODUCT_STANDARD.REVIEW', 'Quyền Xét duyệt Định mức sản xuất', 'REVIEW', 'product_standard')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_CENTER.CREATE', 'Quyền Tạo mới Cụm máy sản xuất', 'CREATE', 'work_center')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_CENTER.READ', 'Quyền Xem tất cả Cụm máy sản xuất', 'READ', 'work_center')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_CENTER.UPDATE', 'Quyền Cập nhật Cụm máy sản xuất', 'UPDATE', 'work_center')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_CENTER.DELETE', 'Quyền Xóa Cụm máy sản xuất', 'DELETE', 'work_center')
on conflict(name) do nothing;
INSERT INTO "public"."masi_authority" ("name", "description", "action", "resource")
VALUES ('PERMISSION.WORK_CENTER.REVIEW', 'Quyền Xét duyệt Cụm máy sản xuất', 'REVIEW', 'work_center')
on conflict(name) do nothing;
-- CREATE = 'CREATE',
--   READ = 'READ',
-- UPDATE = 'UPDATE',
-- DELETE = 'DELETE',
--     REVIEW = 'REVIEW',
--     CREATE_MANY = 'CREATE_MANY',
--     EXPORT = 'EXPORT',
-- insert into "public"."masi_user_authority" ("user_id", "user_authority")
-- values ('e9575666-d349-e182-2d7a-742b4a3086a8', 'ROLE_ADMIN'),
--        ('16567667-edaf-9c63-8363-d6ce74363289', 'ROLE_ADMIN'),
--        ('7fc80f67-cd8e-deb8-1e2a-1d3e605ff353', 'ROLE_ADMIN'),
--        ('53ed8642-028d-79bf-42a9-b52a37f5a87c', 'ROLE_ADMIN'),
--        ('f7fc293c-f482-3640-6f37-c6a6932886c9', 'ROLE_ADMIN'),
--        ('096817e3-5262-4e21-ae2b-519d92b87c4a', 'ROLE_ADMIN'),
--        ('59f3a652-f416-68ed-a4eb-767fea09d3ee', 'ROLE_ADMIN'),
--        ('6d4b19a0-ce74-1d2b-8136-f301cce7f709', 'ROLE_ADMIN')
-- on conflict do nothing;
-- RESOURCE = MANUFACTURER_ORDER, WORK_ORDER, PRODUCT_PACKAGE, QUALITY_CHECK_SAMPLE, SAMPLE_DISPOSAL, PRODUCT_MAINTENANCE, PRODUCT_MAINTENANCE_DETAIL, PRODUCT_MAINTENANCE, PRODUCT_ROUTING, PRODUCT_STANDARD, WORK_CENTER



