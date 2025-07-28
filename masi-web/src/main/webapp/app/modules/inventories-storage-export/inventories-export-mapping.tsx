import BadgeV2, { BadgeVariant } from 'app/components/badge/badge-v2';
import { Color } from 'app/shared/model/enumerations/color.model';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { ReactNode } from 'react';

export const inventoriesExportStatusColorMapping = (
  status: INVENTORIES_STATUS,
): Color => {
  switch (status) {
    case INVENTORIES_STATUS.NEW:
      return Color.BLUE;
    case INVENTORIES_STATUS.WAITING_APPROVED:
      return Color.PRIMARY;
    case INVENTORIES_STATUS.APPROVED:
      return Color.SUCCESS;
    case INVENTORIES_STATUS.REJECTED:
      return Color.ERROR;
    case INVENTORIES_STATUS.COMPLETED:
      return Color.SUCCESS;
    default:
      return undefined;
  }
};

export const InventoriesStorageExportStatusBadgeMapping = (
  text: INVENTORIES_STATUS,
): ReactNode => {
  switch (text) {
    case INVENTORIES_STATUS.WAITING_APPROVED:
      return <BadgeV2 className="bv2 pr-waiting_approve">Đợi duyệt</BadgeV2>;
    case INVENTORIES_STATUS.APPROVED:
      return <BadgeV2 className="bv2 pr-approved">Đã duyệt</BadgeV2>;
    case INVENTORIES_STATUS.REJECTED:
      return <BadgeV2 className="bv2 pr-rejected">Từ chối</BadgeV2>;
    case INVENTORIES_STATUS.COMPLETED:
      return <BadgeV2 className="bv2 pr-completed">Đã xuất kho</BadgeV2>;
    case INVENTORIES_STATUS.NEW:
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case INVENTORIES_STATUS.CANCELLED:
      return <BadgeV2 className="bv2 pr-cancelled">Hủy</BadgeV2>;
    default:
      return '';
  }
};

export const inventoriesExportStatusTextMapping = (
  status: INVENTORIES_STATUS,
): string => {
  switch (status) {
    case INVENTORIES_STATUS.NEW:
      return 'Mới';
    case INVENTORIES_STATUS.WAITING_APPROVED:
      return 'Chờ duyệt';
    case INVENTORIES_STATUS.APPROVED:
      return 'Đã duyệt';
    case INVENTORIES_STATUS.REJECTED:
      return 'Từ chối';
    case INVENTORIES_STATUS.CANCELLED:
      return 'Hủy';
    default:
      return '';
  }
};

export const InventoriesExportStatusOptions = [
  { value: INVENTORIES_STATUS.WAITING_APPROVED, label: 'Đợi duyệt' },
  { value: INVENTORIES_STATUS.APPROVED, label: 'Đã duyệt' },
  { value: INVENTORIES_STATUS.REJECTED, label: 'Từ chối' },
  { value: INVENTORIES_STATUS.COMPLETED, label: 'Đã xuất kho' },
  { value: INVENTORIES_STATUS.NEW, label: 'Mới' },
  { value: INVENTORIES_STATUS.CANCELLED, label: 'Hủy' },
];
