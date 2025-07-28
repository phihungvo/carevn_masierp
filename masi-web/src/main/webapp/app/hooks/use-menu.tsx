import { permissions } from 'app/config/permission';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { Menu } from 'app/shared/layout/menus-left/menus-left';
import { ItemType } from 'app/shared/model/enumerations/item-category.model';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { checkPermission } from 'app/shared/util/check-permission';
import { useState } from 'react';

export const useMenu = (): Menu[] => {
  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const checkPermissionRoute = (authorities: string[], key: string) => {
    const isSuperAdmin = authorities?.indexOf('ROLE_SUPER_ADMIN') != -1;
    const isAdmin = authorities?.indexOf('ROLE_ADMIN') != -1;

    if (isSuperAdmin || isAdmin) {
      return true;
    }

    const permission = permissions.find(permission => permission.key === key);
    if (permission) {
      return Object.keys(permission.actions).find(item => authorities?.indexOf(`PERMISSION.${permission.key}.${item}`) !== -1)?.length > 0;
    }
    return false;
  }

  const items = [
    {
      key: 'general',
      icon: (
        <img src="content/images/vuesax/bulk/columns.svg" alt="general" />
      ),
      selectedIcon: (
        <img src="content/images/vuesax/bulk/columns.svg" alt="general" />
      ),
      isPermitted: checkPermissionRoute(authorities, "REQUEST_PAYMENT") || checkPermissionRoute(authorities, "SUPPLIES_REQUESTS"),
      label: 'Mục chung',
      items: [
        {
          label: 'Mục chung',
          isPermitted: checkPermissionRoute(authorities, "REQUEST_PAYMENT") || checkPermissionRoute(authorities, "SUPPLIES_REQUESTS"),
          items: [
            {
              label: 'Phiếu đề nghị',
              to: PATH.REQUEST_PAYMENT,
              key: PATH.REQUEST_PAYMENT,
              isPermitted: checkPermissionRoute(authorities, "REQUEST_PAYMENT"),
            },
            {
              label: 'Đề xuất mua hàng',
              to: PATH.SUPPLIES_REQUESTS,
              key: PATH.SUPPLIES_REQUESTS,
              isPermitted: checkPermissionRoute(authorities, "SUPPLIES_REQUESTS"),
            },
          ]
        },
      ]
    },
    {
      key: 'time-sheet',
      icon: (
        <img src="content/images/vuesax/bulk/lists 1.svg" alt="time-sheet" />
      ),
      selectedIcon: (
        <img src="content/images/vuesax/bulk/lists 1.svg" alt="time-sheet" />
      ),
      label: 'BCC',
      isPermitted: checkPermissionRoute(authorities, "TIME_SHEET") || checkPermissionRoute(authorities, "TIME_SHEET_VIOLATION")
        || checkPermissionRoute(authorities, "TIME_SHEET_EXPLANATION") || checkPermissionRoute(authorities, "TIME_SHEET_EXPLANATION") ||
        checkPermissionRoute(authorities, "TIME_SHEET_BULK_APPROVAL"),
      items: [
        {
          label: 'Chấm công',
          to: PATH.TIME_SHEET,
          key: PATH.TIME_SHEET,
          isPermitted: checkPermissionRoute(authorities, "TIME_SHEET"),
        },
        {
          label: 'Vi phạm chấm công',
          key: 'violation',
          isPermitted: true,
          items: [
            {
              label: 'Vi phạm chấm công',
              to: PATH.TIME_SHEET_VIOLATION,
              key: PATH.TIME_SHEET_VIOLATION,
              isPermitted: checkPermissionRoute(authorities, "TIME_SHEET_VIOLATION"),
            },
            {
              label: 'Giải trình chấm công',
              to: PATH.TIME_SHEET_EXPLANATION,
              key: PATH.TIME_SHEET_EXPLANATION,
              isPermitted: checkPermissionRoute(authorities, "TIME_SHEET_EXPLANATION"),
            },
          ],
        },
        {
          label: 'Duyệt chấm công tháng',
          to: PATH.TIME_SHEET_BULK_APPROVAL,
          key: PATH.TIME_SHEET_BULK_APPROVAL,
          isPermitted: checkPermissionRoute(authorities, "TIME_SHEET_BULK_APPROVAL"),
        }
      ],
    },
    {
      key: 'day-off',
      icon: (
        <img
          src="content/images/vuesax/bulk/calendar-check-02.svg"
          alt="day-off"
        />
      ),
      selectedIcon: (
        <img
          src="content/images/vuesax/bulk/calendar-check-02.svg"
          alt="day-off"
        />
      ),
      label: 'Nghỉ phép',
      to: PATH.LEAVE_REQUEST,
      isPermitted: checkPermissionRoute(authorities, "LEAVE_REQUEST"),
      items: [
        {
          label: 'Danh sách đơn nghỉ phép',
          to: PATH.LEAVE_REQUEST,
          key: PATH.LEAVE_REQUEST,
          isPermitted: checkPermissionRoute(authorities, "LEAVE_REQUEST"),
        },
      ],
    },
    {
      key: 'customers',
      icon: (
        <img src="content/images/vuesax/bulk/building-2.svg" alt="production" />
      ),
      selectedIcon: (
        <img src="content/images/vuesax/bulk/building-2.svg" alt="production" />
      ),
      label: 'Kinh doanh',
      isPermitted: checkPermissionRoute(authorities, "CUSTOMERS") || checkPermissionRoute(authorities, "CUSTOMERS_DISPOSED") ||
        checkPermissionRoute(authorities, "PRICE_LIST") || checkPermissionRoute(authorities, "CONTRACTS") ||
        checkPermissionRoute(authorities, "CONTRACTS_DELETED") || checkPermissionRoute(authorities, "ORDERS"),
      items: [
        {
          label: 'Quản lý khách hàng',
          key: 'customers-management',
          isPermitted: checkPermissionRoute(authorities, "CUSTOMERS") || checkPermissionRoute(authorities, "CUSTOMERS_DISPOSED"),
          items: [
            {
              label: 'Danh sách khách hàng',
              to: PATH.CUSTOMERS,
              key: PATH.CUSTOMERS,
              isPermitted: checkPermissionRoute(authorities, "CUSTOMERS"),
            },
            {
              label: 'Danh sách vô hiệu hoá',
              to: PATH.CUSTOMERS_DISPOSED,
              key: PATH.CUSTOMERS_DISPOSED,
              isPermitted: checkPermissionRoute(authorities, "CUSTOMERS_DISPOSED"),
            },
          ],
        },
        {
          label: 'Bảng báo giá',
          key: 'priceList',
          to: PATH.PRICE_LIST,
          isPermitted: checkPermissionRoute(authorities, "PRICE_LIST"),
        },
        {
          label: 'Hợp đồng',
          key: 'contracts-management',
          isPermitted: checkPermissionRoute(authorities, "CONTRACTS") || checkPermissionRoute(authorities, "CONTRACTS_DELETED"),
          items: [
            {
              label: 'Danh sách tất cả hợp đồng',
              to: PATH.CONTRACTS,
              key: PATH.CONTRACTS,
              isPermitted: checkPermissionRoute(authorities, "CONTRACTS"),
            },
            {
              label: 'Danh sách hợp đồng đã xoá',
              to: PATH.CONTRACTS_DELETED,
              key: PATH.CONTRACTS_DELETED,
              isPermitted: checkPermissionRoute(authorities, "CONTRACTS_DELETED"),
            },
          ],
        },
        {
          label: 'Đơn đặt hàng',
          to: PATH.ORDERS,
          key: PATH.ORDERS,
          isPermitted: checkPermissionRoute(authorities, "ORDERS"),
        },
        // {
        //   label: 'Đề nghị thu mua',
        //   key: 'purchase',
        //   to: PATH.PURCHASE,
        //   isPermitted: checkPermissionRoute(authorities, "ORDERS"),
        // },
      ],
    },
    {
      key: 'employees',
      icon: <img src="content/images/vuesax/bulk/users-01.svg" alt="user" />,
      selectedIcon: (
        <img
          src="content/images/vuesax/bulk/users-01.svg"
          alt="user-selected"
        />
      ),
      label: 'HCNS',
      isPermitted: checkPermissionRoute(authorities, "EMPLOYEES") || checkPermissionRoute(authorities, "OFFICES") ||
        checkPermissionRoute(authorities, "LEAVE_REGISTER") || checkPermissionRoute(authorities, "RECRUITMENT")
        || checkPermissionRoute(authorities, "RECRUITMENT_CANDIDATES") || checkPermissionRoute(authorities, "DOCUMENTARY")
        || checkPermissionRoute(authorities, "UNIFORM") || checkPermissionRoute(authorities, "UNIFORM_ORDERS")
        || checkPermissionRoute(authorities, "UNIFORM_EXPORTS") || checkPermissionRoute(authorities, "REPORT_UNIFORMS_EXPIRED") || checkPermissionRoute(authorities, "REPORT_UNIFORMS_EXPORTS")
        || checkPermissionRoute(authorities, "REPORT_EMPLOYEE_EXPIRING_CONTRACT") || checkPermissionRoute(authorities, "REPORT_HUMAN_RESOURCE_CHANGE")
        || checkPermissionRoute(authorities, "REPORT_LEAVE_REGIME") || checkPermissionRoute(authorities, "REPORT_RECRUITMENT"),
      items: [
        {
          label: 'Hồ sơ nhân viên',
          key: 'employeesProfile',
          isPermitted: checkPermissionRoute(authorities, "EMPLOYEES") || checkPermissionRoute(authorities, "OFFICES"),
          items: [
            {
              label: 'Danh sách NV',
              to: PATH.EMPLOYEES,
              key: PATH.EMPLOYEES,
              isPermitted: checkPermissionRoute(authorities, "EMPLOYEES"),
            },
            {
              label: 'Văn phòng/Nhà máy',
              to: PATH.OFFICES,
              key: PATH.OFFICES,
              isPermitted: checkPermissionRoute(authorities, "OFFICES"),
            },
          ],
        },
        {
          label: 'Đăng ký nghỉ chế độ',
          key: 'leaveRegister',
          to: PATH.LEAVE_REGISTER,
          isPermitted: checkPermissionRoute(authorities, "LEAVE_REGISTER"),
        },
        {
          label: 'Tuyển dụng',
          key: 'recruitment',
          isPermitted: checkPermissionRoute(authorities, "RECRUITMENT") || checkPermissionRoute(authorities, "RECRUITMENT_CANDIDATES"),
          items: [
            {
              label: 'Yêu cầu tuyển dụng',
              to: PATH.RECRUITMENT,
              key: PATH.RECRUITMENT,
              isPermitted: checkPermissionRoute(authorities, "RECRUITMENT"),
            },
            {
              label: 'Danh sách ứng viên',
              to: PATH.RECRUITMENT_CANDIDATES,
              key: PATH.RECRUITMENT_CANDIDATES,
              isPermitted: checkPermissionRoute(authorities, "RECRUITMENT_CANDIDATES"),
            },
          ],
        },
        {
          label: 'Công văn',
          key: 'documentary',
          to: PATH.DOCUMENTARY,
          isPermitted: checkPermissionRoute(authorities, "DOCUMENTARY"),
        },
        {
          label: 'Đồng phục',
          key: 'uniform',
          isPermitted: checkPermissionRoute(authorities, "UNIFORM") || checkPermissionRoute(authorities, "UNIFORM_ORDERS")
            || checkPermissionRoute(authorities, "UNIFORM_EXPORTS"),
          items: [
            {
              label: 'Danh sách đồng phục',
              to: PATH.UNIFORM,
              key: PATH.UNIFORM,
              isPermitted: checkPermissionRoute(authorities, "UNIFORM"),
            },
            {
              label: 'Đơn mua đồng phục',
              to: PATH.UNIFORM_ORDERS,
              key: PATH.UNIFORM_ORDERS,
              isPermitted: checkPermissionRoute(authorities, "UNIFORM_ORDERS"),
            },
            {
              label: 'Quản lý kho',
              to: PATH.UNIFORM_EXPORTS,
              key: PATH.UNIFORM_EXPORTS,
              isPermitted: checkPermissionRoute(authorities, "UNIFORM_EXPORTS"),
            },
          ],
        },
        {
          label: 'Báo cáo',
          key: 'report',
          isPermitted: checkPermissionRoute(authorities, "REPORT_UNIFORMS_EXPIRED") || checkPermissionRoute(authorities, "REPORT_UNIFORMS_EXPORTS")
            || checkPermissionRoute(authorities, "REPORT_EMPLOYEE_EXPIRING_CONTRACT") || checkPermissionRoute(authorities, "REPORT_HUMAN_RESOURCE_CHANGE")
            || checkPermissionRoute(authorities, "REPORT_LEAVE_REGIME") || checkPermissionRoute(authorities, "REPORT_RECRUITMENT"),
          items: [
            {
              label: 'Nhân viên đến hạn cấp đồng phục',
              to: PATH.REPORT_UNIFORMS_EXPIRED,
              key: PATH.REPORT_UNIFORMS_EXPIRED,
              isPermitted: checkPermissionRoute(authorities, "REPORT_UNIFORMS_EXPIRED"),
            },
            {
              label: 'Nhập xuất tồn đồng phục',
              to: PATH.REPORT_UNIFORMS_EXPORTS,
              key: PATH.REPORT_UNIFORMS_EXPORTS,
              isPermitted: checkPermissionRoute(authorities, "REPORT_UNIFORMS_EXPORTS"),
            },
            {
              label: 'NV sắp hết hạn HĐ',
              to: PATH.REPORT_EMPLOYEE_EXPIRING_CONTRACT,
              key: PATH.REPORT_EMPLOYEE_EXPIRING_CONTRACT,
              isPermitted: checkPermissionRoute(authorities, "REPORT_EMPLOYEE_EXPIRING_CONTRACT"),
            },
            {
              label: 'Biến động nhân sự',
              to: PATH.REPORT_HUMAN_RESOURCE_CHANGE,
              key: PATH.REPORT_HUMAN_RESOURCE_CHANGE,
              isPermitted: checkPermissionRoute(authorities, "REPORT_HUMAN_RESOURCE_CHANGE"),
            },
            {
              label: 'Số lượng NV đăng ký nghỉ chế độ',
              to: PATH.REPORT_LEAVE_REGIME,
              key: PATH.REPORT_LEAVE_REGIME,
              isPermitted: checkPermissionRoute(authorities, "REPORT_LEAVE_REGIME"),
            },
            {
              label: 'Yêu cầu tuyển dụng',
              to: PATH.REPORT_RECRUITMENT,
              key: PATH.REPORT_RECRUITMENT,
              isPermitted: checkPermissionRoute(authorities, "REPORT_RECRUITMENT"),
            },
          ],
        },
      ],
    },
    {
      key: 'production',
      icon: (
        <img
          src="content/images/vuesax/bulk/li_git-fork.svg"
          alt="production"
        />
      ),
      selectedIcon: (
        <img
          src="content/images/vuesax/bulk/li_git-fork.svg"
          alt="production"
        />
      ),
      label: 'Sản xuất',
      isPermitted: checkPermissionRoute(authorities, "PRODUCTION_MANUFACTURE_ORDER_STANDARD") || checkPermissionRoute(authorities, "PRODUCTION_MANUFACTURE_ORDER")
        || checkPermissionRoute(authorities, "PRODUCTION_PACKAGES") || checkPermissionRoute(authorities, "PRODUCTION_QUALITY")
        || checkPermissionRoute(authorities, "PRODUCTION_MAINTENANCE") || checkPermissionRoute(authorities, "PRODUCTION_ROUTINGS")
        || checkPermissionRoute(authorities, "PRODUCTION_STANDARD") || checkPermissionRoute(authorities, "PRODUCTION_WORK_CENTERS"),
      items: [
        {
          label: 'Hằng ngày',
          to: PATH.PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD,
          key: PATH.PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_MANUFACTURE_ORDER_STANDARD"),
        },
        {
          label: 'Trộn bột',
          to: PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER,
          key: PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_MANUFACTURE_ORDER"),
        },
        {
          label: 'Quản lý đóng gói',
          to: PATH.PRODUCTION_PACKAGES,
          key: PATH.PRODUCTION_PACKAGES,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_PACKAGES"),
        },
        {
          label: 'Quản lý kiểm tra chất lượng',
          to: PATH.PRODUCTION_QUALITY,
          key: PATH.PRODUCTION_QUALITY,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_QUALITY"),
        },
        {
          label: 'Quản lý bảo trì sản phẩm',
          to: PATH.PRODUCTION_MAINTENANCE,
          key: PATH.PRODUCTION_MAINTENANCE,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_MAINTENANCE"),
        },
        {
          label: 'Quản lý tuyến kho sản xuất',
          to: PATH.PRODUCTION_ROUTINGS,
          key: PATH.PRODUCTION_ROUTINGS,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_ROUTINGS"),
        },
        {
          label: 'Quản lý định mức sản xuất',
          to: PATH.PRODUCTION_STANDARD,
          key: PATH.PRODUCTION_STANDARD,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_STANDARD"),
        },
        {
          label: 'Quản lý cụm máy sản xuất',
          to: PATH.PRODUCTION_WORK_CENTERS,
          key: PATH.PRODUCTION_WORK_CENTERS,
          isPermitted: checkPermissionRoute(authorities, "PRODUCTION_WORK_CENTERS"),
        },
      ],
    },
    {
      key: 'logistics',
      icon: <img src="content/images/vuesax/bulk/box-2.svg" alt="box" />,
      selectedIcon: (
        <img src="content/images/vuesax/bulk/box-2.svg" alt="box-selected" />
      ),
      label: 'Logistics',
      isPermitted: checkPermissionRoute(authorities, "LOGISTICS_SUPPLIERS") || checkPermissionRoute(authorities, "LOGISTICS_SUPPLIES") ||
        checkPermissionRoute(authorities, "LOGISTICS_FACTORIES") || checkPermissionRoute(authorities, "LOGISTICS_SUPPLIER_CONTRACTS") ||
        checkPermissionRoute(authorities, "INCOMING_INVOICE") || checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE")
        || checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE_EXPORT") || checkPermissionRoute(authorities, "LOGISTICS_STOCKTAKING")
        || checkPermissionRoute(authorities, "DELIVERY_SCHEDULE"),
      items: [
        {
          label: 'Danh mục',
          isPermitted: checkPermissionRoute(authorities, "LOGISTICS_SUPPLIERS") || checkPermissionRoute(authorities, "LOGISTICS_SUPPLIES") ||
            checkPermissionRoute(authorities, "LOGISTICS_FACTORIES"),
          items: [
            {
              label: 'Nhà cung cấp',
              to: PATH.SUPPLIERS,
              key: PATH.SUPPLIERS,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_SUPPLIERS"),
            },
            {
              label: 'Hàng hoá',
              to: PATH.SUPPLIES,
              key: PATH.SUPPLIES,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_SUPPLIES"),
            },
            {
              label: 'Quản lý nhà máy',
              to: PATH.FACTORIES,
              key: PATH.FACTORIES,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_FACTORIES"),
            },
          ]
        },
        {
          label: 'Mua hàng',
          isPermitted: checkPermissionRoute(authorities, "LOGISTICS_SUPPLIER_CONTRACTS") || checkPermissionRoute(authorities, "INCOMING_INVOICE"),
          items: [
            {
              label: 'Hợp đồng mua',
              to: PATH.SUPPLIER_CONTRACTS,
              key: PATH.SUPPLIER_CONTRACTS,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_SUPPLIER_CONTRACTS"),
            },
            {
              label: 'Hoá đơn đầu vào',
              to: PATH.INCOMING_INVOICE,
              key: PATH.INCOMING_INVOICE,
              isPermitted: checkPermissionRoute(authorities, "INCOMING_INVOICE"),
            },
          ]
        },
        {
          label: 'Kho',
          isPermitted: checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE") || checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE_EXPORT")
            || checkPermissionRoute(authorities, "LOGISTICS_STOCKTAKING"),
          items: [
            {
              label: 'Nhập kho',
              to: PATH.INVENTORIES_STORAGE +
                '?warehouse=' +
                InventoriesWarehouse.WAREHOUSE_COMMERCE_IMPORT +
                '&itemType=' +
                ItemType.ITEM,
              key: PATH.INVENTORIES_STORAGE,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE"),
            },
            {
              label: 'Nhập kho(TS - CCDC)',
              to:
                PATH.INVENTORIES_STORAGE +
                '?warehouse=' +
                InventoriesWarehouse.WAREHOUSE_DEPRECIATION_IMPORT +
                '&itemType=' +
                ItemType.ITEM,
              key: PATH.INVENTORIES_STORAGE,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE"),
            },
            {
              label: 'Xuất kho',
              to:
                PATH.INVENTORIES_STORAGE_EXPORT
                +
                '?warehouse=' +
                InventoriesWarehouse.WAREHOUSE_COMMERCE_EXPORT,
              key: PATH.INVENTORIES_STORAGE_EXPORT,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE_EXPORT"),
            },
            {
              label: 'Xuất kho(TS - CCDC)',
              to:
                PATH.INVENTORIES_STORAGE_EXPORT +
                '?warehouse=' +
                InventoriesWarehouse.WAREHOUSE_DEPRECIATION_EXPORT,
              key: PATH.INVENTORIES_STORAGE_EXPORT,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_INVENTORIES_STORAGE_EXPORT"),
            },
            {
              label: 'Kiểm kê',
              to: PATH.STOCKTAKING,
              key: PATH.STOCKTAKING,
              isPermitted: checkPermissionRoute(authorities, "LOGISTICS_STOCKTAKING"),
            },
          ]
        },
        {
          label: 'Giao nhận',
          isPermitted: checkPermissionRoute(authorities, "DELIVERY_SCHEDULE"),
          items: [
            {
              label: 'Lịch giao - nhận hàng',
              to: PATH.DELIVERY_SCHEDULE,
              key: PATH.DELIVERY_SCHEDULE,
              isPermitted: checkPermissionRoute(authorities, "DELIVERY_SCHEDULE"),
            },
          ]
        },
      ],
    },
    {
      key: 'asset',
      icon: (
        <img src="content/images/vuesax/bulk/asset.svg" alt="asset-selected" />
      ),
      selectedIcon: (
        <img src="content/images/vuesax/bulk/asset.svg" alt="asset-selected" />
      ),
      label: 'Tài sản',
      isPermitted: checkPermissionRoute(authorities, "ASSET") || checkPermissionRoute(authorities, "ASSET_TRANSFER")
        || checkPermissionRoute(authorities, "ASSET_DEPRECIATION") || checkPermissionRoute(authorities, "ASSET_ALLOCATION")
        || checkPermissionRoute(authorities, "ASSET_LIQUIDATION"),
      items: [
        {
          label: 'Quản lý',
          key: 'quan-ly-ts',
          isPermitted: checkPermissionRoute(authorities, "ASSET") || checkPermissionRoute(authorities, "ASSET_TRANSFER")
            || checkPermissionRoute(authorities, "ASSET_DEPRECIATION") || checkPermissionRoute(authorities, "ASSET_ALLOCATION")
            || checkPermissionRoute(authorities, "ASSET_LIQUIDATION"),
          items: [
            {
              label: 'Hồ sơ tài sản - CCDC',
              key: PATH.ASSET,
              to: PATH.ASSET,
              isPermitted: checkPermissionRoute(authorities, "ASSET"),
            },
            {
              label: 'Điều chuyển tài sản',
              key: PATH.TRANSFER_ASSETS,
              to: PATH.TRANSFER_ASSETS,
              isPermitted: checkPermissionRoute(authorities, "ASSET_TRANSFER"),
            },
            {
              label: 'Khấu hao tài sản',
              key: PATH.DEPRECIATION,
              to: PATH.DEPRECIATION,
              isPermitted: checkPermissionRoute(authorities, "ASSET_DEPRECIATION"),
            },
            {
              label: 'Phân bổ CCDC',
              key: PATH.ALLOCATION,
              to: PATH.ALLOCATION,
              isPermitted: checkPermissionRoute(authorities, "ASSET_ALLOCATION"),
            },
            {
              label: 'Thanh lý tài sản',
              key: PATH.LIQUIDATION,
              to: PATH.LIQUIDATION,
              isPermitted: checkPermissionRoute(authorities, "ASSET_LIQUIDATION"),
            },
          ]
        }
      ],
    },
    {
      key: 'customer-services',
      icon: <img src="content/images/vuesax/bulk/CS.svg" alt="CS-se lected" />,
      selectedIcon: (
        <img src="content/images/vuesax/bulk/CS.svg" alt="CS-selected" />
      ),
      label: 'CS KH',
      isPermitted: checkPermissionRoute(authorities, "CUSTOMER_SERVICES_CALL_CENTER") || checkPermissionRoute(authorities, "CUSTOMER_SERVICES_COMPLAIN"),
      items: [
        {
          label: 'Chăm sóc khách hàng',
          key: 'cham-soc-kh',
          isPermitted: checkPermissionRoute(authorities, "CUSTOMER_SERVICES_CALL_CENTER") || checkPermissionRoute(authorities, "CUSTOMER_SERVICES_COMPLAIN"),
          items: [
            {
              label: 'Call center',
              key: PATH.CALL_CENTER,
              to: PATH.CALL_CENTER,
              isPermitted: checkPermissionRoute(authorities, "CUSTOMER_SERVICES_CALL_CENTER"),
            },
            {
              label: 'Khiếu nại',
              key: PATH.COMPLAIN,
              to: PATH.COMPLAIN,
              isPermitted: checkPermissionRoute(authorities, "CUSTOMER_SERVICES_COMPLAIN"),
            },
          ]
        },
      ],
    },
    {
      key: 'technique',
      icon: <img src="content/images/vuesax/bulk/laptop-2.svg" alt="CS-se lected" />,
      selectedIcon: (
        <img src="content/images/vuesax/bulk/laptop-2.svg" alt="CS-selected" />
      ),
      label: 'Kỹ thuật',
      isPermitted: checkPermissionRoute(authorities, "TECHNICAL_MACHINERY_EQUIPMENT"),
      items: [
        {
          label: 'Kỹ thuật',
          key: 'ky-thuat',
          isPermitted: checkPermissionRoute(authorities, "TECHNICAL_MACHINERY_EQUIPMENT"),
          items: [
            {
              label: 'Máy móc thiết bị',
              key: PATH.MACHINERY_EQUIPMENT,
              to: PATH.MACHINERY_EQUIPMENT,
              isPermitted: checkPermissionRoute(authorities, "TECHNICAL_MACHINERY_EQUIPMENT"),
            },
          ]
        }
      ]
    },
    {
      key: 'settings',
      icon: (
        <img src="content/images/vuesax/bulk/settings-02.svg" alt="setting" />
      ),
      selectedIcon: (
        <img
          src="content/images/vuesax/bulk/settings-02.svg"
          alt="setting-selected"
        />
      ),
      label: 'Cài đặt',
      isPermitted: checkPermissionRoute(authorities, "PERMISSIONS_USERS") || checkPermissionRoute(authorities, "PERMISSIONS_GROUPS")
        || checkPermissionRoute(authorities, "ANNUAL_LEAVE") || checkPermissionRoute(authorities, "UOM")
        || checkPermissionRoute(authorities, "ITEMS_SETTINGS"),
      items: [
        {
          label: 'Quản lý quyền',
          key: 'authorities-management',
          isPermitted: checkPermissionRoute(authorities, "PERMISSIONS_USERS") || checkPermissionRoute(authorities, "PERMISSIONS_GROUPS"),
          items: [
            {
              label: 'Người dùng',
              to: PATH.AUTHORITIES_USERS,
              key: PATH.AUTHORITIES_USERS,
              isPermitted: checkPermissionRoute(authorities, "PERMISSIONS_USERS"),
            },
            {
              label: 'Nhóm quyền',
              to: PATH.AUTHORITIES_GROUPS,
              key: PATH.AUTHORITIES_GROUPS,
              isPermitted: checkPermissionRoute(authorities, "PERMISSIONS_GROUPS"),
            },
          ],
        },
        {
          label: 'Phép năm',
          key: 'annualLeave',
          to: PATH.ANNUAL_LEAVE,
          isPermitted: checkPermissionRoute(authorities, "ANNUAL_LEAVE"),
        },
        {
          label: 'Quản lý đơn vị',
          key: 'uom-settings',
          to: PATH.UOM,
          isPermitted: checkPermissionRoute(authorities, "UOM"),
          // items: [
          //   {
          //     label: 'Danh sách đơn vị',
          //     to: PATH.UOM,
          //     key: PATH.UOM,
          //   },
          //   {
          //     label: 'Nhóm đơn vị',
          //     to: PATH.UOM_GROUP,
          //     key: PATH.UOM_GROUP,
          //   },
          // ],
        },
        {
          label: 'Quản lý vật phẩm',
          key: 'items-settings-header',
          isPermitted: checkPermissionRoute(authorities, "ITEMS_SETTINGS"),
          items: [
            {
              label: 'Danh sách vật phẩm',
              to: PATH.ITEMS,
              key: PATH.ITEMS,
              isPermitted: checkPermissionRoute(authorities, "ITEMS_SETTINGS"),
            },
            // {
            //   label: 'Danh sách thuộc tính',
            //   to: PATH.ITEM_ATTRIBUTES,
            //   key: PATH.ITEM_ATTRIBUTES,
            //   isPermitted: checkPermission(authorities, PermissionResource.ITEMS_SETTINGS, Action.READ),
            // },
          ],
        },
        // {
        //   label: 'Quản lý nhà cung cấp',
        //   key: 'supplier-header',
        //   isPermitted: checkPermission(authorities, PermissionResource.PERMISSION, Action.READ),
        //   items: [
        //     {
        //       label: 'Danh sách nhà cung cấp',
        //       to: PATH.SUPPLIERS,
        //       key: PATH.SUPPLIERS,
        //       isPermitted: checkPermission(authorities, PermissionResource.PERMISSION, Action.READ),
        //     },
        //     {
        //       label: 'Danh sách nhóm nhà cung cấp',
        //       to: PATH.SUPPLIERS_GROUPS,
        //       key: PATH.SUPPLIERS_GROUPS,
        //       isPermitted: checkPermission(authorities, PermissionResource.PERMISSION, Action.READ),
        //     },
        //   ],
        // },
        {
          label: 'Đồng phục',
          key: 'uniform-settings',
          to: PATH.UNIFORM_SETTINGS,
          isPermitted: checkPermissionRoute(authorities, "UNIFORM_SETTINGS"),
        },
        {
          label: 'Quản lý kho',
          key: 'warehouse-settings',
          isPermitted: true,
          items: [
            {
              label: 'Danh sách kho',
              to: PATH.WAREHOUSES,
              key: PATH.WAREHOUSES,
              isPermitted: checkPermissionRoute(authorities, "WAREHOUSES")
            },
          ],
        },
        {
          label: 'Công ty',
          key: 'company-settings',
          to: PATH.COMPANY,
          isPermitted: checkPermissionRoute(authorities, "COMPANY"),
        },
      ],
    },
  ];

  return items;
};

export const useToggleMenu = (defaultSelectedKeys: string[]) => {
  const [selectedKeys, setSelectedKeys] = useState<string[]>(
    defaultSelectedKeys ?? [],
  );

  const toggleSelectedKeys = (key: string) => {
    if (selectedKeys.includes(key)) {
      setSelectedKeys(prev => prev.filter(item => item !== key));
    } else {
      setSelectedKeys(prev => [...prev, key]);
    }
  };

  return { toggleSelectedKeys, selectedKeys, setSelectedKeys };
};
