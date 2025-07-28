import { useState } from 'react';

export const useModalsUniform = () => {
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);

  const toggleCreate = () => {
    setOpenCreate(prev => !prev);
  };

  const toggleCreateSuccess = () => {
    setOpenCreateSuccess(prev => !prev);
  };

  return [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
  ];
};

export const useModalsUniformOrders = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openApprove, setOpenApprove] = useState(false);
  const [openApproveSign, setOpenApproveSign] = useState(false);
  const [openApproveSuccess, setOpenApproveSuccess] = useState(false);
  const [openReject, setOpenReject] = useState(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState(false);
  const [openCancel, setOpenCancel] = useState(false);
  const [openCancelSuccess, setOpenCancelSuccess] = useState(false);
  const [openStock, setOpenStock] = useState(false);
  const [openStockSuccess, setOpenStockSuccess] = useState(false);

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

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

  const toggleDelete = () => {
    setOpenDelete(prev => !prev);
  };

  const toggleDeleteSuccess = () => {
    setOpenDeleteSuccess(prev => !prev);
  };

  const toggleApprove = () => {
    setOpenApprove(prev => !prev);
  };

  const toggleApproveSign = () => {
    setOpenApproveSign(prev => !prev);
  };

  const toggleApproveSuccess = () => {
    setOpenApproveSuccess(prev => !prev);
  };

  const toggleReject = () => {
    setOpenReject(prev => !prev);
  };

  const toggleRejectSuccess = () => {
    setOpenRejectSuccess(prev => !prev);
  };

  const toggleCancel = () => {
    setOpenCancel(prev => !prev);
  };

  const toggleCancelSuccess = () => {
    setOpenCancelSuccess(prev => !prev);
  };

  const toggleStock = () => {
    setOpenStock(prev => !prev);
  };

  const toggleStockSuccess = () => {
    setOpenStockSuccess(prev => !prev);
  };

  return [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSuccess, toggleApproveSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
    { openStock, toggleStock },
    { openStockSuccess, toggleStockSuccess },
  ];
};

export const useModalsUniformExports = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleCreate = () => {
    setOpenCreate(prev => !prev);
  };

  const toggleCreateSuccess = () => {
    setOpenCreateSuccess(prev => !prev);
  };

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

  return [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openDetail, toggleDetail },
  ];
};

export const useModalsUom = () => {
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);

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

  const toggleDelete = () => {
    setOpenDelete(prev => !prev);
  };

  const toggleDeleteSuccess = () => {
    setOpenDeleteSuccess(prev => !prev);
  };

  return [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ];
};

export const useModalsUniformSettings = () => {
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openActive, setOpenActive] = useState(false);
  const [openActiveSuccess, setOpenActiveSuccess] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);

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

  const toggleDelete = () => {
    setOpenDelete(prev => !prev);
  };

  const toggleDeleteSuccess = () => {
    setOpenDeleteSuccess(prev => !prev);
  };

  const toggleActive = () => {
    setOpenActive(prev => !prev);
  };

  const toggleActiveSuccess = () => {
    setOpenActiveSuccess(prev => !prev);
  };

  const toggleOpenDetail = () => {
    setOpenDetail(prev => !prev);
  };

  return [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openActive, toggleActive },
    { openActiveSuccess, toggleActiveSuccess },
    { openDetail, toggleOpenDetail },
  ];
};
