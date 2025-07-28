import { AUTHORITIES_ACTIONS, AUTHORITIES_NAME } from "app/shared/model/enumerations/authorities-group";

export const action_label = {
  [AUTHORITIES_ACTIONS.CREATE]: 'Tạo mới',
  [AUTHORITIES_ACTIONS.CREATE_MANY]: 'Châm công hàng loạt',
  [AUTHORITIES_ACTIONS.DELETE]: 'Xóa',
  [AUTHORITIES_ACTIONS.EXPORT]: 'Xuất',
  [AUTHORITIES_ACTIONS.READ]: 'Truy cập',
  [AUTHORITIES_ACTIONS.REVIEW]: 'Xét duyệt',
  [AUTHORITIES_ACTIONS.UPDATE]: 'Cập nhật',
  [AUTHORITIES_ACTIONS.ADJOURN]: 'Gia hạn',
  [AUTHORITIES_ACTIONS.PROPOSE_APPROVE]: 'Đề xuất xét duyệt',
  [AUTHORITIES_ACTIONS.CANCEL]: 'Hủy',
  [AUTHORITIES_ACTIONS.MIX_CHECKING]: 'Kiểm tra trộn bột',
  [AUTHORITIES_ACTIONS.START]: 'Bắt đầu',
}

export const permission_group = {
  undefined: 'Khác',

  // ---------------
  [AUTHORITIES_NAME.TIME_KEEPING]: 'Chấm công',
  [AUTHORITIES_NAME.TIME_KEEPING_VIOLATION]: 'Chấm công',
  [AUTHORITIES_NAME.TIME_KEEPING_EXPLANATION]: 'Chấm công',
  [AUTHORITIES_NAME.PERSONAL_MONTHLY_TIMESHEET]: 'Chấm công',

  // ---------------
  [AUTHORITIES_NAME.LEAVE_REQUEST]: 'Nghỉ phép',

  // ---------------
  [AUTHORITIES_NAME.MANUFACTURER_ORDER]: 'Sản xuất',
  [AUTHORITIES_NAME.WORK_ORDER]: 'Sản xuất',
  [AUTHORITIES_NAME.PRODUCT_PACKAGE]: 'Sản xuất',
  [AUTHORITIES_NAME.QUANLITY_CHECK_SAMPLE]: 'Sản xuất',
  [AUTHORITIES_NAME.PRODUCT_MAINTENANCE]: 'Sản xuất',
  [AUTHORITIES_NAME.PRODUCT_ROUTING]: 'Sản xuất',
  [AUTHORITIES_NAME.PRODUCT_STANDARD]: 'Sản xuất',
  [AUTHORITIES_NAME.WORK_CENTER]: 'Sản xuất',

  // ---------------
  [AUTHORITIES_NAME.CUSTOMER]: 'Kinh doanh',
  // Danh sách vô hiệu hóa
  [AUTHORITIES_NAME.QUOTATIONS]: 'Kinh doanh',
  [AUTHORITIES_NAME.CONTRACTS]: 'Kinh doanh',
  // Danh sách hợp đồng đã xóa
  [AUTHORITIES_NAME.ORDER]: 'Kinh doanh',

  // ---------------
  [AUTHORITIES_NAME.EMPLOYEE]: 'HCNS',
  [AUTHORITIES_NAME.WORKSPACE]: 'HCNS',
  [AUTHORITIES_NAME.LEAVE_REGISTER]: 'HCNS',
  [AUTHORITIES_NAME.RECRUITMENT]: 'HCNS',
  // Danh sách ứng viên
  // Công văn
  [AUTHORITIES_NAME.UNIFORM_ORDER_STOCK]: 'HCNS',
  [AUTHORITIES_NAME.UNIFORM_ORDER]: 'HCNS',
  [AUTHORITIES_NAME.UNIFORM_RELEASE]: 'HCNS',

  // ---------------
  [AUTHORITIES_NAME.SUPPLIES_REQUESTS]: 'Logistics',
  [AUTHORITIES_NAME.INCOMING_INVOICE]: 'Logistics',
  [AUTHORITIES_NAME.SUPPLIERS]: 'Logistics',
  [AUTHORITIES_NAME.SUPPLIERS_GROUPS]: 'Logistics',
  // Danh sách NCC
  // DNTT - DNTU - DNHU
  // Mã VT - CCDC

  // ---------------
  [AUTHORITIES_NAME.USERS_SETTING]: 'Cài đặt',
  [AUTHORITIES_NAME.ANNUAL_LEAVE]: 'Cài đặt',
  [AUTHORITIES_NAME.UOM_SETTING]: 'Cài đặt',
  [AUTHORITIES_NAME.ITEMS_SETTING]: 'Cài đặt',
  [AUTHORITIES_NAME.UNIFORM_SETTING]: 'Cài đặt',
  // Danh sách kho
  // Loại kho
}

export const permission_label = {
  [AUTHORITIES_NAME.EMPLOYEE]: 'Danh sách nhân viên',
  [AUTHORITIES_NAME.ANNUAL_LEAVE]: 'Phép năm',
  [AUTHORITIES_NAME.INTERVIEW_SCHEDULE]: 'Lên lịch phỏng vấn',
  [AUTHORITIES_NAME.UNIFORM_ORDER]: 'Đơn mua đồng phục',
  [AUTHORITIES_NAME.UNIFORM_ORDER_STOCK]: 'Danh sách đồng phục',
  [AUTHORITIES_NAME.UNIFORM_RELEASE]: 'Quản lý kho',
  [AUTHORITIES_NAME.REPORT]: 'Xem báo cáo',
  [AUTHORITIES_NAME.PERMISION]: 'Quyền',
  [AUTHORITIES_NAME.WORKSPACE]: 'Văn phòng/Nhà máy',
  [AUTHORITIES_NAME.TIME_KEEPING]: 'Chấm công',
  [AUTHORITIES_NAME.PERSONAL_MONTHLY_TIMESHEET]: 'Bảng chấm công tháng',
  [AUTHORITIES_NAME.TIME_KEEPING_VIOLATION]: 'Vi phạm chấm công',
  [AUTHORITIES_NAME.EXPLANATION_REVIEW]: 'Giải trình chấm công',
  [AUTHORITIES_NAME.TIME_KEEPING_EXPLANATION]: 'Giải trình chấm công',
  [AUTHORITIES_NAME.LEAVE_REQUEST]: 'Danh sách đơn nghỉ phép',
  [AUTHORITIES_NAME.CUSTOMER]: 'Danh sách khách hàng',
  [AUTHORITIES_NAME.PURCHASE_REQUEST]: 'Đề nghị thu mua',
  [AUTHORITIES_NAME.ACCOUNT]: 'Tài khoản',
  [AUTHORITIES_NAME.WORK_CENTER]: 'Quản lý cụm máy sản xuất',
  [AUTHORITIES_NAME.PRODUCT_STANDARD]: 'Kế hoạch định mức',
  [AUTHORITIES_NAME.PRODUCT_ROUTING]: 'Quản lý tuyến kho sản phẩm',
  [AUTHORITIES_NAME.PRODUCT_MAINTENANCE]: 'Quản lý bảo trì sản phẩm',
  [AUTHORITIES_NAME.SAMPLE_DISPOSAL]: 'Đơn huỷ mẫu kiểm thử',
  [AUTHORITIES_NAME.QUANLITY_CHECK_SAMPLE]: 'Quản lí kiểm tra chất lượng',
  [AUTHORITIES_NAME.PRODUCT_PACKAGE]: 'Quản lí đóng gói',
  [AUTHORITIES_NAME.WORK_ORDER]: 'Quản lí công đoạn sản xuất',
  [AUTHORITIES_NAME.MANUFACTURER_ORDER]: 'Quản lí lệnh',
  [AUTHORITIES_NAME.INCOMING_INVOICE]: 'Hóa đơn đầu vào',
  [AUTHORITIES_NAME.SUPPLIERS_REQUESTS]: 'Đề nghị thu mua',
  [AUTHORITIES_NAME.SUPPLIERS]: 'Danh sách NCC',
  [AUTHORITIES_NAME.SUPPLIERS_GROUPS]: 'Nhóm nhà cung cấp',
  [AUTHORITIES_NAME.ANNUAL_LEAVE_SETTING]: 'Cài đặt nghỉ phép',
  [AUTHORITIES_NAME.ITEMS_SETTING]: 'Danh sách vật phẩm',
  [AUTHORITIES_NAME.UNIFORM_SETTING]: 'Đồng phục',
  [AUTHORITIES_NAME.UOM_SETTING]: 'Quản lý đơn vị',
  [AUTHORITIES_NAME.USERS_SETTING]: 'Người dùng',
  [AUTHORITIES_NAME.RECRUITMENT]: 'Yêu cầu tuyển dụng',
  [AUTHORITIES_NAME.LEAVE_REGISTER]: 'Đăng ký nghỉ chế độ',
  [AUTHORITIES_NAME.SUPPLIES_REQUESTS]: 'Đề xuất mua hàng',
  [AUTHORITIES_NAME.ORDERS]: 'Đơn đặt hàng',
  [AUTHORITIES_NAME.CONTRACTS]: 'Danh sách tất cả hợp đồng',
  [AUTHORITIES_NAME.QUOTATIONS]: 'Bảng báo giá',
  // Công văn
  // Danh sách NCC
  // DNTT - DNTU - DNHU
  // Mã VT - CCDC
  // Danh sách kho
  // Loại kho
}
