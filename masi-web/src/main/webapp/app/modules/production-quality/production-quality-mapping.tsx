import BadgeV2 from 'app/components/badge/badge-v2';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';
import { ReactNode } from 'react';

export const mapProductionQualityStatusText = (
  status: PRODUCTION_QUALITY_STATUS,
): string => {
  switch (status) {
    case PRODUCTION_QUALITY_STATUS.AWAITING_DISPOSE:
      return 'Đợi hủy';
    case PRODUCTION_QUALITY_STATUS.DISPOSED:
      return 'Đã duyệt';
    case PRODUCTION_QUALITY_STATUS.NEW:
      return 'Chưa hủy';
    case PRODUCTION_QUALITY_STATUS.REJECTED:
      return 'Đã hủy';
    default:
      return '';
  }
};

export const productionQualityStatusBadgeMapping = (
  text: PRODUCTION_QUALITY_STATUS,
): ReactNode => {
  const label = mapProductionQualityStatusText(text);
  switch (text) {
    case PRODUCTION_QUALITY_STATUS.AWAITING_DISPOSE:
      return <BadgeV2 className="bv2 pr-waiting_approve">{label}</BadgeV2>;
    case PRODUCTION_QUALITY_STATUS.DISPOSED:
      return <BadgeV2 className="bv2 pr-approved">{label}</BadgeV2>;
    case PRODUCTION_QUALITY_STATUS.REJECTED:
      return <BadgeV2 className="bv2 pr-cancelled">{label}</BadgeV2>;
    case PRODUCTION_QUALITY_STATUS.NEW:
      return <BadgeV2 className="bv2 pr-processing">{label}</BadgeV2>;
    default:
      return '';
  }
};
