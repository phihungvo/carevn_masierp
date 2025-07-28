import BadgeV2 from 'app/components/badge/badge-v2';
import { Color } from 'app/shared/model/enumerations/color.model';
import { STOCKTAKING_STATUS } from 'app/shared/model/stocktaking.model';
import { ReactNode } from 'react';

export const stocktakingStatusColorMapping = (
  status: STOCKTAKING_STATUS,
): Color => {
  switch (status) {
    case STOCKTAKING_STATUS.NEW:
      return Color.BLUE;
    case STOCKTAKING_STATUS.WAITING_APPROVED:
      return Color.PRIMARY;
    case STOCKTAKING_STATUS.APPROVED:
      return Color.SUCCESS;
    case STOCKTAKING_STATUS.REJECTED:
      return Color.ERROR;
    case STOCKTAKING_STATUS.COMPLETED:
      return Color.SUCCESS;
    default:
      return undefined;
  }
};

export const StocktakingStatusBadgeMapping = (
  text: STOCKTAKING_STATUS,
): ReactNode => {
  switch (text) {
    case STOCKTAKING_STATUS.WAITING_APPROVED:
      return <BadgeV2 className="bv2 pr-waiting_approve">Đợi duyệt</BadgeV2>;
    case STOCKTAKING_STATUS.APPROVED:
      return <BadgeV2 className="bv2 pr-approved">Đã duyệt</BadgeV2>;
    case STOCKTAKING_STATUS.REJECTED:
      return <BadgeV2 className="bv2 pr-rejected">Từ chối</BadgeV2>;
    case STOCKTAKING_STATUS.NEW:
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case STOCKTAKING_STATUS.CANCELLED:
      return <BadgeV2 className="bv2 pr-cancelled">Hủy</BadgeV2>;
    default:
      return '';
  }
};

export const stocktakingStatusTextMapping = (
  status: STOCKTAKING_STATUS,
): string => {
  switch (status) {
    case STOCKTAKING_STATUS.NEW:
      return 'Mới';
    case STOCKTAKING_STATUS.WAITING_APPROVED:
      return 'Đợi duyệt';
    case STOCKTAKING_STATUS.APPROVED:
      return 'Đã duyệt';
    case STOCKTAKING_STATUS.REJECTED:
      return 'Từ chối';
    case STOCKTAKING_STATUS.CANCELLED:
      return 'Hủy';
    default:
      return '';
  }
};

export const StocktakingStatusOptions = [
  { value: STOCKTAKING_STATUS.WAITING_APPROVED, label: 'Đợi duyệt' },
  { value: STOCKTAKING_STATUS.APPROVED, label: 'Đã duyệt' },
  { value: STOCKTAKING_STATUS.REJECTED, label: 'Từ chối' },
  { value: STOCKTAKING_STATUS.NEW, label: 'Mới' },
  { value: STOCKTAKING_STATUS.CANCELLED, label: 'Hủy' },
];
