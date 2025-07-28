export const PATH = {
  // TIME KEEPING
  TIME_SHEET: '/time-sheet',
  TIME_SHEET_BULK: '/time-sheet/bulk',
  TIME_SHEET_BULK_APPROVAL: '/time-sheet/bulk/approval',
  TIME_SHEET_EXPLANATION: '/time-sheet/explanation',
  TIME_SHEET_EXPLANATION_VIOLATION: '/time-sheet/explanation/:id/violation',
  TIME_SHEET_VIOLATION: '/time-sheet/violation',
  LEAVE_REQUEST: '/leave-request',

  // PRODUCTION
  PRODUCTION: '/production',
  PRODUCTION_STANDARD: '/production/standard',
  PRODUCTION_COMMAND: '/production/command',
  PRODUCTION_PROCESS: '/production/process',
  PRODUCTION_PROCESS_CREATE: '/production/process/create',
  PRODUCTION_PROCESS_UPDATE: '/production/process/:id/update',
  PRODUCTION_PROCESS_DETAIL: '/production/process/:id/:workItemId',
  PRODUCTION_PROCESS_TEMPLATE: '/production/process/:id/template',

  PRODUCTION_MANUFACTURE_ORDER_BY_ORDER: '/production/manufacture-order/order',
  PRODUCTION_MANUFACTURE_ORDER_BY_ORDER_CREATE:
    '/production/manufacture-order/order/create',
  PRODUCTION_MANUFACTURE_ORDER_BY_ORDER_UPDATE:
    '/production/manufacture-order/order/:id/update',

  PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD:
    '/production/manufacture-order/standard',
  PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD_CREATE:
    '/production/manufacture-order/standard/create',
  PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD_UPDATE:
    '/production/manufacture-order/standard/:id/update',

  PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION:
    '/production/process/:id/:workItemId/machine-operation-monitoring-template', // Biểu mẫu giám sát hoạt động máy
  PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_UPDATE:
    '/production/process/:id/:workItemId/machine-operation-monitoring-template/update', // Cập nhật biểu mẫu giám sát hoạt động máy
  PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_DETAIL:
    '/production/process/:id/:workItemId/machine-operation-monitoring-template/detail', // Chi tiết biểu mẫu giám sát hoạt động máy

  PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH:
    '/production/process/:id/:workItemId/magnet-mesh-test-template', // Biểu mẫu kiểm tra nam châm và lưới
  PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_UPDATE:
    '/production/process/:id/:workItemId/magnet-mesh-test-template/update', // Cập nhật biểu mẫu kiểm tra nam châm và lưới
  PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_DETAIL:
    '/production/process/:id/:workItemId/magnet-mesh-test-template/detail', // Chi tiết biểu mẫu kiểm tra nam châm và lưới

  PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT:
    '/production/process/:id/:workItemId/material-receipt-monitoring-template', // Biểu mẫu giám sát tiếp nhận nguyên liệu
  PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_UPDATE:
    '/production/process/:id/:workItemId/material-receipt-monitoring-template/update', // Cập nhật biểu mẫu giám sát tiếp nhận nguyên liệu
  PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_DETAIL:
    '/production/process/:id/:workItemId/material-receipt-monitoring-template/detail', // Chi tiết biểu mẫu giám sát tiếp nhận nguyên liệu

  PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING:
    '/production/process/:id/:workItemId/monitoring-steaming-drying-template', // Biểu mẫu giám sát công đoạn hấp - sấy
  PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_UPDATE:
    '/production/process/:id/:workItemId/monitoring-steaming-drying-template/update',
  PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_DETAIL:
    '/production/process/:id/:workItemId/monitoring-steaming-drying-template/detail',

  PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING:
    '/production/process/:id/:workItemId/report-mixing-fishmeal-template', // Báo cáo trộn sản phẩm bột cá
  PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_UPDATE:
    '/production/process/:id/:workItemId/report-mixing-fishmeal-template/update',
  PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_DETAIL:
    '/production/process/:id/:workItemId/report-mixing-fishmeal-template/detail',

  PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING:
    '/production/process/:id/:workItemId/selecting-adding-additives-template', // Biểu mẫu lựa và bổ sung phụ gia,
  PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_UPDATE:
    '/production/process/:id/:workItemId/selecting-adding-additives-template/update',
  PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_DETAIL:
    '/production/process/:id/:workItemId/selecting-adding-additives-template/detail', // Chi tiết biểu mẫu lựa và bổ sung phụ gia

  PRODUCTION_QUALITY: '/production/quality',
  PRODUCTION_QUALITY_CREATE: '/production/quality/create',
  PRODUCTION_QUALITY_UPDATE: '/production/quality/:id/update',
  PRODUCTION_QUALITY_CANCEL: '/production/quality/:id/cancel',
  PRODUCTION_WORK_CENTERS: '/production/work-centers',
  PRODUCTION_ROUTINGS: '/production/routings',
  PRODUCTION_MAINTENANCE: '/production/maintenance',
  PRODUCTION_PACKAGES: '/production/packages',

  // BUSINESS
  CUSTOMERS: '/customers',
  CUSTOMERS_DISPOSED: '/customers/disposed',
  CUSTOMERS_TRANSFER: '/customers/transfer',
  PRICE_LIST: '/price-list',
  PRICE_LIST_KIM_LONG: '/price-list/kim-long',
  PRICE_LIST_MMS: '/price-list/mms',
  PRICE_LIST_KIM_LONG_UPDATE: '/price-list/kim-long/:id/update',
  PRICE_LIST_MMS_UPDATE: '/price-list/mms/:id/update',
  PRICE_LIST_DETAIL_KIM_LONG: '/price-list/kim-long/:id',
  PRICE_LIST_DETAIL_MMS: '/price-list/mms/:id',
  PRICE_LIST_CREATE_KIM_LONG: '/price-list/:id/kim-long',
  PRICE_LIST_UPDATE_KIM_LONG: '/price-list/:id/kim-long/:detailId',
  PRICE_LIST_CREATE_MMS: '/price-list/:id/mms',
  PRICE_LIST_UPDATE_MMS: '/price-list/:id/mms/:detailId',
  CONTRACTS: '/contracts',
  CONTRACTS_DELETED: '/contracts/deleted',
  ORDERS: '/orders',
  PURCHASE: '/purchase',

  // HUMAN RESOURCES
  EMPLOYEES: '/employees',
  EMPLOYEES_CREATE: '/employees/create',
  EMPLOYEES_UPDATE: '/employees/:id/update',
  EMPLOYEES_CHANGE_LOGS: '/employees/:id/change-logs',
  ANNUAL_LEAVE: '/annual-leave',
  OFFICES: '/offices',
  LEAVE_REGISTER: '/leave-register',
  RECRUITMENT: '/recruitment',
  RECRUITMENT_CANDIDATES: '/recruitment/candidates',
  DOCUMENTARY: '/documentary',
  UNIFORM: '/uniform',
  UNIFORM_ORDERS: '/uniform/orders',
  UNIFORM_EXPORTS: '/uniform/exports',
  REPORT: '/report',
  REPORT_UNIFORMS_EXPIRED: '/report/uniforms-expired',
  REPORT_UNIFORMS_EXPORTS: '/report/uniforms-exports',
  REPORT_UNIFORMS_REIMBURSEMENT: '/report/uniforms-reimbursement',

  // REQUEST
  REQUEST: '/request',

  // REQUEST_PAYMENT
  REQUEST_PAYMENT: '/request/dashboard',

  PAYMENT_REQUEST: '/request/payment-request',
  PAYMENT_REQUEST_DETAIL: '/request/payment-request/:id',
  ADVANCE_REQUEST: '/request/advance-request',
  ADVANCE_REQUEST_DETAIL: '/request/advance-request/:id',
  REFUND_REQUEST: '/request/refund-request',
  REFUND_REQUEST_DETAIL: '/request/refund-request/:id',

  REPORT_EMPLOYEE_EXPIRING_CONTRACT: '/report/employee-expiring-contract',
  REPORT_LEAVE_REGIME: '/report/leave-regime',
  REPORT_RECRUITMENT: '/report/recruitment',
  REPORT_HUMAN_RESOURCE_CHANGE: '/report/human-resource-change',

  // LOGISTICS
  LOGISTICS: '/logistics',
  MATERIAL_PROPOSAL: '/logistics/material-proposal',
  MATERIAL_PROPOSAL_CHANGE_LOGS: '/logistics/material-proposal/:id/change-logs',
  INCOMING_INVOICE: '/incoming-invoice',
  INCOMING_INVOICE_FORM: '/incoming-invoice/:type',
  INCOMING_INVOICE_FORM_DETAIL: '/incoming-invoice/:type/:id',
  IMPORT_FORM: 'import-form',
  NORMAL_FORM: 'form',
  SUPPLIERS: '/logistics/suppliers',
  SUPPLIERS_CREATE: '/logistics/suppliers/create',
  SUPPLIERS_UPDATE: '/logistics/suppliers/:id/update',
  SUPPLIERS_GROUPS: '/logistics/suppliers-groups',
  SUPPLIES_REQUESTS: '/supplies-requests',
  SUPPLIES_REQUESTS_FORM: '/supplies-requests/form',
  SUPPLIES_REQUESTS_FORM_DETAIL: '/supplies-requests/form/:id',
  SUPPLIES: '/logistics/supplies', // Vật tư
  SUPPLIES_CREATE: '/logistics/supplies/create',
  SUPPLIES_UPDATE: '/logistics/supplies/:id/update',
  FACTORIES: '/logistics/factories', // Ql Nhà máy
  FACTORIES_CREATE: '/logistics/factories/create',
  FACTORIES_UPDATE: '/logistics/factories/:id/update',
  INVENTORIES_STORAGE: '/logistics/inventories-storage',
  INVENTORIES_STORAGE_CREATE: '/logistics/inventories-storage/create',
  INVENTORIES_STORAGE_UPDATE: '/logistics/inventories-storage/:id/update',

  INVENTORIES_STORAGE_EXPORT: '/logistics/inventories-storage-export',
  INVENTORIES_STORAGE_EXPORT_CREATE:
    '/logistics/inventories-storage-export/create',
  INVENTORIES_STORAGE_EXPORT_UPDATE:
    '/logistics/inventories-storage-export/:id/update',

  // SETTINGS
  UOM: '/uom',
  UOM_GROUP: '/uom/groups',
  UOM_GROUP_DETAIL: '/uom/groups/:id',
  UNIFORM_SETTINGS: '/uniform/settings',
  WAREHOUSES: '/warehouses',
  WAREHOUSE_TYPES: '/warehouses/types',
  AUTHORITIES: '/permissions',
  AUTHORITIES_GROUPS: '/permissions/groups',
  AUTHORITIES_GROUPS_DETAIL: '/permissions/groups/:id',
  AUTHORITIES_USERS: '/permissions/users',

  ITEMS: '/items-settings',
  ITEM_ATTRIBUTES: '/items-settings/attributes',

  // CUSTOMER SERVICES
  CUSTOMER_SERVICES: '/customer-services',
  CALL_CENTER: '/customer-services/call-center',
  CALL_CENTER_CREATE: '/customer-services/call-center/create',
  CALL_CENTER_UPDATE: '/customer-services/call-center/:id/update',
  COMPLAIN: '/customer-services/complain',
  COMPLAIN_CREATE: '/customer-services/complain/create',
  COMPLAIN_UPDATE: '/customer-services/complain/:id/update',

  //ASSET
  ASSET: '/asset',
  ASSET_CREATE: '/asset/form',
  ASSET_CREATE_DETAIL: '/asset/form/:id',
  ALLOCATION: '/asset/allocation',
  ALLOCATION_CREATE: '/asset/allocation/form',
  ALLOCATION_CREATE_DETAIL: '/asset/allocation/form/:id',
  LIQUIDATION: '/asset/liquidation',
  LIQUIDATION_CREATE: '/asset/liquidation/form',
  LIQUIDATION_CREATE_DETAIL: '/asset/liquidation/form/:id',
  TRANSFER_ASSETS: '/asset/transfer-assets',
  TRANSFER_ASSETS_CREATE: '/asset/transfer-assets/create',
  TRANSFER_ASSETS_DETAIL: '/asset/transfer-assets/:id',
  DEPRECIATION: '/asset/depreciation',
  DEPRECIATION_CREATE: '/asset/depreciation/form',
  DEPRECIATION_CREATE_DETAIL: '/asset/depreciation/form/:id',

  DELIVERY_HISTORY: '/delivery-history',
  DELIVERY_HISTORY_DETAIL: '/delivery-history/:id',

  DELIVERY_SCHEDULE: '/delivery-schedule',

  SUPPLIER_CONTRACTS: '/logistics/supplier-contracts',
  SUPPLIER_CONTRACTS_CREATE: '/logistics/supplier-contracts/create',
  SUPPLIER_CONTRACTS_UPDATE: '/logistics/supplier-contracts/:id/update',

  STOCKTAKING: '/logistics/stocktaking',
  STOCKTAKING_CREATE: '/logistics/stocktaking/create',
  STOCKTAKING_UPDATE: '/logistics/stocktaking/:id/update',

  // MACHINERY_EQUIMENT
  TECHNICAL: '/technical',
  MACHINERY_EQUIPMENT: '/technical/machinery-equipment',
  MACHINERY_EQUIPMENT_CREATE: '/technical/machinery-equipment/create',
  MACHINERY_EQUIPMENT_UPDATE: '/technical/machinery-equipment/:id/update',

  COMPANY: '/company',
  COMPANY_CREATE: '/company/create',
  COMPANY_UPDATE: '/company/:id/update',
};
