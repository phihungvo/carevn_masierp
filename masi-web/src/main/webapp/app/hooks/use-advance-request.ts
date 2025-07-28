import { useState } from 'react';

export const useModalsAdvanceRequest = () => {
  const [openCreateSuccess, setOpenCreateSuccess] = useState<boolean>(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState<boolean>(false);
  const [openApprovalSign, setOpenApprovalSign] = useState<boolean>(false);
  const [openApprovalSignSuccess, setOpenApprovalSignSuccess] =
    useState<boolean>(false);
  const [openReject, setOpenReject] = useState<boolean>(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState<boolean>(false);
  const [openCancel, setOpenCancel] = useState<boolean>(false);
  const [openCancelSuccess, setOpenCancelSuccess] = useState<boolean>(false);

  const toggleCancel = () => setOpenCancel(!openCancel);
  const toggleCancelSuccess = () => setOpenCancelSuccess(!openCancelSuccess);
  const toggleReject = () => setOpenReject(!openReject);
  const toggleRejectSuccess = () => setOpenRejectSuccess(!openRejectSuccess);
  const toggleApprovalSign = () => setOpenApprovalSign(!openApprovalSign);
  const toggleApprovalSignSuccess = () =>
    setOpenApprovalSignSuccess(!openApprovalSignSuccess);
  const toggleCreateSuccess = () => setOpenCreateSuccess(!openCreateSuccess);
  const toggleUpdateSuccess = () => setOpenUpdateSuccess(!openUpdateSuccess);

  return [
    { openApprovalSign, toggleApprovalSign },
    { openApprovalSignSuccess, toggleApprovalSignSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdateSuccess, toggleUpdateSuccess },
  ];
};
