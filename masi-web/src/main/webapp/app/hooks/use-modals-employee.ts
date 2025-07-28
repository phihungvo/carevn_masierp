import { useState } from 'react';

export const useModalsEmployee = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openDeactivate, setOpenDeactivate] = useState(false);
  const [openDeactivateSuccess, setOpenDeactivateSuccess] = useState(false);
  const [openActivate, setOpenActivate] = useState(false);
  const [openActivateSuccess, setOpenActivateSuccess] = useState(false);
  const [openUpload, setOpenUpload] = useState(false);
  const [openUploadSuccess, setOpenUploadSuccess] = useState(false);
  const [openConfirmLeave, setOpenConfirmLeave] = useState(false);
  const [opeConfirmLeaveSuccess, setOpeConfirmLeaveSuccess] = useState(false);
  const [openImport, setOpenImport] = useState(false);
  const [openHistory, setOpenHistory] = useState(false);
  const [openImportSuccess, setOpenImportSuccess] = useState(false);
  const [openProvideAccount, setOpenProvideAccount] = useState(false);
  const [openProvideAccountSuccess, setOpenProvideAccountSuccess] = useState(false);
  const [openActivateTimkeepingDevice, setOpenActivateTimkeepingDevice] = useState(false);

  const toggleActivateTimkeepingDevicer = () => setOpenActivateTimkeepingDevice(!openActivateTimkeepingDevice);

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

  const toggleDeactivate = () => {
    setOpenDeactivate(prev => !prev);
  };

  const toggleDeactivateSuccess = () => {
    setOpenDeactivateSuccess(prev => !prev);
  };

  const toggleActivate = () => {
    setOpenActivate(prev => !prev);
  };

  const toggleActivateSuccess = () => {
    setOpenActivateSuccess(prev => !prev);
  };

  const toggleUpload = () => {
    setOpenUpload(prev => !prev);
  };

  const toggleUploadSuccess = () => {
    setOpenUploadSuccess(prev => !prev);
  };

  const toggleConfirmLeave = () => {
    setOpenConfirmLeave(prev => !prev);
  };

  const toggleConfirmLeaveSuccess = () => {
    setOpeConfirmLeaveSuccess(prev => !prev);
  };

  const toggleImport = () => {
    setOpenImport(prev => !prev);
  };

  const toggleHistory = () => {
    setOpenHistory(prev => !prev);
  };

  const toggleImportSuccess = () => {
    setOpenImportSuccess(prev => !prev);
  };

  const toggleProvideAccount = () => {
    setOpenProvideAccount(prev => !prev);
  }

  const toggleProvideAccountSuccess = () => {
    setOpenProvideAccountSuccess(prev => !prev);
  }

  return [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openDeactivate, toggleDeactivate },
    { openDeactivateSuccess, toggleDeactivateSuccess },
    { openActivate, toggleActivate },
    { openActivateSuccess, toggleActivateSuccess },
    { openUpload, toggleUpload },
    { openUploadSuccess, toggleUploadSuccess },
    { openConfirmLeave, toggleConfirmLeave },
    { opeConfirmLeaveSuccess, toggleConfirmLeaveSuccess },
    { openImport, toggleImport },
    { openHistory, toggleHistory },
    { openImportSuccess, toggleImportSuccess },
    { openProvideAccount, toggleProvideAccount },
    { openProvideAccountSuccess, toggleProvideAccountSuccess },
    { openActivateTimkeepingDevice, toggleActivateTimkeepingDevicer },
  ];
};

export const useModalsEmployeeChangeLog = () => {
  const [openDetail, setOpenDetail] = useState(false);

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

  return [{ openDetail, toggleDetail }];
};
