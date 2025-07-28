import Badge from 'app/components/badge/badge';
import React from 'react';

const mapSuppliesStatusText = (status: boolean): string => {
  switch (status) {
    case true:
      return 'Hoạt động';
    case false:
      return 'Không hoạt động';
    default:
      return '';
  }
};

const mapSuppliesStatusBadge = (status: boolean) => {
  switch (status) {
    case true:
      return <Badge color={'success'}>Hoạt động</Badge>;
    case false:
      return <Badge color={'error'}>Không hoạt động</Badge>;
    default:
      return <></>;
  }
};

export default {
  mapSuppliesStatusText,
  mapSuppliesStatusBadge,
};
