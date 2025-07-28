import { useState } from 'react';

export const useModalPriceList = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openUpdateError, setOpenUpdateError] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openDeleteError, setOpenDeleteError] = useState(false);
  const [openInApprove, setOpenInApprove] = useState(false);
  const [openInApproveSuccess, setOpenInApproveSuccess] = useState(false);
  const [openCancel, setOpenCancel] = useState(false);
  const [openCancelSuccess, setOpenCancelSuccess] = useState(false);
  const [openCancelError, setOpenCancelError] = useState(false);
  const [openCusSend, setOpenCusSend] = useState(false);
  const [openCusSendSuccess, setOpenCusSendSuccess] = useState(false);
  const [openCusSendError, setOpenCusSendError] = useState(false);
  const [openApproveInternal, setOpenApproveInternal] = useState(false);
  const [openApproveInternalSuccess, setOpenApproveInternalSuccess] = useState(false);
  const [openApprove, setOpenApprove] = useState(false);
  const [openApproveSuccess, setOpenApproveSuccess] = useState(false);
  const [openReject, setOpenReject] = useState(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState(false);
  const [openApproveSign, setOpenApproveSign] = useState(false);
  const [openApproveSignSuccess, setOpenApproveSignSuccess] = useState(false);
  const [openRejectInternal, setOpenRejectInternal] = useState(false);
  const [openRejectInternalSucess, setOpenRejectInternalSucess] = useState(false);

  const toggleRejectInternalSucess = () => setOpenRejectInternalSucess(!openRejectInternalSucess)
  const toggleRejectInternal = () => setOpenRejectInternal(!openRejectInternal)
  const toggleApproveSignSuccess = () => setOpenApproveSignSuccess(!openApproveSignSuccess)
  const toggleApproveSign = () => setOpenApproveSign(!openApproveSign)

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleUpdateError = () => {
    setOpenUpdateError(prev => !prev);
  };

  const toggleDelete = () => {
    setOpenDelete(prev => !prev);
  };

  const toggleDeleteSuccess = () => {
    setOpenDeleteSuccess(prev => !prev);
  };

  const toggleDeleteError = () => {
    setOpenDeleteError(prev => !prev);
  };

  const toggleInApprove = () => {
    setOpenInApprove(prev => !prev);
  };

  const toggleInApproveSuccess = () => {
    setOpenInApproveSuccess(prev => !prev);
  };

  const toggleCancel = () => {
    setOpenCancel(prev => !prev);
  };

  const toggleCancelSuccess = () => {
    setOpenCancelSuccess(prev => !prev);
  };

  const toggleCancelError = () => {
    setOpenCancelError(prev => !prev);
  };

  const toggleCusSend = () => {
    setOpenCusSend(prev => !prev);
  };

  const toggleCusSendSuccess = () => {
    setOpenCusSendSuccess(prev => !prev);
  };

  const toggleCusSendError = () => {
    setOpenCusSendError(prev => !prev);
  };

  const toggleApprove = () => {
    setOpenApprove(prev => !prev);
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

  const toggleApproveInternal = () => {
    setOpenApproveInternal(prev => !prev);
  };

  const toggleApproveInternalSuccess = () => {
    setOpenApproveInternalSuccess(prev => !prev);
  };

  return [
    { openFilter, toggleFilter },
    { openUpdateError, toggleUpdateError },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDeleteError, toggleDeleteError },
    { openInApprove, toggleInApprove },
    { openInApproveSuccess, toggleInApproveSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
    { openCancelError, toggleCancelError },
    { openCusSend, toggleCusSend },
    { openCusSendSuccess, toggleCusSendSuccess },
    { openCusSendError, toggleCusSendError },
    { openApprove, toggleApprove },
    { openApproveSuccess, toggleApproveSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openApproveInternal, toggleApproveInternal },
    { openApproveInternalSuccess, toggleApproveInternalSuccess },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openRejectInternal, toggleRejectInternal },
    { openRejectInternalSucess, toggleRejectInternalSucess },
  ];
};
