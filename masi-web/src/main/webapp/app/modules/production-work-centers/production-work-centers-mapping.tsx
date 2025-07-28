import BadgeV2 from 'app/components/badge/badge-v2';
import { WORK_CENTER_STATUS } from 'app/shared/model/enumerations/work-center.model';
import { ReactNode } from 'react';

export const mapProdWorkCenterStatusText = (
  status: WORK_CENTER_STATUS,
): string => {
  switch (status) {
    case WORK_CENTER_STATUS.ACTIVE:
      return 'Đang hoạt động';
    case WORK_CENTER_STATUS.DAMAGED:
      return 'Hỏng';
    case WORK_CENTER_STATUS.REPAIR:
      return 'Đang sửa chữa';
    case WORK_CENTER_STATUS.PENDING:
      return 'Chờ thanh lý';
    case WORK_CENTER_STATUS.LIQUIDATE:
      return 'Đã thanh lý';
    default:
      return '';
  }
};

export const mapProdWorkCenterStatusOptions = [
  { value: WORK_CENTER_STATUS.ACTIVE, label: 'Đang hoạt động' },
  { value: WORK_CENTER_STATUS.DAMAGED, label: 'Hỏng' },
  { value: WORK_CENTER_STATUS.REPAIR, label: 'Đang sửa chữa' },
  { value: WORK_CENTER_STATUS.PENDING, label: 'Chờ thanh lý' },
  { value: WORK_CENTER_STATUS.LIQUIDATE, label: 'Đã thanh lý' },
];

export const prodWorkCenterStatusBadgeMapping = (
  text: WORK_CENTER_STATUS,
): ReactNode => {
  const label = mapProdWorkCenterStatusText(text);
  switch (text) {
    case WORK_CENTER_STATUS.ACTIVE:
      return <BadgeV2 className="bv2 pr-approved">{label}</BadgeV2>;
    case WORK_CENTER_STATUS.DAMAGED:
      return <BadgeV2 className="bv2 pr-rejected">{label}</BadgeV2>;
    case WORK_CENTER_STATUS.REPAIR:
      return <BadgeV2 className="bv2 pr-primary">{label}</BadgeV2>;
    case WORK_CENTER_STATUS.PENDING:
      return <BadgeV2 className="bv2 pr-waiting_approve">{label}</BadgeV2>;
    case WORK_CENTER_STATUS.LIQUIDATE:
      return <BadgeV2 className="bv2 pr-cancelled">{label}</BadgeV2>;
    default:
      return '';
  }
};
