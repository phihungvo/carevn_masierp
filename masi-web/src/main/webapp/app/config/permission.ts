import { PATH } from "app/constants/path";

export const permissions = [
    {
        key: "REQUEST_PAYMENT",
        name: "Phiếu đề nghị",
        path: PATH.REQUEST_PAYMENT,
        actions: {
            VIEW: "Phiếu đề nghị",
            CREATE: "Tạo phiếu đề nghị",
            EDIT: "Chỉnh sửa phiếu đề nghị",
            EXPORT: "Xuất phiếu đề nghị"
        },
        REQUEST_PAYMENT: {
            VIEW: "Phiếu đề nghị",
            CREATE: "Tạo phiếu đề nghị",
            EDIT: "Chỉnh sửa phiếu đề nghị",
            EXPORT: "Xuất phiếu đề nghị"
        }
    },
    {
        key: "SUPPLIES_REQUESTS",
        name: "Đề xuất mua hàng",
        path: PATH.SUPPLIES_REQUESTS,
        actions: {
            VIEW: "Đề xuất mua hàng",
            CREATE: "Tạo đề xuất mua hàng",
            EDIT: "Chỉnh sửa đề xuất mua hàng",
            EXPORT: "Xuất đề xuất mua hàng"
        },
        SUPPLIES_REQUESTS: {
            VIEW: "Đề xuất mua hàng",
            CREATE: "Tạo đề xuất mua hàng",
            EDIT: "Chỉnh sửa đề xuất mua hàng",
            EXPORT: "Xuất đề xuất mua hàng"
        }
    },
    {
        key: "TIME_SHEET",
        name: "Chấm công",
        path: PATH.TIME_SHEET,
        actions: {
            VIEW: "Chấm công",
            CREATE: "Tạo chấm công",
            EDIT: "Chỉnh sửa chấm công",
            EXPORT: "Xuất chấm công"
        },
        TIME_SHEET: {
            VIEW: "Chấm công",
            CREATE: "Tạo chấm công",
            EDIT: "Chỉnh sửa chấm công",
            EXPORT: "Xuất chấm công"
        }
    },
    {
        key: "TIME_SHEET_VIOLATION",
        path: PATH.TIME_SHEET_VIOLATION,
        name: "Vi phạm chấm công",
        actions: {
            VIEW: "Vi phạm chấm công",
            CREATE: "Tạo vi phạm chấm công",
            EDIT: "Chỉnh sửa vi phạm chấm công",
            EXPORT: "Xuất vi phạm chấm công"
        },
        TIME_SHEET_VIOLATION: {
            VIEW: "Vi phạm chấm công",
            CREATE: "Tạo vi phạm chấm công",
            EDIT: "Chỉnh sửa vi phạm chấm công",
            EXPORT: "Xuất vi phạm chấm công"
        }
    },
    {
        key: "TIME_SHEET_EXPLANATION",
        path: PATH.TIME_SHEET_EXPLANATION,
        name: "Giải trình chấm công",
        actions: {
            VIEW: "Giải trình chấm công",
            CREATE: "Tạo giải trình chấm công",
            EDIT: "Chỉnh sửa giải trình chấm công",
            EXPORT: "Xuất giải trình chấm công"
        },
        TIME_SHEET_EXPLANATION: {
            VIEW: "Giải trình chấm công",
            CREATE: "Tạo giải trình chấm công",
            EDIT: "Chỉnh sửa giải trình chấm công",
            EXPORT: "Xuất giải trình chấm công"
        }
    },
    {
        key: "TIME_SHEET_BULK_APPROVAL",
        path: PATH.TIME_SHEET_BULK_APPROVAL,
        name: "Duyệt chấm công tháng",
        actions: {
            VIEW: "Duyệt chấm công tháng",
            CREATE: "Tạo duyệt chấm công tháng",
            EDIT: "Chỉnh sửa duyệt chấm công tháng",
            EXPORT: "Xuất duyệt chấm công tháng"
        },
        TIME_SHEET_BULK_APPROVAL: {
            VIEW: "Duyệt chấm công tháng",
            CREATE: "Tạo duyệt chấm công tháng",
            EDIT: "Chỉnh sửa duyệt chấm công tháng",
            EXPORT: "Xuất duyệt chấm công tháng"
        }
    },
    {
        key: "LEAVE_REQUEST",
        path: PATH.LEAVE_REQUEST,
        name: "Nghỉ phép",
        actions: {
            VIEW: "Nghỉ phép",
            CREATE: "Tạo đơn nghỉ phép",
            EDIT: "Chỉnh sửa đơn nghỉ phép",
            EXPORT: "Xuất đơn nghỉ phép"
        },
        LEAVE_REQUEST: {
            VIEW: "Nghỉ phép",
            CREATE: "Tạo đơn nghỉ phép",
            EDIT: "Chỉnh sửa đơn nghỉ phép",
            EXPORT: "Xuất đơn nghỉ phép"
        }
    },
    {
        key: "CUSTOMERS",
        path: PATH.CUSTOMERS,
        name: "Danh sách khách hàng",
        actions: {
            VIEW: "Danh sách khách hàng",
            CREATE: "Tạo khách hàng",
            EDIT: "Chỉnh sửa khách hàng",
            EXPORT: "Xuất danh sách khách hàng"
        },
        CUSTOMERS: {
            VIEW: "Danh sách khách hàng",
            CREATE: "Tạo khách hàng",
            EDIT: "Chỉnh sửa khách hàng",
            EXPORT: "Xuất danh sách khách hàng"
        }
    },
    {
        key: "CUSTOMERS_DISPOSED",
        path: PATH.CUSTOMERS_DISPOSED,
        name: "Danh sách vô hiệu hoá",
        actions: {
            VIEW: "Danh sách vô hiệu hoá",
            CREATE: "Tạo khách hàng vô hiệu hoá",
            EDIT: "Chỉnh sửa khách hàng vô hiệu hoá",
            EXPORT: "Xuất danh sách khách hàng vô hiệu hoá"
        },
        CUSTOMERS_DISPOSED: {
            VIEW: "Danh sách vô hiệu hoá",
            CREATE: "Tạo khách hàng vô hiệu hoá",
            EDIT: "Chỉnh sửa khách hàng vô hiệu hoá",
            EXPORT: "Xuất danh sách khách hàng vô hiệu hoá"
        }
    },
    {
        key: "PRICE_LIST",
        path: PATH.PRICE_LIST,
        name: "Bảng báo giá",
        actions: {
            VIEW: "Bảng báo giá",
            CREATE: "Tạo bảng báo giá",
            EDIT: "Chỉnh sửa bảng báo giá",
            EXPORT: "Xuất bảng báo giá"
        },
        PRICE_LIST: {
            VIEW: "Bảng báo giá",
            CREATE: "Tạo bảng báo giá",
            EDIT: "Chỉnh sửa bảng báo giá",
            EXPORT: "Xuất bảng báo giá"
        }
    },
    {
        key: "CONTRACTS",
        path: PATH.CONTRACTS,
        name: "Danh sách tất cả hợp đồng",
        actions: {
            VIEW: "Danh sách tất cả hợp đồng",
            CREATE: "Tạo hợp đồng",
            EDIT: "Chỉnh sửa hợp đồng",
            EXPORT: "Xuất hợp đồng"
        },
        CONTRACTS: {
            VIEW: "Danh sách tất cả hợp đồng",
            CREATE: "Tạo hợp đồng",
            EDIT: "Chỉnh sửa hợp đồng",
            EXPORT: "Xuất hợp đồng"
        }
    },
    {
        key: "CONTRACTS_DELETED",
        path: PATH.CONTRACTS_DELETED,
        name: "Danh sách hợp đồng đã xoá",
        actions: {
            VIEW: "Danh sách hợp đồng đã xoá",
            CREATE: "Tạo hợp đồng đã xoá",
            EDIT: "Chỉnh sửa hợp đồng đã xoá",
            EXPORT: "Xuất hợp đồng đã xoá"
        },
        CONTRACTS_DELETED: {
            VIEW: "Danh sách hợp đồng đã xoá",
            CREATE: "Tạo hợp đồng đã xoá",
            EDIT: "Chỉnh sửa hợp đồng đã xoá",
            EXPORT: "Xuất hợp đồng đã xoá"
        }
    },
    {
        key: "ORDERS",
        path: PATH.ORDERS,
        name: "Đơn đặt hàng",
        actions: {
            VIEW: "Đơn đặt hàng",
            CREATE: "Tạo đơn đặt hàng",
            EDIT: "Chỉnh sửa đơn đặt hàng",
            EXPORT: "Xuất đơn đặt hàng"
        },
        ORDERS: {
            VIEW: "Đơn đặt hàng",
            CREATE: "Tạo đơn đặt hàng",
            EDIT: "Chỉnh sửa đơn đặt hàng",
            EXPORT: "Xuất đơn đặt hàng"
        }
    },
    {
        key: "EMPLOYEES",
        path: PATH.EMPLOYEES,
        name: "Danh sách nhân viên",
        actions: {
            VIEW: "Danh sách NV",
            CREATE: "Tạo nhân viên",
            EDIT: "Chỉnh sửa nhân viên",
            EXPORT: "Xuất danh sách nhân viên"
        },
        EMPLOYEES: {
            VIEW: "Danh sách NV",
            CREATE: "Tạo nhân viên",
            EDIT: "Chỉnh sửa nhân viên",
            EXPORT: "Xuất danh sách nhân viên"
        }
    },
    {
        key: "OFFICES",
        path: PATH.OFFICES,
        name: "Văn phòng/Nhà máy",
        actions: {
            VIEW: "Văn phòng/Nhà máy",
            CREATE: "Tạo văn phòng",
            EDIT: "Chỉnh sửa văn phòng",
            EXPORT: "Xuất danh sách văn phòng"
        },
        OFFICES: {
            VIEW: "Văn phòng/Nhà máy",
            CREATE: "Tạo văn phòng",
            EDIT: "Chỉnh sửa văn phòng",
            EXPORT: "Xuất danh sách văn phòng"
        }
    },
    {
        key: "LEAVE_REGISTER",
        path: PATH.LEAVE_REGISTER,
        name: "Đăng ký nghỉ chế độ",
        actions: {
            VIEW: "Đăng ký nghỉ chế độ",
            CREATE: "Tạo đăng ký nghỉ chế độ",
            EDIT: "Chỉnh sửa đăng ký nghỉ chế độ",
            EXPORT: "Xuất đăng ký nghỉ chế độ"
        },
        LEAVE_REGISTER: {
            VIEW: "Đăng ký nghỉ chế độ",
            CREATE: "Tạo đăng ký nghỉ chế độ",
            EDIT: "Chỉnh sửa đăng ký nghỉ chế độ",
            EXPORT: "Xuất đăng ký nghỉ chế độ"
        }
    },
    {
        key: "RECRUITMENT",
        path: PATH.RECRUITMENT,
        name: "Yêu cầu tuyển dụng",
        actions: {
            VIEW: "Yêu cầu tuyển dụng",
            CREATE: "Tạo yêu cầu tuyển dụng",
            EDIT: "Chỉnh sửa yêu cầu tuyển dụng",
            EXPORT: "Xuất yêu cầu tuyển dụng"
        },
        RECRUITMENT: {
            VIEW: "Yêu cầu tuyển dụng",
            CREATE: "Tạo yêu cầu tuyển dụng",
            EDIT: "Chỉnh sửa yêu cầu tuyển dụng",
            EXPORT: "Xuất yêu cầu tuyển dụng"
        }
    },
    {
        key: "RECRUITMENT_CANDIDATES",
        path: PATH.RECRUITMENT_CANDIDATES,
        name: "Danh sách ứng viên",
        actions: {
            VIEW: "Danh sách ứng viên",
            CREATE: "Tạo ứng viên",
            EDIT: "Chỉnh sửa ứng viên",
            EXPORT: "Xuất danh sách ứng viên"
        },
        RECRUITMENT_CANDIDATES: {
            VIEW: "Danh sách ứng viên",
            CREATE: "Tạo ứng viên",
            EDIT: "Chỉnh sửa ứng viên",
            EXPORT: "Xuất danh sách ứng viên"
        }
    },
    {
        key: "DOCUMENTARY",
        path: PATH.DOCUMENTARY,
        name: "Công văn",
        actions: {
            VIEW: "Công văn",
            CREATE: "Tạo công văn",
            EDIT: "Chỉnh sửa công văn",
            EXPORT: "Xuất công văn"
        },
        DOCUMENTARY: {
            VIEW: "Công văn",
            CREATE: "Tạo công văn",
            EDIT: "Chỉnh sửa công văn",
            EXPORT: "Xuất công văn"
        }
    },
    {
        key: "UNIFORM",
        path: PATH.UNIFORM,
        name: "Danh sách đồng phục",
        actions: {
            VIEW: "Danh sách đồng phục",
            CREATE: "Tạo đồng phục",
            EDIT: "Chỉnh sửa đồng phục",
            EXPORT: "Xuất danh sách đồng phục"
        },
        UNIFORM: {
            VIEW: "Danh sách đồng phục",
            CREATE: "Tạo đồng phục",
            EDIT: "Chỉnh sửa đồng phục",
            EXPORT: "Xuất danh sách đồng phục"
        }
    },
    {
        key: "UNIFORM_ORDERS",
        path: PATH.UNIFORM_ORDERS,
        name: "Đơn mua đồng phục",
        actions: {
            VIEW: "Đơn mua đồng phục",
            CREATE: "Tạo đơn mua đồng phục",
            EDIT: "Chỉnh sửa đơn mua đồng phục",
            EXPORT: "Xuất đơn mua đồng phục"
        },
        UNIFORM_ORDERS: {
            VIEW: "Đơn mua đồng phục",
            CREATE: "Tạo đơn mua đồng phục",
            EDIT: "Chỉnh sửa đơn mua đồng phục",
            EXPORT: "Xuất đơn mua đồng phục"
        }
    },
    {
        key: "UNIFORM_EXPORTS",
        path: PATH.UNIFORM_EXPORTS,
        name: "Quản lý kho",
        actions: {
            VIEW: "Quản lý kho",
            CREATE: "Tạo quản lý kho",
            EDIT: "Chỉnh sửa quản lý kho",
            EXPORT: "Xuất quản lý kho"
        },
        UNIFORM_EXPORTS: {
            VIEW: "Quản lý kho",
            CREATE: "Tạo quản lý kho",
            EDIT: "Chỉnh sửa quản lý kho",
            EXPORT: "Xuất quản lý kho"
        }
    },
    {
        key: "REPORT_UNIFORMS_EXPIRED",
        path: PATH.REPORT_UNIFORMS_EXPIRED,
        name: "Nhân viên đến hạn cấp đồng phục",
        actions: {
            VIEW: "Nhân viên đến hạn cấp đồng phục",
            CREATE: "Tạo báo cáo đồng phục hết hạn",
            EDIT: "Chỉnh sửa báo cáo đồng phục hết hạn",
            EXPORT: "Xuất báo cáo đồng phục hết hạn"
        },
        REPORT_UNIFORMS_EXPIRED: {
            VIEW: "Nhân viên đến hạn cấp đồng phục",
            CREATE: "Tạo báo cáo đồng phục hết hạn",
            EDIT: "Chỉnh sửa báo cáo đồng phục hết hạn",
            EXPORT: "Xuất báo cáo đồng phục hết hạn"
        }
    },
    {
        key: "REPORT_UNIFORMS_EXPORTS",
        path: PATH.REPORT_UNIFORMS_EXPORTS,
        name: "Nhập xuất tồn đồng phục",
        actions: {
            VIEW: "Nhập xuất tồn đồng phục",
            CREATE: "Tạo báo cáo nhập xuất tồn đồng phục",
            EDIT: "Chỉnh sửa báo cáo nhập xuất tồn đồng phục",
            EXPORT: "Xuất báo cáo nhập xuất tồn đồng phục"
        },
        REPORT_UNIFORMS_EXPORTS: {
            VIEW: "Nhập xuất tồn đồng phục",
            CREATE: "Tạo báo cáo nhập xuất tồn đồng phục",
            EDIT: "Chỉnh sửa báo cáo nhập xuất tồn đồng phục",
            EXPORT: "Xuất báo cáo nhập xuất tồn đồng phục"
        }
    },
    {
        key: "REPORT_EMPLOYEE_EXPIRING_CONTRACT",
        path: PATH.REPORT_EMPLOYEE_EXPIRING_CONTRACT,
        name: "NV sắp hết hạn HĐ",
        actions: {
            VIEW: "NV sắp hết hạn HĐ",
            CREATE: "Tạo báo cáo hợp đồng hết hạn",
            EDIT: "Chỉnh sửa báo cáo hợp đồng hết hạn",
            EXPORT: "Xuất báo cáo hợp đồng hết hạn"
        },
        REPORT_EMPLOYEE_EXPIRING_CONTRACT: {
            VIEW: "NV sắp hết hạn HĐ",
            CREATE: "Tạo báo cáo hợp đồng hết hạn",
            EDIT: "Chỉnh sửa báo cáo hợp đồng hết hạn",
            EXPORT: "Xuất báo cáo hợp đồng hết hạn"
        }
    },
    {
        key: "REPORT_HUMAN_RESOURCE_CHANGE",
        path: PATH.REPORT_HUMAN_RESOURCE_CHANGE,
        name: "Biến động nhân sự",
        actions: {
            VIEW: "Biến động nhân sự",
            CREATE: "Tạo báo cáo biến động nhân sự",
            EDIT: "Chỉnh sửa báo cáo biến động nhân sự",
            EXPORT: "Xuất báo cáo biến động nhân sự"
        },
        REPORT_HUMAN_RESOURCE_CHANGE: {
            VIEW: "Biến động nhân sự",
            CREATE: "Tạo báo cáo biến động nhân sự",
            EDIT: "Chỉnh sửa báo cáo biến động nhân sự",
            EXPORT: "Xuất báo cáo biến động nhân sự"
        }
    },
    {
        key: "REPORT_LEAVE_REGIME",
        path: PATH.REPORT_LEAVE_REGIME,
        name: "Số lượng NV đăng ký nghỉ chế độ",
        actions: {
            VIEW: "Số lượng NV đăng ký nghỉ chế độ",
            CREATE: "Tạo báo cáo nghỉ chế độ",
            EDIT: "Chỉnh sửa báo cáo nghỉ chế độ",
            EXPORT: "Xuất báo cáo nghỉ chế độ"
        },
        REPORT_LEAVE_REGIME: {
            VIEW: "Số lượng NV đăng ký nghỉ chế độ",
            CREATE: "Tạo báo cáo nghỉ chế độ",
            EDIT: "Chỉnh sửa báo cáo nghỉ chế độ",
            EXPORT: "Xuất báo cáo nghỉ chế độ"
        }
    },
    {
        key: "REPORT_RECRUITMENT",
        path: PATH.REPORT_RECRUITMENT,
        name: "Yêu cầu tuyển dụng",
        actions: {
            VIEW: "Yêu cầu tuyển dụng",
            CREATE: "Tạo báo cáo yêu cầu tuyển dụng",
            EDIT: "Chỉnh sửa báo cáo yêu cầu tuyển dụng",
            EXPORT: "Xuất báo cáo yêu cầu tuyển dụng"
        },
        REPORT_RECRUITMENT: {
            VIEW: "Yêu cầu tuyển dụng",
            CREATE: "Tạo báo cáo yêu cầu tuyển dụng",
            EDIT: "Chỉnh sửa báo cáo yêu cầu tuyển dụng",
            EXPORT: "Xuất báo cáo yêu cầu tuyển dụng"
        }
    },
    {
        key: "PRODUCTION_MANUFACTURE_ORDER_STANDARD",
        path: PATH.PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD,
        name: "Hằng ngày",
        actions: {
            VIEW: "Hằng ngày",
            CREATE: "Tạo đơn hàng sản xuất",
            EDIT: "Chỉnh sửa đơn hàng sản xuất",
            EXPORT: "Xuất đơn hàng sản xuất"
        },
        PRODUCTION_MANUFACTURE_ORDER_STANDARD: {
            VIEW: "Hằng ngày",
            CREATE: "Tạo đơn hàng sản xuất",
            EDIT: "Chỉnh sửa đơn hàng sản xuất",
            EXPORT: "Xuất đơn hàng sản xuất"
        }
    },
    {
        key: "PRODUCTION_MANUFACTURE_ORDER",
        path: PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER,
        name: "Trộn bột",
        actions: {
            VIEW: "Trộn bột",
            CREATE: "Tạo đơn hàng trộn bột",
            EDIT: "Chỉnh sửa đơn hàng trộn bột",
            EXPORT: "Xuất đơn hàng trộn bột"
        },
        PRODUCTION_MANUFACTURE_ORDER: {
            VIEW: "Trộn bột",
            CREATE: "Tạo đơn hàng trộn bột",
            EDIT: "Chỉnh sửa đơn hàng trộn bột",
            EXPORT: "Xuất đơn hàng trộn bột"
        }
    },
    {
        key: "PRODUCTION_PACKAGES",
        path: PATH.PRODUCTION_PACKAGES,
        name: "Quản lý đóng gói",
        actions: {
            VIEW: "Quản lý đóng gói",
            CREATE: "Tạo quản lý đóng gói",
            EDIT: "Chỉnh sửa quản lý đóng gói",
            EXPORT: "Xuất quản lý đóng gói"
        },
        PRODUCTION_PACKAGES: {
            VIEW: "Quản lý đóng gói",
            CREATE: "Tạo quản lý đóng gói",
            EDIT: "Chỉnh sửa quản lý đóng gói",
            EXPORT: "Xuất quản lý đóng gói"
        }
    },
    {
        key: "PRODUCTION_QUALITY",
        path: PATH.PRODUCTION_QUALITY,
        name: "Quản lý kiểm tra chất lượng",
        actions: {
            VIEW: "Quản lý kiểm tra chất lượng",
            CREATE: "Tạo quản lý kiểm tra chất lượng",
            EDIT: "Chỉnh sửa quản lý kiểm tra chất lượng",
            EXPORT: "Xuất quản lý kiểm tra chất lượng"
        },
        PRODUCTION_QUALITY: {
            VIEW: "Quản lý kiểm tra chất lượng",
            CREATE: "Tạo quản lý kiểm tra chất lượng",
            EDIT: "Chỉnh sửa quản lý kiểm tra chất lượng",
            EXPORT: "Xuất quản lý kiểm tra chất lượng"
        }
    },
    {
        key: "PRODUCTION_MAINTENANCE",
        path: PATH.PRODUCTION_MAINTENANCE,
        name: "Quản lý bảo trì sản phẩm",
        actions: {
            VIEW: "Quản lý bảo trì sản phẩm",
            CREATE: "Tạo quản lý bảo trì sản phẩm",
            EDIT: "Chỉnh sửa quản lý bảo trì sản phẩm",
            EXPORT: "Xuất quản lý bảo trì sản phẩm"
        },
        PRODUCTION_MAINTENANCE: {
            VIEW: "Quản lý bảo trì sản phẩm",
            CREATE: "Tạo quản lý bảo trì sản phẩm",
            EDIT: "Chỉnh sửa quản lý bảo trì sản phẩm",
            EXPORT: "Xuất quản lý bảo trì sản phẩm"
        }
    },
    {
        key: "PRODUCTION_ROUTINGS",
        path: PATH.PRODUCTION_ROUTINGS,
        name: "Quản lý tuyến kho sản xuất",
        actions: {
            VIEW: "Quản lý tuyến kho sản xuất",
            CREATE: "Tạo quản lý tuyến kho sản xuất",
            EDIT: "Chỉnh sửa quản lý tuyến kho sản xuất",
            EXPORT: "Xuất quản lý tuyến kho sản xuất"
        },
        PRODUCTION_ROUTINGS: {
            VIEW: "Quản lý tuyến kho sản xuất",
            CREATE: "Tạo quản lý tuyến kho sản xuất",
            EDIT: "Chỉnh sửa quản lý tuyến kho sản xuất",
            EXPORT: "Xuất quản lý tuyến kho sản xuất"
        }
    },
    {
        key: "PRODUCTION_STANDARD",
        path: PATH.PRODUCTION_STANDARD,
        name: "Quản lý định mức sản xuất",
        actions: {
            VIEW: "Quản lý định mức sản xuất",
            CREATE: "Tạo quản lý định mức sản xuất",
            EDIT: "Chỉnh sửa quản lý định mức sản xuất",
            EXPORT: "Xuất quản lý định mức sản xuất"
        },
        PRODUCTION_STANDARD: {
            VIEW: "Quản lý định mức sản xuất",
            CREATE: "Tạo quản lý định mức sản xuất",
            EDIT: "Chỉnh sửa quản lý định mức sản xuất",
            EXPORT: "Xuất quản lý định mức sản xuất"
        }
    },
    {
        key: "PRODUCTION_WORK_CENTERS",
        path: PATH.PRODUCTION_WORK_CENTERS,
        name: "Quản lý cụm máy sản xuất",
        actions: {
            VIEW: "Quản lý cụm máy sản xuất",
            CREATE: "Tạo quản lý cụm máy sản xuất",
            EDIT: "Chỉnh sửa quản lý cụm máy sản xuất",
            EXPORT: "Xuất quản lý cụm máy sản xuất"
        },
        PRODUCTION_WORK_CENTERS: {
            VIEW: "Quản lý cụm máy sản xuất",
            CREATE: "Tạo quản lý cụm máy sản xuất",
            EDIT: "Chỉnh sửa quản lý cụm máy sản xuất",
            EXPORT: "Xuất quản lý cụm máy sản xuất"
        }
    },
    {
        key: "LOGISTICS_SUPPLIERS",
        path: PATH.SUPPLIERS,
        name: "Nhà cung cấp",
        actions: {
            VIEW: "Nhà cung cấp",
            CREATE: "Tạo nhà cung cấp",
            EDIT: "Chỉnh sửa nhà cung cấp",
            EXPORT: "Xuất danh sách nhà cung cấp"
        },
        LOGISTICS_SUPPLIERS: {
            VIEW: "Nhà cung cấp",
            CREATE: "Tạo nhà cung cấp",
            EDIT: "Chỉnh sửa nhà cung cấp",
            EXPORT: "Xuất danh sách nhà cung cấp"
        }
    },
    {
        key: "LOGISTICS_SUPPLIES",
        path: PATH.SUPPLIES,
        name: "Hàng hoá",
        actions: {
            VIEW: "Hàng hoá",
            CREATE: "Tạo hàng hoá",
            EDIT: "Chỉnh sửa hàng hoá",
            EXPORT: "Xuất hàng hoá"
        },
        LOGISTICS_SUPPLIES: {
            VIEW: "Hàng hoá",
            CREATE: "Tạo hàng hoá",
            EDIT: "Chỉnh sửa hàng hoá",
            EXPORT: "Xuất hàng hoá"
        }
    },
    {
        key: "LOGISTICS_FACTORIES",
        path: PATH.FACTORIES,
        name: "Quản lý nhà máy",
        actions: {
            VIEW: "Quản lý nhà máy",
            CREATE: "Tạo nhà máy",
            EDIT: "Chỉnh sửa nhà máy",
            EXPORT: "Xuất danh sách nhà máy"
        },
        LOGISTICS_FACTORIES: {
            VIEW: "Quản lý nhà máy",
            CREATE: "Tạo nhà máy",
            EDIT: "Chỉnh sửa nhà máy",
            EXPORT: "Xuất danh sách nhà máy"
        }
    },
    {
        key: "LOGISTICS_SUPPLIER_CONTRACTS",
        path: PATH.SUPPLIER_CONTRACTS,
        name: "Hợp đồng mua",
        actions: {
            VIEW: "Hợp đồng mua",
            CREATE: "Tạo hợp đồng mua",
            EDIT: "Chỉnh sửa hợp đồng mua",
            EXPORT: "Xuất hợp đồng mua"
        },
        LOGISTICS_SUPPLIER_CONTRACTS: {
            VIEW: "Hợp đồng mua",
            CREATE: "Tạo hợp đồng mua",
            EDIT: "Chỉnh sửa hợp đồng mua",
            EXPORT: "Xuất hợp đồng mua"
        }
    },
    {
        key: "INCOMING_INVOICE",
        path: PATH.INCOMING_INVOICE,
        name: "Hoá đơn đầu vào",
        actions: {
            VIEW: "Hoá đơn đầu vào",
            CREATE: "Tạo hoá đơn đầu vào",
            EDIT: "Chỉnh sửa hoá đơn đầu vào",
            EXPORT: "Xuất hoá đơn đầu vào"
        },
        INCOMING_INVOICE: {
            VIEW: "Hoá đơn đầu vào",
            CREATE: "Tạo hoá đơn đầu vào",
            EDIT: "Chỉnh sửa hoá đơn đầu vào",
            EXPORT: "Xuất hoá đơn đầu vào"
        }
    },
    {
        key: "LOGISTICS_INVENTORIES_STORAGE",
        path: PATH.INVENTORIES_STORAGE,
        name: "Nhập kho",
        actions: {
            VIEW: "Nhập kho",
            CREATE: "Tạo nhập kho",
            EDIT: "Chỉnh sửa nhập kho",
            EXPORT: "Xuất nhập kho"
        },
        LOGISTICS_INVENTORIES_STORAGE: {
            VIEW: "Nhập kho",
            CREATE: "Tạo nhập kho",
            EDIT: "Chỉnh sửa nhập kho",
            EXPORT: "Xuất nhập kho"
        }
    },
    {
        key: "LOGISTICS_INVENTORIES_STORAGE_EXPORT",
        path: PATH.INVENTORIES_STORAGE_EXPORT,
        name: "Xuất kho",
        actions: {
            VIEW: "Xuất kho",
            CREATE: "Tạo xuất kho",
            EDIT: "Chỉnh sửa xuất kho",
            EXPORT: "Xuất xuất kho"
        },
        LOGISTICS_INVENTORIES_STORAGE_EXPORT: {
            VIEW: "Xuất kho",
            CREATE: "Tạo xuất kho",
            EDIT: "Chỉnh sửa xuất kho",
            EXPORT: "Xuất xuất kho"
        }
    },
    {
        key: "LOGISTICS_STOCKTAKING",
        path: PATH.STOCKTAKING,
        name: "Kiểm kê",
        actions: {
            VIEW: "Kiểm kê",
            CREATE: "Tạo kiểm kê",
            EDIT: "Chỉnh sửa kiểm kê",
            EXPORT: "Xuất kiểm kê"
        },
        LOGISTICS_STOCKTAKING: {
            VIEW: "Kiểm kê",
            CREATE: "Tạo kiểm kê",
            EDIT: "Chỉnh sửa kiểm kê",
            EXPORT: "Xuất kiểm kê"
        }
    },
    {
        key: "DELIVERY_SCHEDULE",
        path: PATH.DELIVERY_SCHEDULE,
        name: "Lịch giao - nhận hàng",
        actions: {
            VIEW: "Lịch giao - nhận hàng",
            CREATE: "Tạo lịch giao - nhận hàng",
            EDIT: "Chỉnh sửa lịch giao - nhận hàng",
            EXPORT: "Xuất lịch giao - nhận hàng"
        },
        DELIVERY_SCHEDULE: {
            VIEW: "Lịch giao - nhận hàng",
            CREATE: "Tạo lịch giao - nhận hàng",
            EDIT: "Chỉnh sửa lịch giao - nhận hàng",
            EXPORT: "Xuất lịch giao - nhận hàng"
        }
    },
    {
        key: "ASSET",
        path: PATH.ASSET,
        name: "Hồ sơ tài sản - CCDC",
        actions: {
            VIEW: "Hồ sơ tài sản - CCDC",
            CREATE: "Tạo hồ sơ tài sản",
            EDIT: "Chỉnh sửa hồ sơ tài sản",
            EXPORT: "Xuất hồ sơ tài sản"
        },
        ASSET: {
            VIEW: "Hồ sơ tài sản - CCDC",
            CREATE: "Tạo hồ sơ tài sản",
            EDIT: "Chỉnh sửa hồ sơ tài sản",
            EXPORT: "Xuất hồ sơ tài sản"
        }
    },
    {
        key: "ASSET_TRANSFER",
        path: PATH.TRANSFER_ASSETS,
        name: "Điều chuyển tài sản",
        actions: {
            VIEW: "Điều chuyển tài sản",
            CREATE: "Tạo điều chuyển tài sản",
            EDIT: "Chỉnh sửa điều chuyển tài sản",
            EXPORT: "Xuất điều chuyển tài sản"
        },
        ASSET_TRANSFER: {
            VIEW: "Điều chuyển tài sản",
            CREATE: "Tạo điều chuyển tài sản",
            EDIT: "Chỉnh sửa điều chuyển tài sản",
            EXPORT: "Xuất điều chuyển tài sản"
        }
    },
    {
        key: "ASSET_DEPRECIATION",
        path: PATH.DEPRECIATION,
        name: "Khấu hao tài sản",
        actions: {
            VIEW: "Khấu hao tài sản",
            CREATE: "Tạo khấu hao tài sản",
            EDIT: "Chỉnh sửa khấu hao tài sản",
            EXPORT: "Xuất khấu hao tài sản"
        },
        ASSET_DEPRECIATION: {
            VIEW: "Khấu hao tài sản",
            CREATE: "Tạo khấu hao tài sản",
            EDIT: "Chỉnh sửa khấu hao tài sản",
            EXPORT: "Xuất khấu hao tài sản"
        }
    },
    {
        key: "ASSET_ALLOCATION",
        path: PATH.ALLOCATION,
        name: "Phân bổ CCDC",
        actions: {
            VIEW: "Phân bổ CCDC",
            CREATE: "Tạo phân bổ CCDC",
            EDIT: "Chỉnh sửa phân bổ CCDC",
            EXPORT: "Xuất phân bổ CCDC"
        },
        ASSET_ALLOCATION: {
            VIEW: "Phân bổ CCDC",
            CREATE: "Tạo phân bổ CCDC",
            EDIT: "Chỉnh sửa phân bổ CCDC",
            EXPORT: "Xuất phân bổ CCDC"
        }
    },
    {
        key: "ASSET_LIQUIDATION",
        path: PATH.LIQUIDATION,
        name: "Thanh lý tài sản",
        actions: {
            VIEW: "Thanh lý tài sản",
            CREATE: "Tạo thanh lý tài sản",
            EDIT: "Chỉnh sửa thanh lý tài sản",
            EXPORT: "Xuất thanh lý tài sản"
        },
        ASSET_LIQUIDATION: {
            VIEW: "Thanh lý tài sản",
            CREATE: "Tạo thanh lý tài sản",
            EDIT: "Chỉnh sửa thanh lý tài sản",
            EXPORT: "Xuất thanh lý tài sản"
        }
    },
    {
        key: "CUSTOMER_SERVICES_CALL_CENTER",
        path: PATH.CALL_CENTER,
        name: "Call center",
        actions: {
            VIEW: "Call center",
            CREATE: "Tạo call center",
            EDIT: "Chỉnh sửa call center",
            EXPORT: "Xuất call center"
        },
        CUSTOMER_SERVICES_CALL_CENTER: {
            VIEW: "Call center",
            CREATE: "Tạo call center",
            EDIT: "Chỉnh sửa call center",
            EXPORT: "Xuất call center"
        }
    },
    {
        key: "CUSTOMER_SERVICES_COMPLAIN",
        path: PATH.COMPLAIN,
        name: "Khiếu nại",
        actions: {
            VIEW: "Khiếu nại",
            CREATE: "Tạo khiếu nại",
            EDIT: "Chỉnh sửa khiếu nại",
            EXPORT: "Xuất khiếu nại"
        },
        CUSTOMER_SERVICES_COMPLAIN: {
            VIEW: "Khiếu nại",
            CREATE: "Tạo khiếu nại",
            EDIT: "Chỉnh sửa khiếu nại",
            EXPORT: "Xuất khiếu nại"
        }
    },
    {
        key: "TECHNICAL_MACHINERY_EQUIPMENT",
        path: PATH.MACHINERY_EQUIPMENT,
        name: "Máy móc thiết bị",
        actions: {
            VIEW: "Máy móc thiết bị",
            CREATE: "Tạo máy móc thiết bị",
            EDIT: "Chỉnh sửa máy móc thiết bị",
            EXPORT: "Xuất máy móc thiết bị"
        },
        TECHNICAL_MACHINERY_EQUIPMENT: {
            VIEW: "Máy móc thiết bị",
            CREATE: "Tạo máy móc thiết bị",
            EDIT: "Chỉnh sửa máy móc thiết bị",
            EXPORT: "Xuất máy móc thiết bị"
        }
    },
    {
        key: "PERMISSIONS_USERS",
        path: PATH.AUTHORITIES_USERS,
        name: "Người dùng",
        actions: {
            VIEW: "Người dùng",
            CREATE: "Tạo người dùng",
            EDIT: "Chỉnh sửa người dùng",
            EXPORT: "Xuất người dùng"
        },
        PERMISSIONS_USERS: {
            VIEW: "Người dùng",
            CREATE: "Tạo người dùng",
            EDIT: "Chỉnh sửa người dùng",
            EXPORT: "Xuất người dùng"
        }
    },
    {
        key: "PERMISSIONS_GROUPS",
        path: PATH.AUTHORITIES_GROUPS,
        name: "Nhóm quyền",
        actions: {
            VIEW: "Nhóm quyền",
            CREATE: "Tạo nhóm quyền",
            EDIT: "Chỉnh sửa nhóm quyền",
            EXPORT: "Xuất nhóm quyền"
        },
        PERMISSIONS_GROUPS: {
            VIEW: "Nhóm quyền",
            CREATE: "Tạo nhóm quyền",
            EDIT: "Chỉnh sửa nhóm quyền",
            EXPORT: "Xuất nhóm quyền"
        }
    },
    {
        key: "ANNUAL_LEAVE",
        path: PATH.ANNUAL_LEAVE,
        name: "Phép năm",
        actions: {
            VIEW: "Phép năm",
            CREATE: "Tạo phép năm",
            EDIT: "Chỉnh sửa phép năm",
            EXPORT: "Xuất phép năm"
        },
        ANNUAL_LEAVE: {
            VIEW: "Phép năm",
            CREATE: "Tạo phép năm",
            EDIT: "Chỉnh sửa phép năm",
            EXPORT: "Xuất phép năm"
        }
    },
    {
        key: "UOM",
        path: PATH.UOM,
        name: "Quản lý đơn vị",
        actions: {
            VIEW: "Quản lý đơn vị",
            CREATE: "Tạo đơn vị",
            EDIT: "Chỉnh sửa đơn vị",
            EXPORT: "Xuất danh sách đơn vị"
        },
        UOM: {
            VIEW: "Quản lý đơn vị",
            CREATE: "Tạo đơn vị",
            EDIT: "Chỉnh sửa đơn vị",
            EXPORT: "Xuất danh sách đơn vị"
        }
    },
    {
        key: "ITEMS_SETTINGS",
        path: PATH.ITEMS,
        name: "Danh sách vật phẩm",
        actions: {
            VIEW: "Danh sách vật phẩm",
            CREATE: "Tạo vật phẩm",
            EDIT: "Chỉnh sửa vật phẩm",
            EXPORT: "Xuất danh sách vật phẩm"
        },
        ITEMS_SETTINGS: {
            VIEW: "Danh sách vật phẩm",
            CREATE: "Tạo vật phẩm",
            EDIT: "Chỉnh sửa vật phẩm",
            EXPORT: "Xuất danh sách vật phẩm"
        }
    },
    {
        key: "UNIFORM_SETTINGS",
        path: PATH.UNIFORM_SETTINGS,
        name: "Đồng phục",
        actions: {
            VIEW: "Đồng phục",
            CREATE: "Tạo đồng phục",
            EDIT: "Chỉnh sửa đồng phục",
            EXPORT: "Xuất đồng phục"
        },
        UNIFORM_SETTINGS: {
            VIEW: "Đồng phục",
            CREATE: "Tạo đồng phục",
            EDIT: "Chỉnh sửa đồng phục",
            EXPORT: "Xuất đồng phục"
        }
    },
    {
        key: "WAREHOUSES",
        path: PATH.WAREHOUSES,
        name: "Danh sách kho",
        actions: {
            VIEW: "Danh sách kho",
            CREATE: "Tạo kho",
            EDIT: "Chỉnh sửa kho",
            EXPORT: "Xuất danh sách kho"
        },
        WAREHOUSES: {
            VIEW: "Danh sách kho",
            CREATE: "Tạo kho",
            EDIT: "Chỉnh sửa kho",
            EXPORT: "Xuất danh sách kho"
        }
    },
    {
        key: "COMPANY",
        path: PATH.COMPANY,
        name: "Danh sách công ty",
        actions: {
            VIEW: "Danh sách công ty",
            CREATE: "Tạo công ty",
            EDIT: "Chỉnh sửa công ty",
            EXPORT: "Xuất danh sách công ty"
        },
        COMPANY: {
            VIEW: "Danh sách công ty",
            CREATE: "Tạo công ty",
            EDIT: "Chỉnh sửa công ty",
            EXPORT: "Xuất danh sách công ty"
        }
    },
];

// function generateInsertStatements(permissionKey, description, actions) {
//     const statements = actions.map(action => {
//         return `INSERT INTO masi_authority ("name", description, "action", resource) VALUES('PERMISSION.${permissionKey}.${action}', '${description}', '${action}', '${permissionKey}');`;
//     });
//     return statements.join('\n');
// }

// // Sử dụng hàm
// const actions = ['VIEW', 'CREATE', 'EDIT', 'EXPORT'];
// permissions.forEach(permission => {
//     const insertStatements = generateInsertStatements(permission.key, permission.name, actions);
//     console.log(insertStatements);
// })

export type Permission = typeof permissions[number];