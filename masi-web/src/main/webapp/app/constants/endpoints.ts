import { GROUP_ACTION } from 'app/shared/model/enumerations/group.enum';
import { PAYMENT_REQUEST_TYPE } from 'app/shared/model/enumerations/payment-request';

const GROUPS = '/services/masierp/api/groups';
const ADMIN_USERS = '/services/masierp/api/admin/users';
const AUTHORITIES = '/services/masierp/api/authorities';
const FILE = '/services/masiutility/api/file-attachments';
const NOTIFICATION = '/services/masiutility/api/notification-recipients';
const TIME_KEEPING = '/services/masiemployee/api/time-keeping';
const TIME_KEEPING_CRON_JOB = '/services/masiemployee/api/cron-job';
const TIME_KEEPING_RECORD = '/services/masiemployee/api/time-keeping-record';
const TIME_KEEPING_VIOLATIONS =
  '/services/masiemployee/api/time-keeping-violations';
const TIME_KEEPING_EXPLANATIONS =
  '/services/masiemployee/api/time-keeping-explanations';
const TIME_KEEPING_EXPLANATION_REVIEWS =
  '/services/masiemployee/api/explanation-reviews';
const TIME_KEEPING_MONTHLY =
  '/services/masiemployee/api/personal-monthly-timesheets';
const TIME_KEEPING_MONTHLY_REVIEWS =
  '/services/masiemployee/api/monthly-time-sheet-reviews';
const LEAVE_REQUESTS = '/services/masiemployee/api/leave-request';
const LEAVE_REQUESTS_REVIEWS =
  '/services/masiemployee/api/leave-request-reviews';
const LEAVE_TRACKING_EXPORT =
  '/services/masiemployee/api/time-keeping/leave-tracking/export';
const EMPLOYEES = '/services/masiemployee/api/employees';
const PRODUCTION_STANDARDS =
  '/services/masiproduction/api/production-standards';
const PRODUCTION_STANDARDS_MO =
  '/services/masiproduction/api/production-standards/full';
const PRODUCTION_COMMANDS = '/services/masiproduction/api/manufacture-orders';
const PRODUCTION_PROCESS = '/services/masiproduction/api/work-orders';
const PRODUCTION_QUALITY_CONTROL =
  '/services/masiproduction/api/quality-check-samples';
const PRODUCTION_QUALITY_DISPOSAL =
  '/services/masiproduction/api/sample-disposals';
const PRODUCTION_WORK_CENTER = '/services/masiproduction/api/work-centers';
const PRODUCTION_ROUTINGS = '/services/masiproduction/api/product-routings';
const PRODUCTION_MAINTAINS = '/services/masiproduction/api/product-maintains';
const PRODUCTION_PACKAGES = '/services/masiproduction/api/product-packages';
const RECEIVE_MATERIAL_CHECKLIST =
  '/services/masiproduction/api/receive-material-checklists';
const STEAMING_PROCESS_CHECKLIST =
  '/services/masiproduction/api/steaming-process-checklists';
const ADDITIVE_MATERIAL_CHECKLIST =
  '/services/masiproduction/api/additive-material-checklists';
const METAL_DETECTION_CHECKLIST =
  '/services/masiproduction/api/metal-detection-checklists';
const MIXING_REPORT_CHECKLIST =
  '/services/masiproduction/api/mixing-report-checklists';
const MACHINE_OPERATION_CHECKLIST =
  '/services/masiproduction/api/machine-operation-checklists';
const FACTORIES = '/services/masiproduction/api/factories';
const STORAGES = '/services/masiproduction/api/storages';
const CUSTOMERS = '/services/masisale/api/customers';
const CONTRACTS = '/services/masisale/api/contracts';
const ORDERS = '/services/masisale/api/orders';
const ORDERS_REVIEWS = '/services/masisale/api/order-reviews';
const PURCHASE = '/services/masisale/api/purchase-requests';
const PURCHASE_DELIVERY = '/services/masisale/api/purchase-deliveries';
const PURCHASE_REVIEW = '/services/masisale/api/purchase-reviews';
const PURCHASE_FILE = '/services/masisale/api/purchase-request-files';
const QUOTATIONS = '/services/masisale/api/quotations';
const QUOTATIONDETAILS = '/services/masisale/api/quotation-details';
const EMPLOYEE_PROFILE = '/services/masiemployee/api/employee-profiles';
const EMPLOYEE_ID_SEQUENCES =
  '/services/masiemployee/api/employee-id-sequences';
const EMPLOYEE_PROFILE_ATTACHMENT =
  '/services/masiemployee/api/profile-attachments/profile';
const EMPLOYEE_CHANGE_LOG = '/services/masiemployee/api/employee-change-logs';
const CONFIRM_LEAVE = '/services/masiemployee/api/confirm-leaves';
const WORKSPACE = '/services/masiemployee/api/workspaces';
const RECRUITMENT = '/services/masiemployee/api/recruitment-requests';
const RECRUITMENT_INTERVIEWS = '/services/masiemployee/api/interview-schedule';
const ANNUAL_LEAVE = '/services/masiemployee/api/annual-leaves';
const LEAVE_REGIME = '/services/masiemployee/api/leave-regime-requests';
const PROCESS_LEAVE_REGIME =
  '/services/masiemployee/api/process-leave-regime-requests';
const DOCUMENTARY = '/services/masiemployee/api/documentaries';
const REPORT = '/services/masiemployee/api/reports';
const UNIFORM = '/services/masiemployee/api/uniforms';
const UNIFORM_ORDERS = '/services/masiemployee/api/uniform-orders';
const UNIFORM_ORDERS_STOCKS = '/services/masiemployee/api/uniform-order-stocks';
const UNIFORM_RELEASES = '/services/masiemployee/api/uniform-releases';
const UNIFORM_STOCKS = '/services/masiemployee/api/uniform-stocks';
const UNIFORM_RETURN = '/services/masiemployee/api/uniform-returns';
const UOM = '/services/masilogistics/api/uoms';
const UOM_GROUP = '/services/masilogistics/api/uom-groups';
const SUPPLIERS = '/services/masilogistics/api/suppliers';
const SUPPLIER_TYPE = '/services/masilogistics/api/supplier-types';
const WAREHOUSES = '/services/masilogistics/api/warehouses';
const WAREHOUSE_TYPES = '/services/masilogistics/api/warehouse-types';
const INVENTORIES = '/services/masilogistics/api/inventories';
const INCOMING_INVOICES = '/services/masilogistics/api/incoming-invoices';
const ITEMS = '/services/masilogistics/api/items';
const ITEM_CATEGORIES = '/services/masilogistics/api/item-categories';
const ITEM_TYPES = '/services/masilogistics/api/item-types';
const SUPPLIES_REQUESTS = '/services/masilogistics/api/supplies-requests';
const REGISTER = '/services/masierp/api/register';
const ACCOUNTS = '/services/masierp/api/account';
const USERS = '/services/masierp/api/admin/users';
const TYPE_REQUEST_APPROVAL =
  'services/masiemployee/api/type-request-approval-pages';
const CONFIG = '/services/masiutility/api/configs';
const PAYMENT_REQUESTS = '/services/masilogistics/api/payment-requests';
const SUPPLIER_GROUPS = '/services/masilogistics/api/supplier-groups';
const CONTACT_TYPE = '/services/masilogistics/api/contact-types';
const VAT_RATES = '/services/masilogistics/api/vat-rates';
const FACTORIES_LOGISTICS = '/services/masilogistics/api/factories';
const COMPANIES = '/services/masierp/api/companies';
const PAYMENT_DETAILS = '/services/masilogistics/api/payment-details';
const CURRENCIES = '/services/masilogistics/api/currencies';
const INVENTORIES_STORAGE = '/services/masilogistics/api/inventories';
const INVENTORIES_STORAGE_NEW =
  '/services/masilogistics/api/inventories-storages';
const INVENTORIES_TYPE = '/services/masilogistics/api/inventories-types';
const SUPPLIER_CONTRACTS = '/services/masilogistics/api/supplier-contracts';
const STOCKTAKING = '/services/masilogistics/api/inventories-checks';

const CALL_CENTER = '/services/masiemployee/api/call-centers';
const TRANSFER_ASSETS = '/services/masilogistics/api/item-asset-transfers';
const TRANSACTION_TYPE = '/services/masilogistics/api/transaction-types';

export const configEndpoint = {
  // Lấy chi tiết hoặc tạo mới config
  getConfig: (key: string) => `${CONFIG}/${key}`,
};

export const fileEndpoints = {
  // Tạo File
  postFile: `${FILE}`,

  // Lấy File
  getFile: (id: string) => `${FILE}/${id}`,

  // Xóa File
  deleteFile: (id: string) => `${FILE}/${id}`,
};

export const notificationEndpoints = {
  // Lấy danh sách thông thông báo
  getMyNotifications: `${NOTIFICATION}/my-notifications`,

  // Đếm thông báo chưa đọc
  countUnreadNotifications: `${NOTIFICATION}/count-unread`,

  // Đánh dấu đã đọc thông dấu
  readNotification: (id: string) => `${NOTIFICATION}/${id}/read`,
};

export const timeSheetEndpoints = {
  // Lấy Bản Ghi Chấm Công Theo Nhân Viên Và Ngày
  getTimeKeepingByEmployeeWithDate: (employee: string, date: string) =>
    `${TIME_KEEPING}/employee/${employee}/${date}`,

  // Lấy Bản Ghi Chấm Công Theo Nhân Viên Và Trong Giai Đoạn Ngày
  getTimeKeepingByEmployeeWithPeriod: (
    employee: string,
    startDate: string,
    endDate: string,
  ) => `${TIME_KEEPING}/employee/${employee}/${startDate}/${endDate}`,

  // Lấy Bản Ghi Chấm Công Theo Trong Giai Đoạn Ngày
  getTimeKeepingByRangeDatePeriod: (startDate: string, endDate: string) =>
    `${TIME_KEEPING}/range-date/${startDate}/${endDate}`,

  // Lấy Bản Ghi Chấm Công Theo Trong Ngày
  getTimeKeepingByDate: (startDate: string) =>
    `${TIME_KEEPING}/date/${startDate}`,

  // Cập Nhật Bản Ghi Chấm CônC
  patchTimeKeeping: (id: string) => `${TIME_KEEPING}/${id}`,

  // Tạo Bản Ghi Chấm Công
  postTimeKeeping: `${TIME_KEEPING}`,

  // Tạo Bản Ghi Chấm Công Chi Tiết
  postTimeKeepingRecord: `${TIME_KEEPING_RECORD}`,

  // Lấy Bản Các Bảng Ghi Chấm Công Chi Tiết
  getTimeKeepingRecordByEmployeeWithDate: (employee: string, date: string) =>
    `${TIME_KEEPING_RECORD}/${employee}/${date}`,

  // Lấy Bản Ghi Chấm Công Chi Tiết
  // getTimeKeepingRecordById: (id: string) => `${TIME_KEEPING_RECORD}/${id}`,
  getTimeKeepingRecordById: (id: string) => `${TIME_KEEPING}/${id}`,

  // Cập Nhật Bản Ghi Chấm Công Chi Tiết
  putTimeKeepingRecord: (id: string) => `${TIME_KEEPING_RECORD}/${id}`,

  // Xóa Bản Ghi Chấm Công
  deleteTimeKeepingRecord: (id: string) => `${TIME_KEEPING_RECORD}/${id}`,

  // Xuất Chấm Công Theo Ngày bắt đầu, ngày kết thúc (CCHL)
  getTimeKeepingExportByDate: (startDate: string, endDate: string) =>
    `${TIME_KEEPING}/export/${startDate}/${endDate}`,

  // Xuất Chấm Công Theo Ngày bắt đầu, ngày kết thúc (BCC)
  getTimeKeepingRecordExportByDate: (startDate: string, endDate: string) =>
    `${TIME_KEEPING_RECORD}/export/${startDate}/${endDate}`,

  // Lấy Danh Sách Bản Ghi Chấm Công Nhân Viên Và Trong Giai Đoạn Ngày
  getTimeKeepingGroupByEmployee: `${TIME_KEEPING}/group-by-employee`,

  getOffTracking: (employeeId: string) =>
    `${TIME_KEEPING}/off-tracking/${employeeId}`,

  refreshTimeKeepingMachineData: `${TIME_KEEPING_CRON_JOB}/refresh-data-machine`,
};

export const leaveRequestEndpoints = {
  // Lấy danh sách đơn nghỉ phép
  getLeaveRequests: `${LEAVE_REQUESTS}`,

  // Tạo Đơn Nghỉ Phép
  postLeaveRequest: `${LEAVE_REQUESTS}`,

  countDayOff: `${LEAVE_REQUESTS}/count-day-off`,

  // Lấy Chi Tiết Đơn Nghỉ Phép
  getLeaveRequestById: (id: string) => `${LEAVE_REQUESTS}/${id}`,

  // Cập Nhật Đơn Nghỉ Phép
  putLeaveRequest: (id: string) => `${LEAVE_REQUESTS}/${id}`,

  // Cập Nhật Đơn Nghỉ Phép
  patchLeaveRequest: (id: string) => `${LEAVE_REQUESTS}/${id}`,

  // Xét Duyệt Đơn Nghỉ Phép
  patchLeaveRequestReview: (id: string) => `${LEAVE_REQUESTS_REVIEWS}/${id}`,

  // Huỷ Đơn Nghỉ Phép
  patchLeaveRequestCancel: (id: string) => `${LEAVE_REQUESTS}/${id}/cancel`,

  // Xoá Nhật Đơn Nghỉ Phép
  deleteLeaveRequest: (id: string) => `${LEAVE_REQUESTS}/${id}`,

  // Lấy tổng số ngày nghỉ.
  getLeaveRequestCountDayEndOff: `${LEAVE_REQUESTS}/count-day-end-off`,
};

export const timeKeepingResourseEndpoints = {
  // Lấy danh sách đơn nghỉ phép
  getLeaveTrackingExport: `${LEAVE_TRACKING_EXPORT}`,
};

export const productionStandardEndpoints = {
  // Tạo mới Định Mức Sản Xuất
  postProductionStandard: `${PRODUCTION_STANDARDS}`,

  // Lấy Định Mức Sản Xuất
  getProductionStandardById: (id: string) => `${PRODUCTION_STANDARDS}/${id}`,

  // Lấy Danh Sách Định Mức Sản Xuất
  getProductionStandards: `${PRODUCTION_STANDARDS}`,

  // Cập Nhật Định Mức Sản Xuất
  putProductionStandard: (id: string) => `${PRODUCTION_STANDARDS}/${id}`,

  // Xóa Định Mức Sản Xuất
  deleteProductionStandard: (id: string) => `${PRODUCTION_STANDARDS}/${id}`,

  // Lấy danh sách định mức sản xuất và lệnh
  getProductionStandardWithCommands: `${PRODUCTION_STANDARDS_MO}`,

  // Hủy Định Mức Sản Xuất
  disposeProductionStandard: (id: string) =>
    `${PRODUCTION_STANDARDS}/${id}/cancel`,
};

export const employeeEndpoints = {
  // Lấy thông tin nhân viên
  getEmployees: `${EMPLOYEES}`,

  getEmployeeProfiles: `${EMPLOYEE_PROFILE}`,

  postEmployee: `${EMPLOYEE_PROFILE}`,

  getEmployeeById: (id: string) => `${EMPLOYEE_PROFILE}/${id}`,

  patchEmployee: (id: string) => `${EMPLOYEE_PROFILE}/${id}`,

  getEmployeeSequenceId: `${EMPLOYEE_ID_SEQUENCES}`,

  getEmployeeByTaxCode: (taxCode: string) =>
    `${EMPLOYEE_PROFILE}/tax-code/${taxCode}`,

  getEmployeeByCitizenId: (idCard: string) =>
    `${EMPLOYEE_PROFILE}/citizen-id/${idCard}`,

  getEmployeeByBankNumber: (bankCode: string) =>
    `${EMPLOYEE_PROFILE}/bank-number/${bankCode}`,

  enableEmployee: (id: string) => `${EMPLOYEE_PROFILE}/${id}/enable`,

  disabledEmployee: (id: string) => `${EMPLOYEE_PROFILE}/${id}/disable`,

  profileAttachments: (id: string) => `${EMPLOYEE_PROFILE_ATTACHMENT}/${id}`,

  confirmLeave: `${CONFIRM_LEAVE}`,

  exportEmployee: `${EMPLOYEE_PROFILE}/export`,

  exportEmployeeXlsx: `${EMPLOYEE_PROFILE}/export-xlsx`,

  exportEmployeeContract: (id: string) => `${EMPLOYEE_PROFILE}/${id}/contract`,

  importEmployee: (fileId: string) => `${EMPLOYEE_PROFILE}/import/${fileId}`,

  getEmployeeChangeLog: `${EMPLOYEE_CHANGE_LOG}`,

  getEmployeeChangeLogById: (id: string) => `${EMPLOYEE_CHANGE_LOG}/${id}`,

  getListDepartments: `${EMPLOYEE_PROFILE}/list-department`,

  getListProfileByIds: `${EMPLOYEE_PROFILE}/list`,

  getXlsxTemplate: `${EMPLOYEE_PROFILE}/xlsx-template`,

  patchEnableTimeKeepingDevice: (id: string) =>
    `${EMPLOYEE_PROFILE}/${id}/enable-timekeeping-device`,

  syncAccountStatus: `${EMPLOYEE_PROFILE}/sync-account-status`,
};

export const timeKeepingViolationsEndpoints = {
  // Lấy danh sách vi phạm chấm công
  getTimeKeepingViolations: `${TIME_KEEPING_VIOLATIONS}`,

  // Lấy danh sách vi phạm chấm công theo giải trình
  getTimeKeepingViolationsByExplanation: (id: string) =>
    `${TIME_KEEPING_VIOLATIONS}/explanation-id/${id}`,
};

export const timeKeepingExplanationEndpoints = {
  // Lấy danh sách giải trình chấm công
  getTimeKeepingExplanations: `${TIME_KEEPING_EXPLANATIONS}`,

  // Tạo giải trình chấm công
  postTimeKeepingExplanation: `${TIME_KEEPING_EXPLANATIONS}`,

  // Cập nhật giải trình chấm công
  patchTimeKeepingExplanation: (id: string) =>
    `${TIME_KEEPING_EXPLANATIONS}/${id}`,

  // Lấy chi tiết giải trình chấm công
  getTimeKeepingExplanationById: (id: string) =>
    `${TIME_KEEPING_EXPLANATIONS}/${id}`,

  // Xóa giải trình chấm công
  deleteTimeKeepingExplanation: (id: string) =>
    `${TIME_KEEPING_EXPLANATIONS}/${id}`,

  // Huỷ giải trình chấm công
  patchTimeKeepingExplanationCancel: (id: string) =>
    `${TIME_KEEPING_EXPLANATIONS}/${id}/cancel`,
};

export const timeKeepingExplanationReviewEndpoints = {
  postTimeKeepingExplanationReviews: `${TIME_KEEPING_EXPLANATION_REVIEWS}`,

  patchTimeKeepingExplanationReviews: (id: string) =>
    `${TIME_KEEPING_EXPLANATION_REVIEWS}/${id}`,
};

export const timeKeepingMonthlyEndpoints = {
  // Lấy danh sách chấm công hàng tháng
  getTimeKeepingMonthly: `${TIME_KEEPING_MONTHLY}`,

  // Chi tiết chấm công theo tháng
  getTimeKeepingMonthlyById: (id: string) => `${TIME_KEEPING_MONTHLY}/${id}`,

  // Lấy danh sách chấm công theo tháng
  getTimeKeepingMonthlyExport: `${TIME_KEEPING_MONTHLY}/export-timesheet`,

  // Duyệt chấm công tháng
  reviewTimeKeepingMonthlyApprove: `${TIME_KEEPING_MONTHLY_REVIEWS}`,

  // Đồng ý xét duyệt chấm công tháng
  approveTimesheetBulk: `${TIME_KEEPING_MONTHLY}/approve`,

  // Từ chối xét duyệt chấm công tháng
  rejectTimesheetBulk: `${TIME_KEEPING_MONTHLY}/reject`,
};

export const productionCommandEndpoints = {
  // Lấy danh sách lệnh sản xuất
  getProductionCommands: `${PRODUCTION_COMMANDS}`,

  // Tạo lệnh sản xuất
  postProductionCommand: `${PRODUCTION_COMMANDS}`,

  // Cập nhật lệnh sản xuất
  patchProductionCommand: (id: string) => `${PRODUCTION_COMMANDS}/${id}`,

  // Lấy chi tiết lệnh sản xuất
  getProductionCommandById: (id: string) => `${PRODUCTION_COMMANDS}/${id}`,

  // Xóa lệnh sản xuất
  deleteProductionCommand: (id: string) => `${PRODUCTION_COMMANDS}/${id}`,

  // Huỷ lệnh sản xuất
  patchProductionCommandCancel: (id: string) =>
    `${PRODUCTION_COMMANDS}/${id}/cancel`,

  // Lấy danh sách lệnh sản xuất với các công đoạn
  getProductionCommandsWithProcess: `${PRODUCTION_COMMANDS}/work-orders`,

  // Lấy chi tiết lệnh sản xuất với các công đoạn
  getProductionCommandsWithProcessById: (id: string) =>
    `${PRODUCTION_COMMANDS}/${id}`,

  // Bắt đầu lệnh sản xuất
  patchProductionCommandStart: (id: string) =>
    `${PRODUCTION_COMMANDS}/${id}/start`,

  //
  getProductionCommandMaintain: (id: string) =>
    `${PRODUCTION_COMMANDS}/maintain/${id}`,

  // Hoàn thành 1 công đoạn sản xuất
  completeStateProductionCommand: (id: string, isSkip?: number) =>
    `${PRODUCTION_COMMANDS}/${id}/complete-state?isSkip=` + isSkip,

  // Count totalQty in standards 1 công đoạn sản xuất
  getProductionCommandsCountStandard: (standardId: string) =>
    `${PRODUCTION_COMMANDS}/count-standards/${standardId}`,

  // Xuất excel đề nghị thanh toán.
  exportExcelProductionCommands: () =>
    `${PRODUCTION_COMMANDS}/export?download=true`,
};

export const productionProcessEndpoints = {
  // Lấy danh sách quy trình sản xuất
  getProductionProcesses: `${PRODUCTION_PROCESS}`,

  // Tạo quy trình sản xuất
  postProductionProcess: `${PRODUCTION_PROCESS}`,

  // Cập nhật quy trình sản xuất
  patchProductionProcess: (id: string) => `${PRODUCTION_PROCESS}/${id}`,

  // Lấy chi tiết quy trình sản xuất
  getProductionProcessById: (id: string) => `${PRODUCTION_PROCESS}/${id}`,

  // Xóa quy trình sản xuất
  deleteProductionProcess: (id: string) => `${PRODUCTION_PROCESS}/${id}`,

  // Bắt đầu quy trình sản xuất
  patchProductionProcessStart: (id: string) =>
    `${PRODUCTION_PROCESS}/${id}/start`,

  // Dừng quy trình sản xuất
  patchProductionProcessStop: (id: string) =>
    `${PRODUCTION_PROCESS}/${id}/stop`,

  // Hoàn thành quy trình sản xuất
  patchProductionProcessComplete: (id: string) =>
    `${PRODUCTION_PROCESS}/${id}/complete`,
};

export const receiveMaterialChecklistEndpoints = {
  // Tạo biểu mẫu giám sát tiếp nhận nguyên liệu
  postReceiveMaterialChecklist: `${RECEIVE_MATERIAL_CHECKLIST}`,

  // Cập nhật biểu mẫu giám sát tiếp nhận nguyên liệu
  patchReceiveMaterialChecklist: (id: string) =>
    `${RECEIVE_MATERIAL_CHECKLIST}/${id}`,

  // Xóa biểu mẫu giám sát tiếp nhận nguyên liệu
  deleteReceiveMaterialChecklist: (id: string) =>
    `${RECEIVE_MATERIAL_CHECKLIST}/${id}`,

  // Lấy chi tiết biểu mẫu giám sát tiếp nhận nguyên liệu
  getReceiveMaterialChecklistById: (id: string) =>
    `${RECEIVE_MATERIAL_CHECKLIST}/${id}`,
};

export const steamingProcessChecklistEndpoints = {
  // Tạo biểu mẫu giám sát quy trình hấp sấy
  postSteamingProcessChecklist: `${STEAMING_PROCESS_CHECKLIST}`,

  // Cập nhật biểu mẫu giám sát quy trình hấp sấy
  patchSteamingProcessChecklist: (id: string) =>
    `${STEAMING_PROCESS_CHECKLIST}/${id}`,

  // Xóa biểu mẫu giám sát quy trình hấp sấy
  deleteSteamingProcessChecklist: (id: string) =>
    `${STEAMING_PROCESS_CHECKLIST}/${id}`,

  // Lấy chi tiết biểu mẫu giám sát quy trình hấp sấy
  getSteamingProcessChecklistById: (id: string) =>
    `${STEAMING_PROCESS_CHECKLIST}/${id}`,
};

export const additiveMaterialChecklistEndpoints = {
  // Tạo biểu mẫu giám sát nguyên liệu phụ gia
  postAdditiveMaterialChecklist: `${ADDITIVE_MATERIAL_CHECKLIST}`,

  // Cập nhật biểu mẫu giám sát nguyên liệu phụ gia
  patchAdditiveMaterialChecklist: (id: string) =>
    `${ADDITIVE_MATERIAL_CHECKLIST}/${id}`,

  // Xóa biểu mẫu giám sát nguyên liệu phụ gia
  deleteAdditiveMaterialChecklist: (id: string) =>
    `${ADDITIVE_MATERIAL_CHECKLIST}/${id}`,

  // Lấy chi tiết biểu mẫu giám sát nguyên liệu phụ gia
  getAdditiveMaterialChecklistById: (id: string) =>
    `${ADDITIVE_MATERIAL_CHECKLIST}/${id}`,
};

export const metalDetectionChecklistEndpoints = {
  // Tạo biểu mẫu kiểm tra nam châm và lưới
  postMetalDetectionChecklist: `${METAL_DETECTION_CHECKLIST}`,

  // Cập nhật biểu mẫu kiểm tra nam châm và lưới
  patchMetalDetectionChecklist: (id: string) =>
    `${METAL_DETECTION_CHECKLIST}/${id}`,

  // Xóa biểu mẫu kiểm tra nam châm và lưới
  deleteMetalDetectionChecklist: (id: string) =>
    `${METAL_DETECTION_CHECKLIST}/${id}`,

  // Lấy chi tiết biểu mẫu kiểm tra nam châm và lưới
  getMetalDetectionChecklistById: (id: string) =>
    `${METAL_DETECTION_CHECKLIST}/${id}`,
};

export const mixingReportChecklistEndpoints = {
  // Tạo biểu mẫu báo cáo trộn sản phẩm bột cá
  postMixingReportChecklist: `${MIXING_REPORT_CHECKLIST}`,

  // Cập nhật biểu mẫu báo cáo trộn sản phẩm bột cá
  patchMixingReportChecklist: (id: string) =>
    `${MIXING_REPORT_CHECKLIST}/${id}`,

  // Xóa biểu mẫu báo cáo trộn sản phẩm bột cá
  deleteMixingReportChecklist: (id: string) =>
    `${MIXING_REPORT_CHECKLIST}/${id}`,

  // Lấy chi tiết biểu mẫu báo cáo trộn sản phẩm bột cá
  getMixingReportChecklistById: (id: string) =>
    `${MIXING_REPORT_CHECKLIST}/${id}`,
};

export const machineOperationChecklistEndpoints = {
  // Tạo biểu mẫu báo cáo trộn sản phẩm bột cá
  postMachineOperationChecklist: `${MACHINE_OPERATION_CHECKLIST}`,

  // Cập nhật biểu mẫu báo cáo trộn sản phẩm bột cá
  patchMachineOperationChecklist: (id: string) =>
    `${MACHINE_OPERATION_CHECKLIST}/${id}`,

  // Xóa biểu mẫu báo cáo trộn sản phẩm bột cá
  deleteMachineOperationChecklist: (id: string) =>
    `${MACHINE_OPERATION_CHECKLIST}/${id}`,

  // Lấy chi tiết biểu mẫu báo cáo trộn sản phẩm bột cá
  getMachineOperationChecklistById: (id: string) =>
    `${MACHINE_OPERATION_CHECKLIST}/${id}`,
};

export const qualityCheckSampleEndpoints = {
  // Lấy danh sách biểu mẫu giám sát chất lượng sản phẩm
  getQualityCheckSamples: `${PRODUCTION_QUALITY_CONTROL}`,

  // Tạo biểu mẫu giám sát chất lượng sản phẩm
  postQualityCheckSample: `${PRODUCTION_QUALITY_CONTROL}`,

  // Cập nhật biểu mẫu giám sát chất lượng sản phẩm
  patchQualityCheckSample: (id: string) =>
    `${PRODUCTION_QUALITY_CONTROL}/${id}`,

  // Xóa biểu mẫu giám sát chất lượng sản phẩm
  deleteQualityCheckSample: (id: string) =>
    `${PRODUCTION_QUALITY_CONTROL}/${id}`,

  // Lấy chi tiết biểu mẫu giám sát chất lượng sản phẩm
  getQualityCheckSampleById: (id: string) =>
    `${PRODUCTION_QUALITY_CONTROL}/${id}`,

  // Cập nhập trạng thái đã đạt của mẫu giám sát chất lượng sản phẩm
  patchQualityCheckSampleByIdReviewPass: (id: string) =>
    `${PRODUCTION_QUALITY_CONTROL}/${id}/review-pass`,

  // Cập nhập trạng thái đã đạt của mẫu giám sát chất lượng sản phẩm
  patchQualityCheckSampleByIdReviewFail: (id: string) =>
    `${PRODUCTION_QUALITY_CONTROL}/${id}/review-fail`,

  // Tù chối biểu mẫu giám sát chất lượng sản phẩm
  rejectQualityCheckSample: (id: string) =>
    `${PRODUCTION_QUALITY_CONTROL}/${id}/reject`,
};

export const qualityDisposalEndpoints = {
  // Tạo biểu mẫu huỷ mẫu kiểm thử
  postQualityDisposal: `${PRODUCTION_QUALITY_DISPOSAL}`,

  // Từ chối / Xác nhận huỷ mẫu kiểm thử
  patchQualityDisposal: (id: string) => `${PRODUCTION_QUALITY_DISPOSAL}/${id}`,

  // Từ chối / Xác nhận huỷ mẫu kiểm thử
  patchQualityDisposalReviews: (id: string) =>
    `${PRODUCTION_QUALITY_DISPOSAL}/${id}/reviews`,
};

export const workCenterEndpoints = {
  // Lấy danh sách cụm máy sản xuất
  getWorkCenters: `${PRODUCTION_WORK_CENTER}`,

  // Lấy chi tiết cụm máy sản xuất
  getWorkCenterById: (id: string) => `${PRODUCTION_WORK_CENTER}/${id}`,

  // Tạo cụm máy sản xuất
  postWorkCenter: `${PRODUCTION_WORK_CENTER}`,

  // Cập nhật cụm máy sản xuất
  patchWorkCenter: (id: string) => `${PRODUCTION_WORK_CENTER}/${id}`,

  // Xóa cụm máy sản xuất
  deleteWorkCenter: (id: string) => `${PRODUCTION_WORK_CENTER}/${id}`,
};

export const factoryEndpoints = {
  // Lấy danh sách nhà máy
  getFactories: `${FACTORIES}`,
};

export const storageEndpoints = {
  getStorages: `${STORAGES}`,
};

export const productionRoutingsEndpoints = {
  // Lấy danh sách định tuyến
  getRoutings: `${PRODUCTION_ROUTINGS}`,

  // Tạo định tuyến
  postRouting: `${PRODUCTION_ROUTINGS}`,

  // Cập nhật định tuyến
  patchRouting: (id: string) => `${PRODUCTION_ROUTINGS}/${id}`,

  // Xóa định tuyến
  deleteRouting: (id: string) => `${PRODUCTION_ROUTINGS}/${id}`,

  // Lấy chi tiết định tuyến
  getRoutingById: (id: string) => `${PRODUCTION_ROUTINGS}/${id}`,
};

export const productionMaintainsEndpoints = {
  // Lấy danh sách định tuyến
  getMaintains: `${PRODUCTION_MAINTAINS}`,

  // Tạo định tuyến
  postMaintain: `${PRODUCTION_MAINTAINS}`,

  // Cập nhật định tuyến
  patchMaintain: (id: string) => `${PRODUCTION_MAINTAINS}/${id}`,

  // Xóa định tuyến
  deleteMaintain: (id: string) => `${PRODUCTION_MAINTAINS}/${id}`,

  // Lấy chi tiết định tuyến
  getMaintainById: (id: string) => `${PRODUCTION_MAINTAINS}/${id}`,
};

export const productionPackagesEndpoints = {
  // Lấy danh sách định tuyến
  getPackages: `${PRODUCTION_PACKAGES}`,

  // Tạo định tuyến
  postPackage: `${PRODUCTION_PACKAGES}`,

  // Cập nhật định tuyến
  patchPackage: (id: string) => `${PRODUCTION_PACKAGES}/${id}`,

  // Xóa định tuyến
  deletePackage: (id: string) => `${PRODUCTION_PACKAGES}/${id}`,

  // Lấy chi tiết định tuyến
  getPackageById: (id: string) => `${PRODUCTION_PACKAGES}/${id}`,
};

export const customerEndpoints = {
  // Lấy danh sách khách hàng hiệu lực
  getEnabledCustomers: `${CUSTOMERS}/enabled`,

  // Lấy danh sách khách hàng vô hiệu
  getDisabledCustomers: `${CUSTOMERS}/disabled`,

  // Tạo mới khách hàng
  postCustomer: `${CUSTOMERS}`,

  // Cập nhật khách hàng
  patchCustomer: (id: string) => `${CUSTOMERS}/${id}`,

  // Lấy chi tiết khách hàng
  getCustomerById: (id: string) => `${CUSTOMERS}/${id}`,

  // Vô hiệu khách hàng
  deleteCustomerDisable: (id: string) => `${CUSTOMERS}/${id}/disable`,

  // Kích hoạt khách hàng
  patchCustomerEnable: (id: string) => `${CUSTOMERS}/${id}/activate`,

  // Chuyển giao khách hàng
  patchCustomerTransfer: (id: string, newOwnerId: string) =>
    `${CUSTOMERS}/${id}/transfer/${newOwnerId}`,

  // Xóa khách hàng
  deleteCustomer: (id: string) => `${CUSTOMERS}/${id}`,

  // Lấy khách hàng bằng mã KH
  getCustomerByCode: (code: string) => `${CUSTOMERS}/customer-code/${code}`,

  // Xuất file CSV danh sách khách hàng
  exportCustomersEnabled: `${CUSTOMERS}/export/enabled`,

  // Xuất file CSV danh sách khách hàng đã xoá
  exportCustomersDisabled: `${CUSTOMERS}/export/disabled`,

  // Mã khách hàng tiếp theo
  getNextCustomerCode: `${CUSTOMERS}/nextCustomerCode`,

  // Lấy khách hàng bằng MST
  getCustomerByTaxCode: (taxCode: string) => `${CUSTOMERS}/tax-code/${taxCode}`,

  // Lấy sinh nhật khách hàng
  getCustomerBirthday: `${CUSTOMERS}/birthday`,
};

export const contractsEndpoints = {
  // Lấy danh sách hợp đồng
  getContracts: `${CONTRACTS}`,

  // Tạo mới hợp đồng
  postContract: `${CONTRACTS}`,

  // Lấy chi tiết hợp đồng
  getContractById: (id: string) => `${CONTRACTS}/${id}`,

  // Cập nhật hợp đồng
  patchContract: (id: string) => `${CONTRACTS}/${id}`,

  // Xoá hợp đồng
  deleteContract: (id: string) => `${CONTRACTS}/${id}`,

  // Phục hồi hợp đồng
  recoverContract: (id: string) => `${CONTRACTS}/recover-contract/${id}`,

  // Đề xuất xét duyệt
  approvedReviewContract: (id: string) => `${CONTRACTS}/approved-review/${id}`,

  // Xét duyệt hợp đồng
  approveContract: (id: string) => `${CONTRACTS}/approved/${id}`,

  // Đề xuất xét duyệt thanh lý
  reviewContractLiquid: (id: string) => `${CONTRACTS}/liqidated-review/${id}`,

  // Xét duyệt thanh lý hợp đồng
  consentContractLiquid: (id: string) => `${CONTRACTS}/liqidated-consent/${id}`,

  // Từ chối xét duyệt thanh lý hợp đồng
  refuseContractLiquid: (id: string) => `${CONTRACTS}/liqidated-refusal/${id}`,

  // Lấy danh sách hợp đồng đã xoá
  getContractsDeleted: `${CONTRACTS}/status-delete`,

  // Lấy Danh sách thành phẩm
  getMaterials: `${CONTRACTS}/list-material`,

  // Lấy danh sách sản phẩm
  getProducts: `${CONTRACTS}/list-product`,

  // Lấy danh sách hợp đồng mẫu
  getFilesContract: `${CONTRACTS}/files-template-contract`,

  // Hoàn tất hợp đồng
  patchContractMaskFinished: (id: string) => `${CONTRACTS}/${id}/mask-finished`,

  // Gửi yêu cầu đề xuất thanh lý hợp đồng
  patchContractRequestLiquidationReview: (id: string) =>
    `${CONTRACTS}/${id}/request-liquidation-review`,

  // Xét duyệt đề nghị thanh lý hợp đồng
  patchContractReview: `${CONTRACTS}/review`,

  // Gửi yêu cầu xét duyệt hợp đồng.
  patchContractProposeApprove: (id: string) =>
    `${CONTRACTS}/${id}/request-review`,

  // xét duyệt hợp đồng.
  patchContractNormalReview: (id: string) => `${CONTRACTS}/${id}/normal-review`,
};

export const orderEndpoints = {
  // Lấy danh sách đơn đặt hàng
  getOrders: `${ORDERS}`,

  // Tạo mới đơn đặt hàng
  postOrder: `${ORDERS}`,

  // Lấy chi tiết đơn đặt hàng
  getOrderById: (id: string) => `${ORDERS}/${id}`,

  // Cập nhật đơn đặt hàng
  patchOrder: (id: string) => `${ORDERS}/${id}`,

  // Xóa đơn đặt hàng
  deleteOrder: (id: string) => `${ORDERS}/${id}`,

  // Huỷ đơn đặt hàng
  patchOrderCancel: (id: string) => `${ORDERS}/${id}/cancel`,

  // Tải đơn hàng
  getOrderExport: (id: string, download: boolean) =>
    `${ORDERS}/${id}/export?download=${download}`,

  // Yêu cầu xét duyệt
  postOrderReview: `${ORDERS_REVIEWS}`,

  // Xác nhận xét duyệt
  patchOrderReview: (id: string) => `${ORDERS_REVIEWS}/${id}/review`,

  // Get order review by order id
  getOrderReviewByOrderId: (id: string) => `${ORDERS_REVIEWS}/order/${id}`,

  // Get orders with list production manufacture orders
  getOrdersWithManufactureOrders: `${ORDERS}/join-with-manufacture-order`,
};

export const purchaseRequestEndpoints = {
  // Lấy danh sách đề nghị thu mua
  getPurchaseRequests: `${PURCHASE}`,

  // Tạo mới đề nghị thu mua
  postPurchaseRequest: `${PURCHASE}`,

  // Lấy chi tiết đề nghị thu mua
  getPurchaseRequestById: (id: string) => `${PURCHASE}/${id}`,

  // Cập nhật đề nghị thu mua
  patchPurchaseRequest: (id: string) => `${PURCHASE}/${id}`,

  // Xóa đề nghị thu mua
  deletePurchaseRequest: (id: string) => `${PURCHASE}/${id}`,

  // Cập nhật trạng thái giao hàng đề nghị thu mua
  patchPurchaseRequestDelivery: (id: string) => `${PURCHASE_DELIVERY}/${id}`,

  // Tạo đơn xét duyệt
  postPurchaseReview: `${PURCHASE_REVIEW}`,

  // Xét duyệt đơn đề nghị thu mua
  patchPurchaseReview: (id: string) => `${PURCHASE_REVIEW}/${id}/review`,

  // Lấy đơn xét duyệt
  getPurchaseReviewByPurchaseRequestId: (id: string) =>
    `${PURCHASE_REVIEW}/purchase-request/${id}`,

  // Lấy file
  getPurchaseRequestFileById: (id: string) => `${PURCHASE_FILE}/${id}`,
};

export const quotationsEndpoints = {
  // Lấy danh sách báo giá
  getQuotations: `${QUOTATIONS}`,

  // Tạo mới báo giá
  postQuotation: `${QUOTATIONS}`,

  // Lấy chi tiết báo giá
  getQuotationById: (id: string) => `${QUOTATIONS}/${id}`,

  // Cập nhật báo giá
  patchQuotation: (id: string) => `${QUOTATIONS}/${id}`,

  // Xoá bảng báo giá
  deleteQuotation: (id: string) => `${QUOTATIONS}/${id}`,

  // Gửi báo giá duyệt nội bộ
  patchQuotationInternalSend: (id: string) =>
    `${QUOTATIONS}/${id}/internal-sent`,

  // Hủy báo giá
  patchQuotationCancel: (id: string) => `${QUOTATIONS}/${id}/cancel`,

  // Gửi báo giá cho khách hàng
  patchQuotationSend: (id: string) => `${QUOTATIONS}/${id}/send-customer`,

  // Xét duyệt nội bộ
  patchInternalApproved: (id: string) => `${QUOTATIONS}/${id}/internal-process`,

  // Từ chối xét duyệt nội bộ
  patchInternalReject: (id: string) => `${QUOTATIONS}/${id}/internal-reject`,

  // KH xét duyệt
  patchCustomerApproved: (id: string) => `${QUOTATIONS}/${id}/customer-process`,

  // tải file và xem file
  getQuotationExport: (id: string, download: boolean) =>
    `${QUOTATIONS}/${id}/export?download=${download}`,
};

export const quotationDetailsEndpoints = {
  // Lấy chi tiết báo giá
  getQuotationDetailsById: (id: string): string => `${QUOTATIONDETAILS}/${id}`,

  // Tạo mới chi tiết báo giá
  postQuotationDetails: `${QUOTATIONDETAILS}`,

  // Cập nhập chi tiết báo giá
  patchQuotationDetails: (id: string): string => `${QUOTATIONDETAILS}/${id}`,

  // Xóa chi tiết báo giá
  deleteQuotationDetails: (id: string): string => `${QUOTATIONDETAILS}/${id}`,
};

export const workspaceEndpoints = {
  // Lấy thông tin VP/NM
  getWorkspaces: `${WORKSPACE}`,

  // Tạo mới VP/NM
  postWorkspace: `${WORKSPACE}`,

  // Lấy chi tiết VP/NM
  getWorkspaceById: (id: string) => `${WORKSPACE}/${id}`,

  // Cập nhật VP/NM
  patchWorkspace: (id: string) => `${WORKSPACE}/${id}`,

  // Xoá VP/NM
  deleteWorkspace: (id: string) => `${WORKSPACE}/${id}`,
};

export const annualLeaveEndpoints = {
  // Lấy danh sách nghỉ phép năm
  getAnnualLeaves: `${ANNUAL_LEAVE}`,

  // Cập nhật nghỉ phép năm
  patchAnnualLeave: `${ANNUAL_LEAVE}`,

  // Số phép năm còn lại
  getEmployeeAnnualLeave: (id: string) => `${ANNUAL_LEAVE}/employee/${id}`,

  // Thâm niên
  getEmployeeSeniority: (workspaceId: string, date: string) =>
    `${ANNUAL_LEAVE}/seniority/${workspaceId}/${date}`,
};

export const recruitmentEndpoints = {
  // Lấy danh sách yêu cầu tuyển dụng
  getRecruitmentRequests: `${RECRUITMENT}`,

  // Lấy danh sách lịch sử yêu cầu tuyển dụng
  getRecruitmentHistoryRequests: (id: string) =>
    `${RECRUITMENT}/recruitment-change-logs/${id}`,

  // Tạo mới yêu cầu tuyển dụng
  postRecruitmentRequest: `${RECRUITMENT}/recruitment`,

  // Lấy chi tiết yêu cầu tuyển dụng
  getRecruitmentRequestById: (id: string) => `${RECRUITMENT}/${id}`,

  // Cập nhật yêu cầu tuyển dụng
  patchRecruitmentRequest: (id: string) => `${RECRUITMENT}/${id}`,

  // Gia hạn tuyển dụng
  patchRecruitmentRequestAdjourn: (id: string) =>
    `${RECRUITMENT}/adjourn/${id}`,

  // Xoá yêu cầu tuyển dụng
  deleteRecruitmentRequest: (id: string) => `${RECRUITMENT}/${id}`,

  // Duyệt yêu cầu tuyển dụng
  approveRecruitmentRequest: (id: string) =>
    `${RECRUITMENT}/Approve-consent/${id}`,

  // Từ chối yêu cầu tuyển dụng
  rejectRecruitmentRequest: (id: string) =>
    `${RECRUITMENT}/Approve-refusal/${id}`,

  // Lên lịch phỏng vấn
  postRecruitmentInterview: (id: string) => `${RECRUITMENT}/interview/${id}`,

  // Cập nhật kết quả phỏng vấn
  patchRecruitmentInterviewResult: (id: string) =>
    `${RECRUITMENT_INTERVIEWS}/${id}/result`,

  // Cập nhật lịch phỏng vấn
  patchInterviewSchedule: (id: string) => `${RECRUITMENT_INTERVIEWS}/${id}`,

  // lấy chi tiết lên lịch phỏng vấn
  getInterviewSchedule: (id: string) => `${RECRUITMENT_INTERVIEWS}/${id}`,

  // Danh sách ứng viên
  getRecruitmentCandidates: `${RECRUITMENT_INTERVIEWS}`,

  // Chi tiết ứng viên
  getRecruitmentCandidateById: (id: string) =>
    `${RECRUITMENT_INTERVIEWS}/${id}`,
};

export const leaveRegimeEndpoints = {
  // Lấy danh sách chế độ nghỉ phép
  getLeaveRegimes: `${LEAVE_REGIME}`,

  // Tạo mới chế độ nghỉ phép
  postLeaveRegime: `${LEAVE_REGIME}`,

  // Lấy chi tiết chế độ nghỉ phép
  getLeaveRegimeById: (id: string) => `${LEAVE_REGIME}/${id}`,

  // Cập nhật chế độ nghỉ phép
  patchLeaveRegime: (id: string) => `${LEAVE_REGIME}/${id}`,

  // Xoá chế độ nghỉ phép
  deleteLeaveRegime: (id: string) => `${LEAVE_REGIME}/${id}`,

  // Huỷ chế độ nghỉ phép
  cancelLeaveRegime: (id: string) => `${LEAVE_REGIME}/${id}/cancel`,

  // Duyệt chế độ nghỉ phép
  processLeaveRegime: (id: string) => `${PROCESS_LEAVE_REGIME}/${id}`,

  // Duyệt chế độ nghỉ phép
  reviewLeaveRegime: (id: string) => `${LEAVE_REGIME}/${id}/process`,
};

export const documentariesEndpoints = {
  // Lấy danh sách công văn
  getDocumentaries: `${DOCUMENTARY}`,

  // Tạo mới công văn
  postDocumentary: `${DOCUMENTARY}`,

  // Lấy chi tiết công văn
  getDocumentaryById: (id: string) => `${DOCUMENTARY}/${id}`,

  // Cập nhật công văn
  patchDocumentary: (id: string) => `${DOCUMENTARY}/${id}`,

  // Xoá công văn
  deleteDocumentary: (id: string) => `${DOCUMENTARY}/${id}`,

  // Yêu cầu xét duyệt
  patchDocumentaryReview: (id: string) =>
    `${DOCUMENTARY}/documentary-review/${id}`,

  // Xét duyệt công văn
  patchDocumentaryConsent: (id: string) =>
    `${DOCUMENTARY}/documentary-consent/${id}`,

  // Từ chối xét duyệt công văn
  patchDocumentaryRefusal: (id: string) =>
    `${DOCUMENTARY}/documentary-refusal/${id}`,
};

export const reportsEndpoints = {
  // Báo cáo NV sắp hết hạn đồng phục
  getUniformExpiringReports: `${REPORT}/uniform-expiring`,

  // Báo cáo đồng phục nhập kho
  getUniformImportReports: `${REPORT}/uniform-import`,

  // Báo cáo đồng phục xuất kho
  getUniformExportReports: `${REPORT}/uniform-export`,

  // Báo cáo đồng phục tồn kho
  getUniformInventoryReports: `${REPORT}/uniform-inventory`,

  // Báo cáo xuất ứng, hoàn ứng
  getUniformSupportReports: `${REPORT}/uniform-support`,

  // Báo cáo theo dõi nhân viên sắp hết hạn hợp đồng
  getEmployeeExpiringContractReports: `${REPORT}/employee-expiring-contract`,

  // Xuất báo cáo theo dõi NV sắp hết hạn
  getEmployeeExpiringContractReportsExcel: `${REPORT}/employee-expiring-contract/excel`,

  // Báo cáo số lượng NV đăng ký nghỉ chế độ theo khung thời gian
  getLeaveRegimeReports: `${REPORT}/leave-regime-request`,

  // Xuất báo cáo số lượng NV đăng ký nghỉ chế độ theo khung thời gian
  getLeaveRegimeReportsExcel: `${REPORT}/leave-regime-request/excel`,

  // Báo cáo so sánh yêu cầu tuyển dụng và trạng thái tuyển dụng
  getRecruitmentReports: `${REPORT}/recruitment-request`,

  // Xuất báo so sánh yêu cầu tuyển dụng và trạng thái tuyển dụng
  getRecruitmentReportsExcel: `${REPORT}/recruitment-request/excel`,

  // Báo cáo biến động nhân sự
  getHumanResourceChangeReports: `${REPORT}/human-resource-change`,

  // Báo cáo tất cả xuất tồn đồng phục
  getAllUniformReports: `${REPORT}/all-uniform`,

  // Xuất báo cáo xuất nhập tồn đồng phục
  getUniformChangeReportsExcel: `${REPORT}/uniform-change/excel`,

  // Chi tiết xuất tồn đồng phục
  getUniformChangeReports: (id: string) => `${REPORT}/uniform-change/${id}`,
};

export const uniformEndpoints = {
  // Lấy danh sách đồng phục
  getUniforms: `${UNIFORM}`,

  // Tạo mới đồng phục
  postUniform: `${UNIFORM}`,

  // Lấy chi tiết đồng phục
  getUniformById: (id: string) => `${UNIFORM}/${id}`,

  // Cập nhật đồng phục
  patchUniform: (id: string) => `${UNIFORM}/${id}`,

  // Xoá đồng phục
  deleteUniform: (id: string) => `${UNIFORM}/${id}`,

  patchUniformStatus: (id: string, status: string) =>
    `${UNIFORM}/${id}/status/${status}`,

  exportUniform: `${UNIFORM}/export-xlsx`,
};

export const uniformOrderEndpoints = {
  // Lấy danh sách đơn đặt hàng đồng phục
  getUniformOrders: `${UNIFORM_ORDERS}`,

  // Tạo mới đơn đặt hàng đồng phục
  postUniformOrder: `${UNIFORM_ORDERS}`,

  // Lấy chi tiết đơn đặt hàng đồng phục
  getUniformOrderById: (id: string) => `${UNIFORM_ORDERS}/${id}`,

  // Cập nhật đơn đặt hàng đồng phục
  patchUniformOrder: (id: string) => `${UNIFORM_ORDERS}/${id}`,

  // Huỷ đơn hàng đồng phục
  cancelUniformOrder: (id: string) => `${UNIFORM_ORDERS}/${id}/cancel`,

  // Xoá đơn đặt hàng đồng phục
  deleteUniformOrder: (id: string) => `${UNIFORM_ORDERS}/${id}`,

  // Duyệt đơn đặt hàng đồng phục
  processUniformOrder: (id: string) => `${UNIFORM_ORDERS}/${id}/process`,

  // Xuất file đơn đặt hàng đồng phục
  exportUniformOrder: `${UNIFORM_ORDERS}/export`,

  // Nhập kho đơn hàng đồng phục
  uniformStockIn: (id: string) => `${UNIFORM_ORDERS}/${id}/stock`,

  // Xuất excel nhập kho
  exportUniformStockIn: `${UNIFORM_ORDERS}/export-order-xlsx`,
};

export const uniformOrderStockEndpoints = {
  // Nhập kho đơn hàng đồng phục
  postUniformOrderStock: `${UNIFORM_ORDERS_STOCKS}`,

  //// Xuất excel nhập kho
  exportUniformOrderStockIn: `${UNIFORM_ORDERS}/export-order-stock-xlsx`,
};

export const uniformReleaseEndpoints = {
  // Lấy danh sách xuất đồng phục
  getUniformReleases: `${UNIFORM_RELEASES}`,

  // Tạo mới xuất đồng phục
  postUniformRelease: `${UNIFORM_RELEASES}`,

  // Lấy chi tiết xuất đồng phục
  getUniformReleaseById: (id: string) => `${UNIFORM_RELEASES}/${id}`,

  // Xuất phiếu xuất
  exportUniformRelease: `${UNIFORM_RELEASES}/export-xlsx`,
};

export const uniformStockEndpoints = {
  // Lấy danh sách kho đồng phục
  getUniformStocks: `${UNIFORM_STOCKS}`,
};

export const uniformReturnEndpoints = {
  // Hoàn ứng đồng phục
  postUniformReturn: `${UNIFORM_RETURN}`,

  getUniformReturnsRemaining: `${UNIFORM_RETURN}/remaining`,
};

export const uomEndpoints = {
  // Lấy danh sách đơn vị tính
  getUoms: `${UOM}`,

  // Tạo mới đơn vị tính
  postUom: `${UOM}`,

  // Lấy chi tiết đơn vị tính
  getUomById: (id: string) => `${UOM}/${id}`,

  // Cập nhật đơn vị tính
  patchUom: (id: string) => `${UOM}/${id}`,

  // Xoá đơn vị tính
  deleteUom: (id: string) => `${UOM}/${id}`,
};

export const uomGroupEndpoints = {
  // Lấy danh sách nhóm đơn vị tính
  getUomGroups: `${UOM_GROUP}`,

  // Tạo mới nhóm đơn vị tính
  postUomGroup: `${UOM_GROUP}`,

  // Lấy chi tiết nhóm đơn vị tính
  getUomGroupById: (id: string) => `${UOM_GROUP}/${id}`,

  // Cập nhật nhóm đơn vị tính
  patchUomGroup: (id: string) => `${UOM_GROUP}/${id}`,

  // Xoá nhóm đơn vị tính
  deleteUomGroup: (id: string) => `${UOM_GROUP}/${id}`,
};

export const suppliersEndpoints = {
  // Lấy danh sách nhà cung cấp
  getSuppliers: `${SUPPLIERS}`,

  getSupplierTypes: `${SUPPLIER_TYPE}`,

  // Tạo mới nhà cung cấp
  postSupplier: `${SUPPLIERS}`,

  // Lấy chi tiết nhà cung cấp
  getSupplierById: (id: string) => `${SUPPLIERS}/${id}`,

  // Cập nhật nhà cung cấp
  patchSupplier: (id: string) => `${SUPPLIERS}/${id}`,

  // Xoá nhà cung cấp
  deleteSupplier: (id: string) => `${SUPPLIERS}/${id}`,

  // Vô hiệu nhà cung cấp
  disableSupplier: (id: string) => `${SUPPLIERS}/${id}/disable`,

  // Kích hoạt nhà cung cấp
  enableSupplier: (id: string) => `${SUPPLIERS}/${id}/active`,

  // Xuất NCC
  exportSuppliers: `${SUPPLIERS}/export`,
};

export const warehouseEndpoints = {
  // Lấy danh sách kho
  getWarehouses: `${WAREHOUSES}`,

  // Tạo mới kho
  postWarehouse: `${WAREHOUSES}`,

  // Lấy chi tiết kho
  getWarehouseById: (id: string) => `${WAREHOUSES}/${id}`,

  // Cập nhật kho
  patchWarehouse: (id: string) => `${WAREHOUSES}/${id}`,

  // Xoá kho
  deleteWarehouse: (id: string) => `${WAREHOUSES}/${id}`,

  getWarehouseRouting: `${WAREHOUSES}/routing`,
};

export const warehouseTypeEndpoints = {
  // Lấy danh sách loại kho
  getWarehouseTypes: `${WAREHOUSE_TYPES}`,

  // Tạo mới loại kho
  postWarehouseType: `${WAREHOUSE_TYPES}`,

  // Lấy chi tiết loại kho
  getWarehouseTypeById: (id: string) => `${WAREHOUSE_TYPES}/${id}`,

  // Cập nhật loại kho
  patchWarehouseTypeById: (id: string) => `${WAREHOUSE_TYPES}/${id}`,

  // Xoá loại kho
  deleteWarehouseType: (id: string) => `${WAREHOUSE_TYPES}/${id}`,
};

export const authorityEndpoints = {
  // Lấy danh sách quyền
  getAuthorities: `${AUTHORITIES}`,
};

export const groupEndpoints = {
  // Lấy danh sách nhóm quyền
  getGroups: `${GROUPS}`,

  // Tạo mới nhóm quyền
  postGroup: `${GROUPS}`,

  // Lấy chi tiết nhóm quyền
  getGroupById: (id: string) => `${GROUPS}/${id}`,

  // Cập nhật nhóm quyền
  patchGroup: (id: string) => `${GROUPS}/${id}`,

  // Xoá nhóm quyền
  deleteGroup: (id: string) => `${GROUPS}/${id}`,

  // Thêm người dùng vào nhóm quyền
  postGroupUser: (action: GROUP_ACTION) => `${GROUPS}/users?action=${action}`,
};

export const inventoryEndpoints = {
  // Lấy danh sách nguyên liệu trong kho
  getInventoryMaterials: `${INVENTORIES}/material-mix`,
};
export const incomingInvoiceEndpoints = {
  // Lấy danh sách hóa đơn đầu vào
  getIncomingInvoices: `${INCOMING_INVOICES}`,

  // Tạo mới hóa đơn đầu vào
  postIncomingInvoice: `${INCOMING_INVOICES}`,

  // Lấy chi tiết hóa đơn đầu vào
  getIncomingInvoiceById: (id: string) => `${INCOMING_INVOICES}/${id}`,

  // Cập nhật hóa đơn đầu vào
  patchIncomingInvoice: (id: string) => `${INCOMING_INVOICES}/${id}`,

  // Xoá hóa đơn đầu vào
  deleteIncomingInvoice: (id: string) => `${INCOMING_INVOICES}/${id}`,

  // Lấy mã hóa đơn tiếp theo
  getNextInvoiceNo: `${INCOMING_INVOICES}/next-invoice-no`,
};

export const itemsEndpoints = {
  // Lấy danh sách vật tư
  getItems: `${ITEMS}`,

  // Tạo mới vật tư
  postItem: `${ITEMS}`,

  // Lấy chi tiết vật tư
  getItemById: (id: string) => `${ITEMS}/${id}`,

  // Cập nhật vật tư
  patchItem: (id: string) => `${ITEMS}/${id}`,

  // Xoá vật tư
  deleteItem: (id: string) => `${ITEMS}/${id}`,

  // Vô hiệu vật tư
  disableItem: (id: string) => `${ITEMS}/${id}/disable`,

  // Kích hoạt vật tư
  enableItem: (id: string) => `${ITEMS}/${id}/active`,

  // Xuất file vật tư
  exportItems: `${ITEMS}/export`,

  // Get vật tư có % đạm
  getItemsPercentProtein: `${ITEMS}/percent-protein`,
};

export const itemCategoriesEndpoints = {
  // Lấy danh sách loại vật tư
  getItemCategories: `${ITEM_CATEGORIES}`,

  // Tạo mới loại vật tư
  postItemCategory: `${ITEM_CATEGORIES}`,

  // Lấy chi tiết loại vật tư
  getItemCategoryById: (id: string) => `${ITEM_CATEGORIES}/${id}`,

  // Cập nhật loại vật tư
  patchItemCategory: (id: string) => `${ITEM_CATEGORIES}/${id}`,

  // Xoá loại vật tư
  deleteItemCategory: (id: string) => `${ITEM_CATEGORIES}/${id}`,
};

export const accountEndpoints = {
  // Tạo mới tài khoản
  createAccount: `${REGISTER}`,

  // Lấy trạng thái tài khoản
  getAccountsStatuses: `${ACCOUNTS}/check`,

  // Lấy chi tiết tài khoản
  getAccountById: (id: string) => `${ACCOUNTS}/${id}`,

  // Chuyển trạng thái tài khoản
  toggleActiveAccount: (id: string) => `${ACCOUNTS}/${id}/toggle-active`,
  // /services/masierp/api/admin/users/M0020
  updateAccount: (id: string) => `${ACCOUNTS}/update/${id}`,

  checkAccountExist: (id: string) => `${ACCOUNTS}/username/count/${id}`,

  setCompanyUser: (id: string) => `${ACCOUNTS}/${id}/update-companies`,

  setCompany: `${ACCOUNTS}/update-companies`,
  // /services/masierp/api/admin/users/M0020

  getAccountByUsername: (username: string) => `${USERS}/${username}`,

  changeAccountCompany: `${ACCOUNTS}/update-current-company`,
};

export const adminAdminUsersEndpoints = {
  // Thêm nhóm quyền vào người dùng
  postAdminUserGroups: (action: string) =>
    `${ADMIN_USERS}/groups?action=${action}`,

  // Lấy list quyền của người dùng
  getAdminUserGroups: (id: string) => `${ADMIN_USERS}/${id}/groups`,
};
export const suppliesRequestEndpoints = {
  // Lấy danh sách đề nghị thu mua
  getSuppliesRequests: `${SUPPLIES_REQUESTS}`,
  // Lay danh sach suppliers type
  getSuppliesRequestTypes: '/services/masilogistics/api/supplies-request-types',
  // Tạo mới đề nghị thu mua
  postSuppliesRequest: `${SUPPLIES_REQUESTS}`,

  // Lấy chi tiết đề nghị thu mua
  getSuppliesRequestById: (id: string) => `${SUPPLIES_REQUESTS}/${id}`,

  // Cập nhật đề nghị thu mua
  patchSuppliesRequest: (id: string) => `${SUPPLIES_REQUESTS}/${id}`,

  // Xoá đề nghị thu mua
  deleteSuppliesRequest: (id: string) => `${SUPPLIES_REQUESTS}/${id}`,

  // xét duyệt đề nghị thu mua
  patchSuppliesRequestReview: (id: string) =>
    `${SUPPLIES_REQUESTS}/approved-review/${id}`,

  patchSuppliesRequestApprovalSign: (id: string) =>
    `${SUPPLIES_REQUESTS}/approved/${id}`,

  patchSuppliesRequestRejectSign: (id: string) =>
    `${SUPPLIES_REQUESTS}/${id}/reject`,

  patchSuppliesRequestCancel: (id: string) =>
    `${SUPPLIES_REQUESTS}/${id}/cancel`,

  exportExcelSuppliesRequest: () => `${SUPPLIES_REQUESTS}/export`,

  nextCodeSuppliesRequest: () => `${SUPPLIES_REQUESTS}/code/next`,
};

export const typeRequestApproveEndpoints = {
  // Lấy danh sách loại yêu cầu xét duyệt
  getTypeRequestApproves: `${TYPE_REQUEST_APPROVAL}`,

  // Tạo mới loại yêu cầu xét duyệt
  postTypeRequestApprove: `${TYPE_REQUEST_APPROVAL}`,

  // Lấy chi tiết loại yêu cầu xét duyệt
  getTypeRequestApproveById: (id: string) => `${TYPE_REQUEST_APPROVAL}/${id}`,

  // Cập nhật loại yêu cầu xét duyệt
  patchTypeRequestApprove: (id: string) => `${TYPE_REQUEST_APPROVAL}/${id}`,

  // Xoá loại yêu cầu xét duyệt
  deleteTypeRequestApprove: (id: string) => `${TYPE_REQUEST_APPROVAL}/${id}`,
};

export const paymentRequestEndpoins = {
  // Lấy danh sách đề nghị thanh toán.
  getPaymentRequests: `${PAYMENT_REQUESTS}`,

  // Tạo mới đề nghị thanh toán.
  postPaymentRequest: `${PAYMENT_REQUESTS}`,

  // Lấy chi tiết đề nghị thanh toán.
  getPaymentRequestById: (id: string) => `${PAYMENT_REQUESTS}/${id}`,

  // Cập nhật đề nghị thanh toán.
  patchPaymentRequest: (id: string) => `${PAYMENT_REQUESTS}/${id}`,

  // Xoá đề nghị thanh toán.
  deletePaymentRequest: (id: string) => `${PAYMENT_REQUESTS}/${id}`,

  // Lấy code của đề nghị thanh toán
  getPaymentRequestAutoGeneratedCode: (type: PAYMENT_REQUEST_TYPE) =>
    `${PAYMENT_REQUESTS}/next-code?type=${type}`,

  // Hủy đề nghị thanh toán.
  cancelPaymentRequest: (id: string) => `${PAYMENT_REQUESTS}/${id}/cancel`,

  // Trình duyệt đề nghị thanh toán.
  sendApprovePaymentRequest: (id: string) =>
    `${PAYMENT_REQUESTS}/${id}/send-approve`,

  // Xuất excel đề nghị thanh toán.
  exportExcelPaymentRequest: () => `${PAYMENT_REQUESTS}/export?download=true`,

  // Duyệt yêu cầu đề nghị thanh toán
  approvePaymentRequest: (id: string) => `${PAYMENT_REQUESTS}/${id}/approve`,

  // Từ chối yêu cầu đề nghị thanh toán
  rejectPaymentRequest: (id: string) => `${PAYMENT_REQUESTS}/${id}/reject`,
};

export const supplierGroupsEndpoints = {
  // Lấy danh sách nhóm nhà cung cấp
  getSupplierGroups: `${SUPPLIER_GROUPS}`,

  // Tạo mới nhóm nhà cung cấp
  postSupplierGroup: `${SUPPLIER_GROUPS}`,

  // Lấy chi tiết nhóm nhà cung cấp
  getSupplierGroupById: (id: string) => `${SUPPLIER_GROUPS}/${id}`,

  // Cập nhật nhóm nhà cung cấp
  patchSupplierGroup: (id: string) => `${SUPPLIER_GROUPS}/${id}`,

  // Xoá nhóm nhà cung cấp
  deleteSupplierGroup: (id: string) => `${SUPPLIER_GROUPS}/${id}`,
};

export const contactTypesEndpoints = {
  // Lấy danh sách loại liên hệ
  getContactTypes: `${CONTACT_TYPE}`,

  // Tạo mới loại liên hệ
  postContactType: `${CONTACT_TYPE}`,

  // Lấy chi tiết loại liên hệ
  getContactTypeById: (id: string) => `${CONTACT_TYPE}/${id}`,

  // Cập nhật loại liên hệ
  patchContactType: (id: string) => `${CONTACT_TYPE}/${id}`,

  // Xoá loại liên hệ
  deleteContactType: (id: string) => `${CONTACT_TYPE}/${id}`,
};

export const vatRatesEndpoints = {
  // Lấy danh sách tỉ giá
  getVatRates: `${VAT_RATES}`,

  // Tạo mới tỉ giá
  postVatRate: `${VAT_RATES}`,

  // Lấy chi tiết tỉ giá
  getVatRateById: (id: string) => `${VAT_RATES}/${id}`,

  // Cập nhật tỉ giá
  patchVatRate: (id: string) => `${VAT_RATES}/${id}`,

  // Xoá tỉ giá
  deleteVatRate: (id: string) => `${VAT_RATES}/${id}`,
};

export const factoryLogisticsEndpoints = {
  // Lấy danh sách nhà máy
  getFactories: `${FACTORIES_LOGISTICS}`,

  // Tạo mới nhà máy
  postFactory: `${FACTORIES_LOGISTICS}`,

  // Lấy chi tiết nhà máy
  getFactoryById: (id: string) => `${FACTORIES_LOGISTICS}/${id}`,

  // Cập nhật nhà máy
  patchFactory: (id: string) => `${FACTORIES_LOGISTICS}/${id}`,

  // Xoá nhà máy
  deleteFactory: (id: string) => `${FACTORIES_LOGISTICS}/${id}`,

  // Vô hiệu nhà máy
  disableFactory: (id: string) => `${FACTORIES_LOGISTICS}/${id}/disable`,

  // Kích hoạt nhà máy
  enableFactory: (id: string) => `${FACTORIES_LOGISTICS}/${id}/active`,
};

export const companyEndpoints = {
  // Lấy danh sách nhà máy
  getCompanies: `${COMPANIES}`,

  // Tạo mới công ty
  postCompany: `${COMPANIES}`,

  // Lấy chi tiết công ty
  getCompanyById: (id: string) => `${COMPANIES}/${id}`,

  // Cập nhật công ty
  patchCompany: (id: string) => `${COMPANIES}/${id}`,

  // Tiếp nhận công ty
  activeCompany: (id: string) => `${COMPANIES}/${id}/activate`,

  // Hoàn thành công ty
  inactiveCompany: (id: string) => `${COMPANIES}/${id}/deactivate`,
};

export const paymentDetails = {
  // Lấy danh sách chi tiết thanh toán.
  getPaymentDetails: `${PAYMENT_DETAILS}`,

  // Tạo mới chi tiết thanh toán.
  postPaymentDetails: `${PAYMENT_DETAILS}`,

  // Lấy chi tiết chi tiết thanh toán.
  getPaymentDetailsById: (id: string) => `${PAYMENT_DETAILS}/${id}`,

  // Cập nhật chi tiết thanh toán.
  patchPaymentDetails: (id: string) => `${PAYMENT_DETAILS}/${id}`,

  // Xoá chi tiết thanh toán.
  deletePaymentDetails: (id: string) => `${PAYMENT_DETAILS}/${id}`,
};

export const currenciesEndpoints = {
  // Lấy danh sách nhà cung cấp
  getCurrencies: `${CURRENCIES}`,

  // Tạo mới nhà cung cấp
  postCurrency: `${CURRENCIES}`,

  // Lấy chi tiết nhà cung cấp
  getCurrencyById: (id: string) => `${CURRENCIES}/${id}`,

  // Cập nhật nhà cung cấp
  patchCurrency: (id: string) => `${CURRENCIES}/${id}`,

  // Xoá nhà cung cấp
  deleteCurrency: (id: string) => `${CURRENCIES}/${id}`,

  // Vô hiệu nhà cung cấp
  disableCurrency: (id: string) => `${CURRENCIES}/${id}/disable`,

  // Kích hoạt nhà cung cấp
  enableCurrency: (id: string) => `${CURRENCIES}/${id}/active`,
};

export const inventoriesStorageEndpoints = {
  // Lấy danh sách nhập kho
  getInventoriesStorage: `${INVENTORIES_STORAGE}`,

  // Tạo mới nhập kho
  postInventoriesStorage: `${INVENTORIES_STORAGE}`,

  // Lấy chi tiết nhập kho
  getInventoriesStorageById: (id: string) => `${INVENTORIES_STORAGE}/${id}`,

  // Cập nhật nhập kho
  patchInventoriesStorage: (id: string) => `${INVENTORIES_STORAGE}/${id}`,

  // Xoá nhập kho
  deleteInventoriesStorage: (id: string) =>
    `${INVENTORIES_STORAGE}/${id}/cancel-inventories`,

  // Xuất excel nhập kho
  exportInventoriesStorage: `${INVENTORIES_STORAGE}/export`,

  // Trình duyệt nhập kho
  reqReviewInventoriesStorage: (id: string) =>
    `${INVENTORIES_STORAGE}/${id}/request-review`,

  // Duyệt/ Từ chối nhập kho
  reviewInventoriesStorage: (id: string) =>
    `${INVENTORIES_STORAGE}/${id}/review`,

  // Generate next code phiếu nhập kho
  getInventoriesStorageNextCode: () => `${INVENTORIES_STORAGE}/code/next`,

  // xác nhận nhập kho
  confirmImportInventoriesStorage: (id: string) =>
    `${INVENTORIES_STORAGE}/${id}/confirm-inventories`,

  // Lấy tồn của sản phẩm
  getInventoriesStorageItems: `${INVENTORIES_STORAGE}-storages/items`,

  // Lấy tồn của sản phẩm
  getInventoriesStorageItemsAllByWarehouse: (warehouseId: string) =>
    `${INVENTORIES_STORAGE}-storages/items/${warehouseId}/all`,
};

export const inventoriesType = {
  getInventoriesType: `${INVENTORIES_TYPE}`,
};

export const supplierContractEndpoints = {
  // Lấy danh sách hợp đồng
  getSupplierContracts: `${SUPPLIER_CONTRACTS}`,

  // Tạo mới hợp đồng
  postSupplierContract: `${SUPPLIER_CONTRACTS}`,

  // Lấy chi tiết hợp đồng
  getSupplierContractById: (id: string) => `${SUPPLIER_CONTRACTS}/${id}`,

  // Cập nhật hợp đồng
  patchSupplierContract: (id: string) => `${SUPPLIER_CONTRACTS}/${id}`,

  // Xoá hợp đồng
  deleteSupplierContract: (id: string) => `${SUPPLIER_CONTRACTS}/${id}/cancel`,

  // Xuất excel hợp đồng
  exportSupplierContract: `${SUPPLIER_CONTRACTS}/export`,

  // Trình duyệt hợp đồng
  reqReviewSupplierContract: (id: string) =>
    `${SUPPLIER_CONTRACTS}/${id}/send-approve`,

  // Duyệt hợp đồng
  approveSupplierContract: (id: string) =>
    `${SUPPLIER_CONTRACTS}/${id}/approve`,

  changeStatusSupplierContract: (id: string) =>
    `${SUPPLIER_CONTRACTS}/${id}/update-status`,

  // Từ chối hợp đồng
  rejectSupplierContract: (id: string) => `${SUPPLIER_CONTRACTS}/${id}/reject`,

  // Trình duyệt thanh lý hợp đồng
  reqReviewLiquidationSupplierContract: (id: string) =>
    `${SUPPLIER_CONTRACTS}/${id}/send-liquidation`,

  // Duyệt thanh lý hợp đồng
  approveLiquidationSupplierContract: (id: string) =>
    `${SUPPLIER_CONTRACTS}/${id}/approve-liquidation`,

  // Từ chối hợp đồng
  rejectLiquidationSupplierContract: (id: string) =>
    `${SUPPLIER_CONTRACTS}/${id}/reject-liquidation`,

  // Generate next code phiếu hợp đồng
  getSupplierContractNextCode: () => `${SUPPLIER_CONTRACTS}/next-code`,
};

export const stocktakingEndpoints = {
  // Lấy danh sách kiểm kê
  getStocktaking: `${STOCKTAKING}`,

  // Tạo mới kiểm kê
  postStocktaking: `${STOCKTAKING}`,

  // Lấy chi tiết kiểm kê
  getStocktakingById: (id: string) => `${STOCKTAKING}/${id}`,

  // Cập nhật kiểm kê
  patchStocktaking: (id: string) => `${STOCKTAKING}/${id}`,

  // Xoá kiểm kê
  deleteStocktaking: (id: string) => `${STOCKTAKING}/${id}/cancel-inventories`,

  // Xuất excel kiểm kê
  exportStocktaking: `${STOCKTAKING}/export`,

  // Trình duyệt kiểm kê
  reqReviewStocktaking: (id: string) => `${STOCKTAKING}/${id}/request-review`,

  // Duyệt/ Từ chối kiểm kê
  reviewStocktaking: (id: string) => `${STOCKTAKING}/${id}/review`,

  // Generate next code phiếu kiểm kê
  getStocktakingNextCode: () => `${STOCKTAKING}/code/next`,
};

export const itemTypesEndpoints = {
  // Lấy danh sách kiểm kê
  getItemTypes: `${ITEM_TYPES}`,
};

export const callCenterEndpoints = {
  // Lấy thông tin CS
  getCallCenters: `${CALL_CENTER}`,

  // Tạo mới CS
  postCallCenter: `${CALL_CENTER}`,

  // Lấy chi tiết CS
  getCallCenterById: (id: string) => `${CALL_CENTER}/${id}`,

  // Cập nhật CS
  patchCallCenter: (id: string) => `${CALL_CENTER}/${id}`,

  // Xoá CS
  deleteCallCenter: (id: string) => `${CALL_CENTER}/${id}`,

  // Tiếp nhận CS
  approveCallCenter: (id: string) => `${CALL_CENTER}/${id}/processing`,

  // Hoàn thành CS
  completeCallCenter: (id: string) => `${CALL_CENTER}/${id}/completed`,

  // Đóng CS
  closeCallCenter: (id: string) => `${CALL_CENTER}/${id}/closed`,
};

export const transferAssetsEndpoints = {
  exportTransferAssets: `${TRANSFER_ASSETS}/export`,
  // Lấy thông tin TransferAssets:
  getTransferAssets: `${TRANSFER_ASSETS}`,

  // Tạo mới TransferAssets:
  postTransferAssets: `${TRANSFER_ASSETS}`,

  // Lấy chi tiết TransferAssets:
  getTransferAssetsById: (id: string) => `${TRANSFER_ASSETS}/${id}`,

  // Cập nhật TransferAssets:
  patchTransferAssets: (id: string) => `${TRANSFER_ASSETS}/${id}`,

  // Xoá TransferAssets:
  deleteTransferAssets: (id: string) => `${TRANSFER_ASSETS}/${id}`,

  nextCodeTransferAssets: () => `${TRANSFER_ASSETS}/code/next`,
  disableTransferAssets: (id: string) => `${TRANSFER_ASSETS}/${id}/cancel`,
  enableTransferAssets: (id: string) => `${TRANSFER_ASSETS}/${id}/confirm`,
};

export const transactionTypeEndpoints = {
  getTransactionType: `${TRANSACTION_TYPE}`,
};

export const inventoriesStorageNewEndpoints = {
  // Lấy danh sách nhập kho
  getInventoriesStorage: `${INVENTORIES_STORAGE_NEW}`,
  getDepreciationInventoriesStorage: `${INVENTORIES_STORAGE_NEW}/depreciation`,
  getFishMealInventoriesStorage: `${INVENTORIES_STORAGE_NEW}/fish-meal`,
  exportInventoriesStorageXlsx: `${INVENTORIES_STORAGE_NEW}/export`,
  updateInventoriesStorage: (id: string) => `${INVENTORIES_STORAGE_NEW}/${id}`,
  getInventoriesStorageById: (id: string) => `${INVENTORIES_STORAGE_NEW}/${id}`,
};
