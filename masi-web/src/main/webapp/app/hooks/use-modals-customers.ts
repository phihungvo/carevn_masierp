import { useState } from 'react';

export const useModalsCustomers = () => {
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openFilter, setOpenFilter] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openDispose, setOpenDispose] = useState(false);
  const [openDisposeSuccess, setOpenDisposeSuccess] = useState(false);
  const [openTransfer, setOpenTransfer] = useState(false);
  const [openTransferSuccess, setOpenTransferSuccess] = useState(false);

  const toggleCreate = () => {
    setOpenCreate(prev => !prev);
  };

  const toggleCreateSuccess = () => {
    setOpenCreateSuccess(prev => !prev);
  };

  const toggleUpdate = () => {
    setOpenUpdate(prev => !prev);
  };

  const toggleUpdateSuccess = () => {
    setOpenUpdateSuccess(prev => !prev);
  };

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

  const toggleDispose = () => {
    setOpenDispose(prev => !prev);
  };

  const toggleDisposeSuccess = () => {
    setOpenDisposeSuccess(prev => !prev);
  };

  const toggleTransfer = () => {
    setOpenTransfer(prev => !prev);
  };

  const toggleTransferSuccess = () => {
    setOpenTransferSuccess(prev => !prev);
  };

  return [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openDispose, toggleDispose },
    { openDisposeSuccess, toggleDisposeSuccess },
    { openTransfer, toggleTransfer },
    { openTransferSuccess, toggleTransferSuccess },
  ];
};
