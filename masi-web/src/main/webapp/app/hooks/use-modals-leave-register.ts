import { useState } from 'react';

export const useModalsLeaveRegister = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openApprove, setOpenApprove] = useState(false);
  const [openApproveSuccess, setOpenApproveSuccess] = useState(false);
  const [openCancel, setOpenCancel] = useState(false);
  const [openCancelSuccess, setOpenCancelSuccess] = useState(false);
  const [openReject, setOpenReject] = useState(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openPropose, setOpenPropose] = useState(false);
  const [openProposeSuccess, setOpenProposeSuccess] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openApproveSign, setOpenApproveSign] = useState(false);

  const toggleApproveSign = () => {
    setOpenApproveSign(prev => !prev);
  };

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
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

  const toggleApprove = () => {
    setOpenApprove(prev => !prev);
  };

  const toggleApproveSuccess = () => {
    setOpenApproveSuccess(prev => !prev);
  };

  const toggleCancel = () => {
    setOpenCancel(prev => !prev);
  };

  const toggleCancelSuccess = () => {
    setOpenCancelSuccess(prev => !prev);
  };

  const toggleReject = () => {
    setOpenReject(prev => !prev);
  };

  const toggleRejectSuccess = () => {
    setOpenRejectSuccess(prev => !prev);
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

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

  return [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openApprove, toggleApprove },
    { openApproveSuccess, toggleApproveSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openPropose, togglePropose },
    { openProposeSuccess, toggleProposeSuccess },
    { openDetail, toggleDetail },
    { openApproveSign, toggleApproveSign },
  ];
};
