import { useState } from 'react';

export const useModalsCustomersTransfer = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openTransfer, setOpenTransfer] = useState(false);
  const [openTransferSuccess, setOpenTransferSuccess] = useState(false);

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleCreate = () => {
    setOpenCreate(prev => !prev);
  };

  const toggleCreateSuccess = () => {
    setOpenCreateSuccess(prev => !prev);
  };

  const toggleTransfer = () => {
    setOpenTransfer(prev => !prev);
  };

  const toggleTransferSuccess = () => {
    setOpenTransferSuccess(prev => !prev);
  };

  return [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openTransfer, toggleTransfer },
    { openTransferSuccess, toggleTransferSuccess },
  ];
};
