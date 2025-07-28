import BadgeV2 from 'app/components/badge/badge-v2';
import { PRODUCTION_STANDARD_STATUS } from 'app/shared/model/enumerations/production-standard.model';
import { ReactNode } from 'react';

export const mapProductionStandardStatusText = (
  status: PRODUCTION_STANDARD_STATUS,
): string => {
  switch (status) {
    case PRODUCTION_STANDARD_STATUS.NEW:
      return 'Mới';
    case PRODUCTION_STANDARD_STATUS.CANCELED:
      return 'Đã hủy';
    default:
      return '';
  }
};

export const productionStandardStatusBadgeMapping = (
  text: PRODUCTION_STANDARD_STATUS,
): ReactNode => {
  switch (text) {
    case PRODUCTION_STANDARD_STATUS.NEW:
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case PRODUCTION_STANDARD_STATUS.CANCELED:
      return <BadgeV2 className="bv2 pr-cancelled">Hủy</BadgeV2>;
    default:
      return '';
  }
};
