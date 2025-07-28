import BadgeV2 from 'app/components/badge/badge-v2';
import { PRODUCTION_PACKAGES_STATUS } from 'app/shared/model/enumerations/production-packages.model';
import { ReactNode } from 'react';

export const mapProductionPackagesStatusText = (
  status: PRODUCTION_PACKAGES_STATUS,
): string => {
  switch (status) {
    case PRODUCTION_PACKAGES_STATUS.COMPLETED:
      return 'Đã may bao';
    case PRODUCTION_PACKAGES_STATUS.WAITING:
      return 'Chưa may bao';
    default:
      return '';
  }
};

export const mapProductionPackagesStatusOptions = [
  {
    value: PRODUCTION_PACKAGES_STATUS.WAITING,
    label: 'Chưa may bao',
  },
  {
    value: PRODUCTION_PACKAGES_STATUS.COMPLETED,
    label: 'Đã may bao',
  },
];

export const productionPackagesStatusBadgeMapping = (
  text: PRODUCTION_PACKAGES_STATUS,
): ReactNode => {
  const label = mapProductionPackagesStatusText(text);
  switch (text) {
    case PRODUCTION_PACKAGES_STATUS.COMPLETED:
      return <BadgeV2 className="bv2 pr-approved">{label}</BadgeV2>;
    case PRODUCTION_PACKAGES_STATUS.WAITING:
      return <BadgeV2 className="bv2 pr-rejected">{label}</BadgeV2>;
    default:
      return '';
  }
};
