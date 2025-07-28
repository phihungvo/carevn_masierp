import BadgeV2 from 'app/components/badge/badge-v2';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { ReactNode } from 'react';

export const mapManufactureOrderStatusText = (
  status: MANUFACTURE_ORDER_STATUS,
): string => {
  switch (status) {
    case MANUFACTURE_ORDER_STATUS.NEW:
      return 'Mới';
    case MANUFACTURE_ORDER_STATUS.MATERIAL:
      return 'Nguyên liệu';
    case MANUFACTURE_ORDER_STATUS.ADDITIVES:
      return 'Phụ gia';
    case MANUFACTURE_ORDER_STATUS.PRODUCTION:
      return 'Sản xuất';
    case MANUFACTURE_ORDER_STATUS.PACKAGING:
      return 'Đóng gói';
    case MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED:
      return 'Nhập lô';
    case MANUFACTURE_ORDER_STATUS.SHIPPED:
      return 'Nhập kho';
    case MANUFACTURE_ORDER_STATUS.WAREHOUSED:
      return 'Đã nhập kho';
    case MANUFACTURE_ORDER_STATUS.COMPLETED:
      return 'Hoàn thành';
    case MANUFACTURE_ORDER_STATUS.CANCELLED:
      return 'Hủy';
    default:
      return '';
  }
};

export const manufactureOrderBadgeMapping = (
  text: MANUFACTURE_ORDER_STATUS,
): ReactNode => {
  switch (text) {
    case MANUFACTURE_ORDER_STATUS.NEW:
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.MATERIAL:
      return <BadgeV2 className="bv2 pr-primary">Nguyên liệu</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.ADDITIVES:
      return <BadgeV2 className="bv2 pr-new">Phụ gia</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.PRODUCTION:
      return <BadgeV2 className="bv2 pr-new">Sản xuất</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.PACKAGING:
      return <BadgeV2 className="bv2 pr-primary">Đóng gói</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED:
      return <BadgeV2 className="bv2 pr-primary">Nhập lô</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.SHIPPED:
      return <BadgeV2 className="bv2 pr-primary">Nhập kho</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.WAREHOUSED:
      return <BadgeV2 className="bv2 pr-liquidation">Đã nhập kho</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.COMPLETED:
      return <BadgeV2 className="bv2 pr-approved">Hoàn thành</BadgeV2>;
    case MANUFACTURE_ORDER_STATUS.CANCELLED:
      return <BadgeV2 className="bv2 pr-cancelled">Hủy</BadgeV2>;
    default:
      return '';
  }
};

export const mapProductionDashboardStatusOptions = [
  {
    value: MANUFACTURE_ORDER_STATUS.NEW,
    label: 'Mới',
  },
  {
    value: MANUFACTURE_ORDER_STATUS.MATERIAL,
    label: 'Nguyên liệu',
  },
  {
    value: MANUFACTURE_ORDER_STATUS.ADDITIVES,
    label: 'Phụ gia',
  },
  {
    value: MANUFACTURE_ORDER_STATUS.PRODUCTION,
    label: 'Sản xuất',
  },
  {
    value: MANUFACTURE_ORDER_STATUS.PACKAGING,
    label: 'Đóng gói',
  },
  {
    value: MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED,
    label: 'Nhập lô',
  },
  {
    value: MANUFACTURE_ORDER_STATUS.SHIPPED,
    label: 'Nhập kho',
  },
  {
    value: MANUFACTURE_ORDER_STATUS.COMPLETED,
    label: 'Hoàn thành',
  },
];
