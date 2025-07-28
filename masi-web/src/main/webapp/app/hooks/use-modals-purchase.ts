import { useState } from 'react';

export const useModalsPurchase = () => {
  const [openFilter, setOpenFilter] = useState<boolean>(false);
  const [openDetail, setOpenDetail] = useState<boolean>(false);
  const [openCreate, setOpenCreate] = useState<boolean>(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState<boolean>(false);
  const [openUpdate, setOpenUpdate] = useState<boolean>(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState<boolean>(false);
  const [openDelete, setOpenDelete] = useState<boolean>(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState<boolean>(false);
  const [openDeleteError, setOpenDeleteError] = useState<boolean>(false);
  const [openStatus, setOpenStatus] = useState<boolean>(false);
  const [openStatusSuccess, setOpenStatusSuccess] = useState<boolean>(false);
  const [openRequest, setOpenRequest] = useState<boolean>(false);
  const [openRequestSuccess, setOpenRequestSuccess] = useState<boolean>(false);
  const [openRequestError, setOpenRequestError] = useState<boolean>(false);
  const [openApprove, setOpenApprove] = useState<boolean>(false);
  const [openApproveSign, setOpenApproveSign] = useState<boolean>(false);
  const [openApproveSignSuccess, setOpenApproveSignSuccess] = useState<boolean>(false);
  const [openReject, setOpenReject] = useState<boolean>(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState<boolean>(false);

  const toggleFilter = () => setOpenFilter(!openFilter);

  const toggleCreate = () => setOpenCreate(!openCreate);

  const toggleDetail = () => setOpenDetail(!openDetail);

  const toggleCreateSuccess = () => setOpenCreateSuccess(!openCreateSuccess);

  const toggleUpdate = () => setOpenUpdate(!openUpdate);

  const toggleUpdateSuccess = () => setOpenUpdateSuccess(!openUpdateSuccess);

  const toggleDelete = () => setOpenDelete(!openDelete);

  const toggleDeleteSuccess = () => setOpenDeleteSuccess(!openDeleteSuccess);

  const toggleDeleteError = () => setOpenDeleteError(!openDeleteError);

  const toggleStatus = () => setOpenStatus(!openStatus);

  const toggleStatusSuccess = () => setOpenStatusSuccess(!openStatusSuccess);

  const toggleRequest = () => setOpenRequest(!openRequest);

  const toggleRequestSuccess = () => setOpenRequestSuccess(!openRequestSuccess);

  const toggleRequestError = () => setOpenRequestError(!openRequestError);

  const toggleApprove = () => setOpenApprove(!openApprove);

  const toggleApproveSign = () => setOpenApproveSign(!openApproveSign);

  const toggleReject = () => setOpenReject(!openReject);

  const toggleApproveSignSuccess = () => setOpenApproveSignSuccess(!openApproveSignSuccess);

  const toggleRejectSuccess = () => setOpenRejectSuccess(!openRejectSuccess);

  return [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openDetail, toggleDetail },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDeleteError, toggleDeleteError },
    { openStatus, toggleStatus },
    { openStatusSuccess, toggleStatusSuccess },
    { openRequest, toggleRequest },
    { openRequestSuccess, toggleRequestSuccess },
    { openRequestError, toggleRequestError },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
  ];
};
