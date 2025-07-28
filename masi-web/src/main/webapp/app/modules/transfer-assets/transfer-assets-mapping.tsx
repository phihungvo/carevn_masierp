import BadgeV2 from 'app/components/badge/badge-v2';
import { ReactNode } from 'react';

export const mapTransferAssetsStatusBadge = (
  text: string,
): ReactNode => {
  switch (text) {
    case 'NEW':
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case 'COMPLETED':
      return <BadgeV2 className="bv2 pr-completed">Hoàn tất</BadgeV2>;
    case 'CANCELLED':
      return <BadgeV2 className="bv2 pr-cancelled">Hủy</BadgeV2>;
    default:
      return '';
  }
};