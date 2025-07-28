import { useState } from 'react';

export const useModalReportUniformsExpired = () => {
  const [openModalFilter, setOpenModalFilter] = useState<boolean>(false);
  const [openModalHistory, setOpenModalHistory] = useState<boolean>(false);

  const toggleModalFilter = () => setOpenModalFilter(prev => !prev);

  const toggleModalHistory = () => setOpenModalHistory(prev => !prev);

  return [
    { openModalFilter, toggleModalFilter },
    { openModalHistory, toggleModalHistory },
  ];
};
