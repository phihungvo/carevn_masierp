import { useState } from 'react';

export const useModalsSuppliesRequest = () => {
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openReview, setOpenReview] = useState(false);
  const [openReviewSuccess, setOpenReviewSuccess] = useState(false);
  const [openApproveProcess, setOpenApproveProcess] = useState(false);
  const [openApprove, setOpenApprove] = useState(false);
  const [openApproveSuccess, setOpenApproveSuccess] = useState(false);
  const [openReject, setOpenReject] = useState(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState(false);

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

  const toggleOpenDetail = () => {
    setOpenDetail(prev => !prev);
  };

  const toggleReview = () => {
    setOpenReview(prev => !prev);
  };

  const toggleReviewSuccess = () => {
    setOpenReviewSuccess(prev => !prev);
  };

  const toggleApproveProcess = () => {
    setOpenApproveProcess(prev => !prev);
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

  return [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDetail, toggleOpenDetail },
    { openReview, toggleReview },
    { openReviewSuccess, toggleReviewSuccess },
    { openApproveProcess, toggleApproveProcess },
    { openApprove, toggleApprove },
    { openApproveSuccess, toggleApproveSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
  ];
};
