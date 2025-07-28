INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('9c7146b7-bd47-4692-a912-6e27d6c7984c', 'KTP', 'Kho thành phẩm', NULL, 't', '2024-09-19 09:45:10', 'system',
        NULL, NULL, NULL, NULL, 'KIM_LONG', 't') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('4b0e4fd8-7ab7-41b3-81e7-bd2d46eae7d4', 'KNL', 'Kho nguyên liệu', NULL, 't', '2024-09-19 09:46:18', 'system',
        NULL, NULL, NULL, NULL, 'KIM_LONG', 't') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('bdb81f26-6bf5-46e6-a54a-d260c0890c49', 'KBTP', 'Kho bán thành phẩm', NULL, 't', '2024-10-01 02:41:26.758064',
        'system', NULL, NULL, NULL, NULL, 'KIM_LONG', 't') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('d4c3e824-8735-419b-b41f-332063edd318', 'KTS', 'Kho tài sản', NULL, 't', '2024-09-19 09:44:29', 'system', NULL,
        NULL, NULL, NULL, 'KIM_LONG', 'f') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('bd1251a8-f59e-441e-b34b-2b2845051a03', 'KTP', 'Kho thành phẩm', NULL, 't', '2024-09-19 09:45:10', 'system',
        NULL, NULL, NULL, NULL, 'MMS', 't') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('f62b5864-9c04-4928-a2a4-193564e537aa', 'KNL', 'Kho nguyên liệu', NULL, 't', '2024-09-19 09:46:18', 'system',
        NULL, NULL, NULL, NULL, 'MMS', 't') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('6b865d57-fb1a-4645-9817-0d8e6a51cb49', 'KBTP', 'Kho bán thành phẩm', NULL, 't', '2024-10-01 02:41:26.758064',
        'system', NULL, NULL, NULL, NULL, 'MMS', 't') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."warehouse_type" ("id", "code", "name", "description", "active", "create_at", "create_by",
                                       "update_at", "update_by", "delete_at", "delete_by", "company", "use_manufacture")
VALUES ('67b1ac0e-2c58-4d63-9d07-f60978a7fadb', 'KTS', 'Kho tài sản', NULL, 't', '2024-09-19 09:44:29', 'system', NULL,
        NULL, NULL, NULL, 'MMS', 'f') ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."uom" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by",
                            "company")
VALUES ('eab9dd9f-54d6-4b91-9fc0-e1aaff641d50', 'Cái', '2024-09-13 17:41:05', 'system', NULL, NULL, NULL, NULL,
        'KIM_LONG') ON CONFLICT ("id") DO NOTHING;;
INSERT INTO "public"."uom" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by",
                            "company")
VALUES ('0c3cf5fe-d928-4f80-a49d-de3062352ea1', 'Kg', '2024-10-02 02:19:39.184844',
        '84b36e89-155e-4c4c-9b61-85cbc8f73589', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;;
INSERT INTO "public"."uom" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by",
                            "company")
VALUES ('f20dcf33-076a-4a05-840a-87e002ccf7e8', 'Tấn', '2024-10-02 02:19:48.273262',
        '84b36e89-155e-4c4c-9b61-85cbc8f73589', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;;
INSERT INTO "public"."uom" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by",
                            "company")
VALUES ('f1c31289-c4bd-4967-aad5-1bfd830d69cc', 'Thùng', '2024-10-02 02:19:54.086007',
        '84b36e89-155e-4c4c-9b61-85cbc8f73589', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;;
INSERT INTO "public"."uom" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by",
                            "company")
VALUES ('7de032b0-4376-4802-a325-3ec7b610fdba', 'Bao', '2024-10-03 06:57:02.206016',
        '84b36e89-155e-4c4c-9b61-85cbc8f73589', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;;
INSERT INTO "public"."uom" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by",
                            "company")
VALUES ('29209891-a6ec-42da-abaf-7bce80a27519', 'gram', '2024-10-09 03:12:34.824803',
        '84b36e89-155e-4c4c-9b61-85cbc8f73589', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;;
INSERT INTO "public"."uom" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by",
                            "company")
VALUES ('d3dee163-6715-4e70-964c-e095520e5791', '123', '2024-11-04 02:21:26.070393',
        '84b36e89-155e-4c4c-9b61-85cbc8f73589', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;;

ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT FALSE;
ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS deleted_by VARCHAR (255);
ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS company VARCHAR (255);
ALTER TABLE incoming_invoice
    ADD COLUMN IF NOT EXISTS pattern_no VARCHAR (255);
ALTER TABLE incoming_invoice
    ADD COLUMN IF NOT EXISTS status VARCHAR (255);
ALTER TABLE incoming_invoice
    ADD COLUMN IF NOT EXISTS currency_code VARCHAR (255);
ALTER TABLE incoming_invoice
    ADD COLUMN IF NOT EXISTS need_approval BOOLEAN;
ALTER TABLE suppliers
    ADD COLUMN IF NOT EXISTS manager_id UUID;
ALTER TABLE incoming_invoice
    ADD COLUMN IF NOT EXISTS supplier_id UUID;
ALTER TABLE supplies_item
    ADD COLUMN IF NOT EXISTS bank_info VARCHAR (255);
ALTER TABLE supplies_item
    ADD COLUMN IF NOT EXISTS note TEXT;

ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS payment_voucher VARCHAR (500);
ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS payment_voucher_amount DOUBLE PRECISION;

ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS remaining_balance DOUBLE PRECISION;
ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS over_spent DOUBLE PRECISION;

ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS created_by VARCHAR (255);

ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ;

ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS updated_by VARCHAR (255);

ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ;

ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS deleted_by VARCHAR (255);
ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ;
ALTER TABLE payment_detail
    ADD COLUMN IF NOT EXISTS department VARCHAR (255);

ALTER TABLE payment_request
    ADD COLUMN IF NOT EXISTS reimbursement_date TIMESTAMPTZ;


-- ////////////// Inventories Type //////////////

INSERT INTO "public"."inventories_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('4ac506e6-b869-d664-2dfa-305284a56b83', 'BO_SUNG', 'Bổ sung', ' ', 't', 'f', '2021-07-14 06:10:52', 'system', NULL, NULL, NULL, 'TyakNbNjBc', 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('a5fe7f6c-4877-bea9-a157-7e02abe006df', 'BU_KHO', 'Bù kho', ' ', 't', 'f', '2015-03-12 16:55:38', 'system', NULL, NULL, NULL, 'KhA7pskq1E', 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('c09fa7ad-ab5a-024f-78e9-0f6500e91d03', 'TON_KHO', 'Tồn kho', ' ', 't', 'f', '2018-09-10 15:20:11', 'system', NULL, NULL, NULL, '3Zhpm2dm7F', 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('31a64fa5-de8d-5ab8-a3e9-c763a142bc8c', 'SAN_XUAT', 'Sản xuất', ' ', 't', 'f', '2003-05-11 18:42:22', 'system', NULL, NULL, NULL, '0qs2Rrl3RD', 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;


-- ////////////// Warehouse //////////////

INSERT INTO "public"."warehouse" ("id", "code", "name", "address", "active", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "warehouse_type_id", "warehouse_type_page") VALUES ('72b7a9f4-dca8-be65-3a4d-a171832a85fd', 'KHO_TS', 'Kho tài sản', NULL, 't', '2005-04-25 17:19:47', 'SYSTEM', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, 'ASSET_STORAGE')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."warehouse" ("id", "code", "name", "address", "active", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "warehouse_type_id", "warehouse_type_page") VALUES ('4191a437-52e7-48b9-86bc-9c246e71fe56', 'KHO_THANH_PHAM', 'Kho thành phẩm', NULL, 't', '2024-12-09 08:15:41.683822', 'SYSTEM', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, 'FINISHED_PRODUCTS_STORAGE')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."warehouse" ("id", "code", "name", "address", "active", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "warehouse_type_id", "warehouse_type_page") VALUES ('ca78313e-91c9-cd35-b988-9cfa968952ac', 'KHO_BAN_THANH_PHAM', 'Kho bán thành phẩm', NULL, 't', '2009-02-24 03:27:37', 'SYSTEM', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, 'SEMI_FINISHED_PRODUCTS_STORAGE')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."warehouse" ("id", "code", "name", "address", "active", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "warehouse_type_id", "warehouse_type_page") VALUES ('12e7dea0-0011-44a0-8b2e-4d8504c06f34', 'KHO_DONG_PHUC', 'Kho đồng phục', NULL, 't', '2009-02-24 03:27:37', 'SYSTEM', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, 'UNIFORM_WAREHOUSE')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."warehouse" ("id", "code", "name", "address", "active", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "warehouse_type_id", "warehouse_type_page") VALUES ('72b7a9f4-dca8-be65-3a4d-a171832a81fd', 'KHO_CCDC', 'Kho công cụ dụng cụ', NULL, 't', '2005-04-25 17:19:47', 'SYSTEM', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, 'TOOLS_AND_EQUIPMENT_STORAGE')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."warehouse" ("id", "code", "name", "address", "active", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "warehouse_type_id", "warehouse_type_page") VALUES ('ca78313e-91c9-cd35-b988-9cfa968959ac', 'KHO_HANG_HOA', 'Kho thương mại', '111', 't', '2009-02-24 03:27:37', 'SYSTEM', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, 'FINISHED_PRODUCTS_STORAGE')  ON CONFLICT ("id") DO NOTHING;



-- ////////////// Supplier //////////////
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id") VALUES ('b5d24164-ac46-4ac1-bbb6-63d18e6a18b5', 'NCC_BOT', 'Nhà cung câp bột', 'hikarikono@icloud.com', '868 East Alley', '614-537-1360', '5SkHo9ENA2', '2008-03-30 15:00:40', '8ZHeAVDSIe', '2007-04-10 03:07:49', '2023-07-30', NULL, NULL, 'KIM_LONG', '31296dff-ddba-42c8-bc19-9ab7110e312c', 'BIDV', 'dF0sZL7Mg8', 'ABC', '2022-07-11 09:46:51', 'Kono Hikari', 'm89X7ZDfsf', 't', '868 East Alley', '{"key5": 857, "key102": 6, "key1144": 7, "key9034": 9209}', '2014-02-24', 603, NULL)ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id") VALUES ('e112d293-4fad-4ae1-890e-b941b5a753a4', 'NCC_TIEN', 'Nhà cung câp tiền', 'zitaojiang@hotmail.com', '238 Riverview Road', '330-361-5165', 'IDLfu63gLy', '2011-01-05 02:44:02', '19f9tnkK0S', '2018-04-20 11:18:22', '2014-04-12', NULL, NULL, 'KIM_LONG', 'f3523bd1-0785-4773-bd6e-8a7085e3b14f', 'BIDV', 'jOlXEA0TDh', 'ABC', '2005-05-25 22:27:15', 'Jiang Zitao', 'l1IWN6v6UB', 't', '238 Riverview Road', '{"key31": 30}', '2023-01-20', 768, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id") VALUES ('15674db3-80e8-4c6a-becf-b94480c6391c', 'NCC_HANG_HOA', 'Nhà cung câp hàng hóa', 'ikki7@gmail.com', '6-1-19, Miyanomori 4 Jō, Chuo Ward', '80-5429-3204', 'F2gN91kDrB', '2007-10-30 17:27:42', 'vIX14eQIPQ', '2012-08-20 00:19:36', '2009-06-18', NULL, NULL, 'KIM_LONG', 'a80dbe83-a5ab-4eec-8bd1-1042f1543325', 'BIDV', 'Zh2j6W9Km0', 'ABC', '2019-12-05 03:38:52', 'Mori Ikki', 'DsTfxTBKmx', 't', '6-1-19, Miyanomori 4 Jō, Chuo Ward', '{"key5": 30, "key8": 526, "key79": 6865, "key80": 88, "key1501": 91}', '2009-07-09', 60, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id") VALUES ('fa2baac8-16d8-4878-a887-f23035970c76', 'NCC_BOT', 'Nhà cung câp bột', 'ellisjuli8@gmail.com', '3-19-18 Shimizu, Kita Ward', '80-9263-9238', 'WJT3nn4c9l', '2017-10-19 13:03:59', 'BKJwXoIcYE', '2019-05-29 04:15:43', '2000-07-31', NULL, NULL, 'MMS', '20ade21e-ea31-4824-912a-c37d012622a6', 'BIDV', 'yiAVx49vM0', 'ABC', '2012-03-06 14:14:18', 'Julie Ellis', 'ASjU22AKTJ', 't', '3-19-18 Shimizu, Kita Ward', '{"key1": 8943, "key8": 311, "key35": 2, "key89": 8438, "key138": 1}', '2017-09-24', 0, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id") VALUES ('559f476f-f119-460f-acbd-0c6ecff44ace', 'NCC_TIEN', 'Nhà cung câp tiền', 'chiyuent3@yahoo.com', '15 Fifth Avenue', '212-918-5916', 'sBthokDjyJ', '2015-06-01 12:06:20', 'FgpGduqmfA', '2023-01-05 22:49:20', '2024-10-13', NULL, NULL, 'MMS', 'd41579a3-0e5e-42d3-bc87-32db858134a5', 'BIDV', '1BHoITMGRt', 'ABC', '2006-03-24 18:30:10', 'Tsui Chi Yuen', '05mDqQrORC', 't', '15 Fifth Avenue', '{"key7": 311, "key118": 1837, "key864": 2, "key936": 6891, "key9216": 6}', '2016-11-02', 701, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id") VALUES ('d0d5b2f1-007a-4285-bec5-3c3aef78d8d8', 'NCC_HANG_HOA', 'Nhà cung câp hàng hóa', 'zitaopeng@gmail.com', '13-3-7 Toyohira 3 Jo, Toyohira Ward', '90-1925-3199', 'Fm8dEPXsct', '2024-08-20 15:56:39', '0ereRAGwhs', '2017-10-01 08:09:28', '2019-08-06', NULL, NULL, 'MMS', 'aa9f35f7-31e9-4123-afe8-9c0252263713', 'BIDV', 'CL9cu5bji7', 'ABC', '2021-12-20 04:34:36', 'Peng Zitao', 'fizrhzf2y0', 't', '13-3-7 Toyohira 3 Jo, Toyohira Ward', '{"key53": 39, "key83": 3392, "key2528": 1}', '2020-07-06', 18, NULL) ON CONFLICT ("id") DO NOTHING;

-- ////////////// Supplier Contract //////////////
INSERT INTO "public"."supplier_contract" ("id", "contract_code", "contract_name", "supplier_id", "contract_date", "end_date", "note", "attachments", "status", "company", "department", "created_by", "created_at", "updated_by", "updated_at", "deleted_by", "deleted_at", "contract_amount") VALUES ('210e6994-3890-ac47-a349-b3076400e37d', 'HD_001', 'Hợp đồng 001', 'b5d24164-ac46-4ac1-bbb6-63d18e6a18b5', '2002-07-16', '2015-05-23', 'BWPK6z6yi4', '{"key20": 76, "key85": 390, "key399": 606, "key429": 5, "key2660": 2, "key6774": 135}', 'NEW', 'KIM_LONG', NULL, 'System', '2022-11-22 17:36:30', '2014-03-03', NULL, NULL, '2009-11-04 09:28:49', '148.19')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_contract" ("id", "contract_code", "contract_name", "supplier_id", "contract_date", "end_date", "note", "attachments", "status", "company", "department", "created_by", "created_at", "updated_by", "updated_at", "deleted_by", "deleted_at", "contract_amount") VALUES ('dffcff5f-575a-bda7-3943-f31839a3a0b4', 'HD_002', 'Hợp đồng 002', 'e112d293-4fad-4ae1-890e-b941b5a753a4', '2007-03-29', '2011-10-17', 'lhx8NcfRIc', '{"key2": 7814, "key8955": 6794}', 'NEW', 'KIM_LONG', NULL, 'System', '2014-11-09 20:35:01', '2004-10-01', NULL, NULL, '2018-06-14 10:57:07', '756.35')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_contract" ("id", "contract_code", "contract_name", "supplier_id", "contract_date", "end_date", "note", "attachments", "status", "company", "department", "created_by", "created_at", "updated_by", "updated_at", "deleted_by", "deleted_at", "contract_amount") VALUES ('eb27e3de-f7c5-e882-c790-a26661291455', 'HD_003', 'Hợp đồng 003', 'e112d293-4fad-4ae1-890e-b941b5a753a4', '2015-05-02', '2000-03-10', 'VCbu9ggLWF', '{"key278": 13, "key611": 4, "key7642": 1455, "key9042": 95}', 'NEW', 'KIM_LONG', NULL, 'System', '2014-08-17 23:41:05', '2006-10-24', NULL, NULL, '2010-05-05 05:25:17', '79.50')  ON CONFLICT ("id") DO NOTHING;

-- ////////////// document_code_sequence //////////////
INSERT INTO "public"."document_code_sequence" ("id", "company", "current_sequence", "java_format", "document_type") VALUES ('165778d7-0615-4aa6-bf4c-25cc110ee351', 'KIM_LONG', 1, 'PK%04d', 'inventories')  ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."document_code_sequence" ("id", "company", "current_sequence", "java_format", "document_type") VALUES ('0c8a334c-1c4c-4107-a436-bdcdf03c62c7', 'MMS', 1, 'PK%04d', 'inventories')  ON CONFLICT ("id") DO NOTHING;


-- ////////////// Inventories Check //////////////
INSERT INTO "public"."inventories_check" ("id", "code", "check_date", "warehouse", "note", "approver_1", "approver_2", "approver_3", "status", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute") VALUES ('896c9ec1-48c8-fe5a-bfe1-8797b2b92bd5', 'ABC-1', '2003-02-11 01:01:21', '72b7a9f4-dca8-be65-3a4d-a171832a85fd', 'abc', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', 'NEW', 'f', NULL, 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_check" ("id", "code", "check_date", "warehouse", "note", "approver_1", "approver_2", "approver_3", "status", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute") VALUES ('52fd8409-d6f5-6672-17a4-72e38a9a17c1', 'ABC-2', '2008-07-08 18:24:12', '72b7a9f4-dca8-be65-3a4d-a171832a85fd', 'abc', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', 'NEW', 'f', NULL, 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_check" ("id", "code", "check_date", "warehouse", "note", "approver_1", "approver_2", "approver_3", "status", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute") VALUES ('944b9cb6-f69b-4473-8d5f-667ca7a0b542', 'ABC-3', '2015-02-07 23:16:40', '72b7a9f4-dca8-be65-3a4d-a171832a85fd', 'abc', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', 'NEW', 'f', NULL, 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_check" ("id", "code", "check_date", "warehouse", "note", "approver_1", "approver_2", "approver_3", "status", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute") VALUES ('7bc73ae0-d56e-a2dd-450b-a3f9490dfdba', 'ABC-4', '2008-02-08 02:51:01', '72b7a9f4-dca8-be65-3a4d-a171832a85fd', 'abc', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', 'NEW', 'f', NULL, 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_check" ("id", "code", "check_date", "warehouse", "note", "approver_1", "approver_2", "approver_3", "status", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute") VALUES ('e982297b-d051-ce9b-5990-db34f62cba03', 'ABC-5', '2004-03-10 08:41:05', '72b7a9f4-dca8-be65-3a4d-a171832a85fd', 'abc', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', '999e1416-a92a-459a-add7-611278b61b35', 'NEW', 'f', NULL, 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL) ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."inventories_check_detail" ("id", "inventories_check_id", "code", "item_id", "system_quantity", "actual_quantity", "note", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('ca5aa436-c14b-5983-25eb-f895fc8268f4', '896c9ec1-48c8-fe5a-bfe1-8797b2b92bd5', 'umYISSE7tf', '1c122a92-a08f-496e-ac9d-a1e11119f7f3', '822.13', '28.42', NULL, 'f', '2022-07-16 05:12:35', NULL, NULL, NULL, NULL, NULL, 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_check_detail" ("id", "inventories_check_id", "code", "item_id", "system_quantity", "actual_quantity", "note", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('369c5a4e-23ce-5ba6-2e4d-e20fa4d3512d', '896c9ec1-48c8-fe5a-bfe1-8797b2b92bd5', 'pwxYAaOfNd', '1c122a92-a08f-496e-ac9d-a1e11119f7f3', '602.28', '506.82', NULL, 'f', '2024-02-17 01:40:46', NULL, NULL, NULL, NULL, NULL, 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_check_detail" ("id", "inventories_check_id", "code", "item_id", "system_quantity", "actual_quantity", "note", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('1e163176-071a-8ec5-a1c2-395185401a8f', '896c9ec1-48c8-fe5a-bfe1-8797b2b92bd5', 'Qr0SLhyj16', '1c122a92-a08f-496e-ac9d-a1e11119f7f3', '372.72', '517.47', NULL, 'f', '2018-11-05 04:29:37', NULL, NULL, NULL, NULL, NULL, 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;


-- ////////////// Add Data Sequence //////////////

INSERT INTO "public"."document_code_sequence" ("id", "company", "current_sequence", "java_format", "document_type") VALUES ('6323ad11-a4ad-42ca-bad5-04b5d788ef1b', 'KIM_LONG', 1, 'Date%04d', 'inventories_check') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."document_code_sequence" ("id", "company", "current_sequence", "java_format", "document_type") VALUES ('5978c8e7-d96c-4ebb-86f2-48a1ffe77466', 'MMS', 1, 'Date%04d', 'inventories_check') ON CONFLICT ("id") DO NOTHING;


-- ////////////// Add Data Sequence //////////////
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('d1a485cb-bee5-4e7d-96c8-df23d8d0e19a', 'HANG_HOA', 'Hàng Hóa', NULL, 't', 'f', '2007-02-21 15:46:11', 'System', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('fc1e9c5a-5d0e-7b85-f5e3-908b5290ffd1', 'TAI_SAN', 'Tài sản', NULL, 't', 'f', '2015-07-13 21:16:58', 'System', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('c8d2e022-8d87-0d0f-7aa6-99d9c411f6fb', 'CCDC', 'Công cụ dụng cụ', NULL, 't', 'f', '2015-05-26 05:10:58', 'System', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('2812eee7-6e52-0783-41b2-f56812b78913', 'THANH_PHAM', 'Thành Phẩm', NULL, 't', 'f', '2012-07-05 03:22:31', 'System', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('cd64ac67-484b-fc81-b8f1-afe12c0fadff', 'BAN_THANH_PHAM', 'Bán Thành Phẩm', NULL, 't', 'f', '2004-09-01 05:52:12', 'System', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('21a2c060-4293-ad8d-56d3-ec5b6b13165d', 'HANG_HOA', 'Hàng Hóa', NULL, 't', 'f', '2007-09-23 02:27:47', 'System', NULL, NULL, NULL, NULL, 'MMS', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('ea4fd555-a88a-68ae-4912-b78281660c4a', 'TAI_SAN', 'Tài sản', NULL, 't', 'f', '2015-04-27 15:49:41', 'System', NULL, NULL, NULL, NULL, 'MMS', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('c168cb6c-a8a0-4413-385f-b284947d060f', 'CCDC', 'Công cụ dụng cụ', NULL, 't', 'f', '2019-04-20 15:45:30', 'System', NULL, NULL, NULL, NULL, 'MMS', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('0855d2db-3a5b-2767-e40f-f46ff0346b65', 'THANH_PHAM', 'Thành Phẩm', NULL, 't', 'f', '2023-03-02 04:55:15', 'System', NULL, NULL, NULL, NULL, 'MMS', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."item_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "item_type") VALUES ('a77590ca-8550-8302-4115-386c19a3e516', 'BAN_THANH_PHAM', 'Bán Thành Phẩm', NULL, 't', 'f', '2006-12-01 10:39:01', 'System', NULL, NULL, NULL, NULL, 'MMS', NULL, NULL, 'ITEM') ON CONFLICT ("id") DO NOTHING;

-- ////////////// Add Data supplier_group //////////////
INSERT INTO "public"."supplier_group" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company") VALUES ('bd08ec1f-b7ea-393e-8f15-31273516dee7', 'Nhà cung cấp bột', '2008-08-31 18:48:10', 'system', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_group" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company") VALUES ('9488ffe3-398a-0dce-65d1-c0b5a3c17c05', 'Nhà cung cấp cám', '2017-07-20 02:29:52', 'system', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_group" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company") VALUES ('180156d1-aa32-751d-ab81-86ac4ffdeecd', 'Nhà cung cấp tiền', '2019-06-30 13:42:58', 'system', NULL, NULL, NULL, NULL, 'KIM_LONG') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_group" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company") VALUES ('6e84f94e-0684-483b-89b1-997e1555eba2', 'Nhà cung cấp bột', '2008-08-31 18:48:10', 'system', NULL, NULL, NULL, NULL, 'MMS') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_group" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company") VALUES ('d0cbe845-b528-46c7-9da2-74924716bb44', 'Nhà cung cấp cám', '2017-07-20 02:29:52', 'system', NULL, NULL, NULL, NULL, 'MMS') ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_group" ("id", "name", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company") VALUES ('cc96e666-d885-436e-89a8-095f8cbe2c67', 'Nhà cung cấp tiền', '2019-06-30 13:42:58', 'system', NULL, NULL, NULL, NULL, 'MMS') ON CONFLICT ("id") DO NOTHING;

-- ////////////// Add Data supplier_type //////////////
INSERT INTO "public"."supplier_type" ("id", "code", "name", "description", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('6acb23f1-ae52-5c40-941a-68998541834b', 'Nhà cung cấp bột', 'ABC-1', NULL, 'f', '2024-11-27 00:26:19', 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_type" ("id", "code", "name", "description", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('00518c61-9062-a5fd-be70-3a9ef0099e00', 'Nhà cung cấp cám', 'ABC-2', NULL, 'f', '2013-07-01 09:27:51', 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_type" ("id", "code", "name", "description", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('a7485330-e741-f9ee-67db-ba2935e900f0', 'Nhà cung cấp tiền', 'ABC-3', NULL, 'f', '2014-09-26 17:24:30', 'system', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_type" ("id", "code", "name", "description", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('974ee4f5-25e6-4824-a6e6-82f9f6ea77e7', 'Nhà cung cấp bột', 'ABC-1', NULL, 'f', '2024-11-27 00:26:19', 'system', NULL, NULL, NULL, NULL, 'MMS', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_type" ("id", "code", "name", "description", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('71148d44-0f0b-4d2c-b0ba-50327e3ca60b', 'Nhà cung cấp cám', 'ABC-2', NULL, 'f', '2013-07-01 09:27:51', 'system', NULL, NULL, NULL, NULL, 'MMS', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplier_type" ("id", "code", "name", "description", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('96f27dc3-41f9-4a10-b70f-8e8ff190f611', 'Nhà cung cấp tiền', 'ABC-3', NULL, 'f', '2014-09-26 17:24:30', 'system', NULL, NULL, NULL, NULL, 'MMS', NULL) ON CONFLICT ("id") DO NOTHING;


-- ////////////// Add Data supplier //////////////
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id", "full_name", "position", "attachment", "payment_term_text", "supplier_type_id") VALUES ('f165cb19-271f-4052-b20f-adc16a57d6bc', 'NCC_NOI_BO', 'Nội bộ công ty', NULL, NULL, NULL, NULL, '2024-08-20 15:56:39', 'SYSTEM', '2017-10-01 08:09:28', '2019-08-06', NULL, NULL, 'MMS', 'bd08ec1f-b7ea-393e-8f15-31273516dee7', NULL, NULL, NULL, NULL, NULL, NULL, 't', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."suppliers" ("id", "code", "name", "email", "address", "phone", "note", "create_at", "create_by", "update_at", "update_by", "delete_at", "delete_by", "company", "supplier_group_id", "bank_info", "tax_code", "contact", "payment_term", "short_name", "fax", "is_active", "address_service", "debt_employees", "birthday", "payment_term_number", "manager_id", "full_name", "position", "attachment", "payment_term_text", "supplier_type_id") VALUES ('ac2a78a2-0648-4e72-af9d-5558e61c2acc', 'NCC_NOI_BO', 'Nội bộ công ty', NULL, NULL, NULL, NULL, '2024-08-20 15:56:39', 'SYSTEM', '2017-10-01 08:09:28', '2019-08-06', NULL, NULL, 'KIM_LONG', 'bd08ec1f-b7ea-393e-8f15-31273516dee7', NULL, NULL, NULL, NULL, NULL, NULL, 't', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL) ON CONFLICT ("id") DO NOTHING;

-- ////////////// Add Data item //////////////
-- INSERT INTO "public"."item" ("id", "code", "name", "uom_id", "attribute", "company", "department", "is_deleted", "created_by", "created_date", "updated_by", "updated_at", "deleted_by", "deleted_at", "item_category_id", "vat_rate", "unit_price", "revenue_group", "supplier_id", "item_type", "is_active", "vat_id", "item_type_id", "percent_protein", "notes", "is_separation") VALUES ('766a94fc-cdff-429c-b653-c238296f8967', 'BAN_THANH_PHAM', 'Bán Thành Phẩm', 'eab9dd9f-54d6-4b91-9fc0-e1aaff641d50', '{"origin": "Việt nam", "nameEng": "Bán Thành Phẩm"}', 'MMS', NULL, 'f', 'SYSTEM_BAN_THANH_PHAM', '2024-12-10 08:49:39.569235', NULL, NULL, NULL, NULL, 'de80e071-8409-48d2-b864-f489048d074d', '0', '0', NULL, 'f165cb19-271f-4052-b20f-adc16a57d6bc', 'ITEM', 't', NULL, 'cd64ac67-484b-fc81-b8f1-afe12c0fadff', NULL, NULL, 'f') ON CONFLICT ("id") DO NOTHING;
-- INSERT INTO "public"."item" ("id", "code", "name", "uom_id", "attribute", "company", "department", "is_deleted", "created_by", "created_date", "updated_by", "updated_at", "deleted_by", "deleted_at", "item_category_id", "vat_rate", "unit_price", "revenue_group", "supplier_id", "item_type", "is_active", "vat_id", "item_type_id", "percent_protein", "notes", "is_separation") VALUES ('d3a01bbb-be51-46c3-896c-9896d51d549f', 'BAN_THANH_PHAM', 'Bán Thành Phẩm', 'eab9dd9f-54d6-4b91-9fc0-e1aaff641d50', '{"origin": "Việt nam", "nameEng": "Bán Thành Phẩm"}', 'KIM_LONG', NULL, 'f', 'SYSTEM_BAN_THANH_PHAM', '2024-12-10 08:49:39.569235', NULL, NULL, NULL, NULL, 'de80e071-8409-48d2-b864-f489048d074d', '0', '0', NULL, 'ac2a78a2-0648-4e72-af9d-5558e61c2acc', 'ITEM', 't', NULL, 'cd64ac67-484b-fc81-b8f1-afe12c0fadff', NULL, NULL, 'f') ON CONFLICT ("id") DO NOTHING;
-- INSERT INTO "public"."item" ("id", "code", "name", "uom_id", "attribute", "company", "department", "is_deleted", "created_by", "created_date", "updated_by", "updated_at", "deleted_by", "deleted_at", "item_category_id", "vat_rate", "unit_price", "revenue_group", "supplier_id", "item_type", "is_active", "vat_id", "item_type_id", "percent_protein", "notes", "is_separation") VALUES ('228f877e-f73d-4f1b-ba46-3bc57b69f21d', 'THANH_PHAM', 'Thành Phẩm', 'eab9dd9f-54d6-4b91-9fc0-e1aaff641d50', '{"origin": "Việt nam", "nameEng": "Bán Thành Phẩm"}', 'MMS', NULL, 'f', 'SYSTEM_THANH_PHAM', '2024-12-10 08:49:39.569235', NULL, NULL, NULL, NULL, 'de80e071-8409-48d2-b864-f489048d074d', '0', '0', NULL, 'f165cb19-271f-4052-b20f-adc16a57d6bc', 'ITEM', 't', NULL, 'cd64ac67-484b-fc81-b8f1-afe12c0fadff', NULL, NULL, 'f') ON CONFLICT ("id") DO NOTHING;
-- INSERT INTO "public"."item" ("id", "code", "name", "uom_id", "attribute", "company", "department", "is_deleted", "created_by", "created_date", "updated_by", "updated_at", "deleted_by", "deleted_at", "item_category_id", "vat_rate", "unit_price", "revenue_group", "supplier_id", "item_type", "is_active", "vat_id", "item_type_id", "percent_protein", "notes", "is_separation") VALUES ('296de1d3-de0f-4e9a-bd42-56509d454c2e', 'THANH_PHAM', 'Thành Phẩm', 'eab9dd9f-54d6-4b91-9fc0-e1aaff641d50', '{"origin": "Việt nam", "nameEng": "Bán Thành Phẩm"}', 'KIM_LONG', NULL, 'f', 'SYSTEM_THANH_PHAM', '2024-12-10 08:49:39.569235', NULL, NULL, NULL, NULL, 'de80e071-8409-48d2-b864-f489048d074d', '0', '0', NULL, 'ac2a78a2-0648-4e72-af9d-5558e61c2acc', 'ITEM', 't', NULL, 'cd64ac67-484b-fc81-b8f1-afe12c0fadff', NULL, NULL, 'f') ON CONFLICT ("id") DO NOTHING;

--
INSERT INTO "public"."inventories_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('92fa0931-c2f7-4a13-8485-a28714436d96', 'NHAP_SAN_XUAT', 'Nhập sản xuất', ' ', 't', 'f', '2021-07-14 06:10:52', 'SYSTEM', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."inventories_type" ("id", "code", "name", "description", "is_active", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department") VALUES ('1d023fda-b505-435c-871a-c0507a49df40', 'NHAP_SAN_XUAT', 'Nhập sản xuất', ' ', 't', 'f', '2021-07-14 06:10:52', 'SYSTEM', NULL, NULL, NULL, NULL, 'MMS', NULL) ON CONFLICT ("id") DO NOTHING;


INSERT INTO "public"."supplies_request_type" ("id", "code", "name", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "note") VALUES ('cdb8b1a1-227d-2f8f-6d8d-a3f74e6e124b', 'DX_MOI', 'Đề xuất mua mới', 'f', '2008-11-01 18:09:01', 'ADMIN', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplies_request_type" ("id", "code", "name", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "note") VALUES ('494ff186-e201-998d-e885-8d54e66c19f2', 'DX_BO_SUNG', 'Đề xuất bổ sung', 'f', '2007-02-12 08:28:44', 'ADMIN', NULL, NULL, NULL, NULL, 'KIM_LONG', NULL, NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplies_request_type" ("id", "code", "name", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "note") VALUES ('980826f7-8845-6d00-5d3e-5ef2c3014e71', 'DX_MOI', 'Đề xuất mua mới', 'f', '2013-05-30 05:25:52', 'ADMIN', NULL, NULL, NULL, NULL, 'MMS', NULL, NULL, NULL) ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."supplies_request_type" ("id", "code", "name", "is_deleted", "created_at", "created_by", "updated_at", "updated_by", "deleted_at", "deleted_by", "company", "department", "attribute", "note") VALUES ('81b1fa9e-b458-368e-5228-948274df3c2a', 'DX_BO_SUNG', 'Đề xuất bổ sung', 'f', '2020-06-27 19:48:23', 'ADMIN', NULL, NULL, NULL, NULL, 'MMS', NULL, NULL, NULL) ON CONFLICT ("id") DO NOTHING;
