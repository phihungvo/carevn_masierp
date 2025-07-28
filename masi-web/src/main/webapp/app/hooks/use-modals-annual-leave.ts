import { useState } from 'react';

export const useModalsAnnualLeave = () => {
  const [openCreate, setOpenCreate] = useState<boolean>(false);
  const [openSuccess, setOpenSuccess] = useState<boolean>(false);

  const toggleCreate = () => {
    setOpenCreate(!openCreate);
  };

  const toggleSuccess = () => {
    setOpenSuccess(!openSuccess);
  };

  return [
    { openCreate, toggleCreate },
    { openSuccess, toggleSuccess },
  ];
};
