import { useState } from 'react';

export const useModalsProductionQuality = () => {
  const [isOpenFilter, setIsOpenFilter] = useState(false);
  const [isOpenCreateSuccess, setIsOpenCreateSuccess] = useState(false);
  const [isOpenUpdateSuccess, setIsOpenUpdateSuccess] = useState(false);
  const [isOpenRejectCancel, setIsOpenRejectCancel] = useState(false);
  const [isOpenRejectCancelSuccess, setIsOpenRejectCancelSuccess] =
    useState(false);
  const [isOpenApprove, setIsOpenApprove] = useState(false);
  const [isOpenApproveSuccess, setIsOpenApproveSuccess] = useState(false);
  const [isOpenDelete, setIsOpenDelete] = useState(false);
  const [isOpenDeleteSuccess, setIsOpenDeleteSuccess] = useState(false);
  const [isOpenPass, setIsOpenPass] = useState(false);
  const [isOpenPassSuccess, setIsOpenPassSuccess] = useState(false);
  const [selectedRecord, setSelectedRecord] = useState<string>('');

  const [isOpenCancel, setIsOpenCancel] = useState(false);
  const [isOpenCancelSuccess, setIsOpenCancelSuccess] = useState(false);

  const toggleModalPass = () => setIsOpenPass(!isOpenPass);
  const toggleModalPassSuccess = () => setIsOpenPassSuccess(!isOpenPassSuccess);

  const toggleFilter = () => setIsOpenFilter(prev => !prev);

  /* MODAL REJECT CANCEL TESTING SAMPLE */
  const toggleRejectCancel = () => setIsOpenRejectCancel(prev => !prev);

  /* MODAL REJECT CANCEL TESTING SAMPLE SUCCESS */
  const toggleRejectCancelSuccess = () =>
    setIsOpenRejectCancelSuccess(prev => !prev);

  const toggleApprove = () => setIsOpenApprove(prev => !prev);

  const toggleApproveSuccess = () => setIsOpenApproveSuccess(prev => !prev);

  /* MODAL DELETE TESTING SAMPLE */
  const toggleDelete = () => setIsOpenDelete(prev => !prev);

  /* MODAL DELETE TESTING SAMPLE SUCCESS */
  const toggleDeleteSuccess = () => setIsOpenDeleteSuccess(prev => !prev);

  const toggleCancel = () => setIsOpenCancel(prev => !prev);
  const toggleCancelSuccess = () => setIsOpenCancelSuccess(prev => !prev);

  const toggleCreateSuccess = () => setIsOpenCreateSuccess(prev => !prev);
  const toggleUpdateSuccess = () => setIsOpenUpdateSuccess(prev => !prev);

  return [
    { isOpenFilter, toggleFilter },
    { isOpenCreateSuccess, toggleCreateSuccess },
    { isOpenUpdateSuccess, toggleUpdateSuccess },
    { isOpenApprove, toggleApprove },
    { isOpenApproveSuccess, toggleApproveSuccess },
    { isOpenRejectCancel, toggleRejectCancel },
    { isOpenRejectCancelSuccess, toggleRejectCancelSuccess },
    { isOpenCancel, toggleCancel },
    { isOpenCancelSuccess, toggleCancelSuccess },
    { isOpenDelete, toggleDelete },
    { isOpenDeleteSuccess, toggleDeleteSuccess },
    { isOpenPass, toggleModalPass },
    { isOpenPassSuccess, toggleModalPassSuccess },
    { selectedRecord, setSelectedRecord },
  ];
};
