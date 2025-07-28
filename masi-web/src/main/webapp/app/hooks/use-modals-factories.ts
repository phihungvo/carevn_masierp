import { useState } from 'react';

export const useModalsFactories = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openPropose, setOpenPropose] = useState(false);
  const [openProposeSuccess, setOpenProposeSuccess] = useState(false);
  const [openApprove, setOpenApprove] = useState(false);
  const [openApproveSign, setOpenApproveSign] = useState(false);
  const [openApproveSuccess, setOpenApproveSuccess] = useState(false);
  const [openReject, setOpenReject] = useState(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState(false);
  const [openDispose, setOpenDispose] = useState(false);
  const [openDisposeSuccess, setOpenDisposeSuccess] = useState(false);
  const [openActivate, setOpenActivate] = useState(false);
  const [openActivateSuccess, setOpenActivateSuccess] = useState(false);

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

  const togglePropose = () => {
    setOpenPropose(prev => !prev);
  };

  const toggleProposeSuccess = () => {
    setOpenProposeSuccess(prev => !prev);
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

  const toggleDispose = () => {
    setOpenDispose(prev => !prev);
  };

  const toggleDisposeSuccess = () => {
    setOpenDisposeSuccess(prev => !prev);
  };

  const toggleActivate = () => {
    setOpenActivate(prev => !prev);
  };

  const toggleActivateSuccess = () => {
    setOpenActivateSuccess(prev => !prev);
  };

  return {
    filter: { openFilter, toggleFilter },
    detail: { openDetail, toggleDetail },
    create: { openCreate, toggleCreate },
    createSuccess: { openCreateSuccess, toggleCreateSuccess },
    update: { openUpdate, toggleUpdate },
    updateSuccess: { openUpdateSuccess, toggleUpdateSuccess },
    delete: { openDelete, toggleDelete },
    deleteSuccess: { openDeleteSuccess, toggleDeleteSuccess },
    propose: { openPropose, togglePropose },
    proposeSuccess: { openProposeSuccess, toggleProposeSuccess },
    approve: { openApprove, toggleApprove },
    approveSign: { openApproveSign, toggleApproveSign },
    approveSuccess: { openApproveSuccess, toggleApproveSuccess },
    reject: { openReject, toggleReject },
    rejectSuccess: { openRejectSuccess, toggleRejectSuccess },
    dispose: { openDispose, toggleDispose },
    disposeSuccess: { openDisposeSuccess, toggleDisposeSuccess },
    activate: { openActivate, toggleActivate },
    activateSuccess: { openActivateSuccess, toggleActivateSuccess },
  };
};
