import { useState } from 'react';

export const useModalsReportUniformExports = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);

  const toggleFilter = () => {
    setOpenFilter(!openFilter);
  };

  const toggleDetail = () => {
    setOpenDetail(!openDetail);
  };

  return [
    {
      openFilter,
      toggleFilter,
    },
    {
      openDetail,
      toggleDetail,
    },
  ];
};
