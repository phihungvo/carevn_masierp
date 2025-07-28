import BadgeV2 from 'app/components/badge/badge-v2';
import React from 'react';

const mapFactoriesActiveText = (status: boolean): string => {
  switch (status) {
    case true:
      return 'Đang hoạt động';
    case false:
      return 'Ngưng hoạt động';
    default:
      return '';
  }
};

const mapFactoriesActiveBadge = (status: boolean) => {
  switch (status) {
    case true:
      return <BadgeV2 color={'primary'}>Đang hoạt động</BadgeV2>;
    case false:
      return <BadgeV2 color={'error'}>Ngưng hoạt động</BadgeV2>;
    default:
      return <></>;
  }
};

export default {
  mapFactoriesActiveText,
  mapFactoriesActiveBadge,
};
