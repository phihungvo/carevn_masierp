import { useState } from 'react';

export const useModalsRecruitment = () => {
  const [openFilter, setOpenFilter] = useState<boolean>(false);
  const [openDetail, setOpenDetail] = useState<boolean>(false);
  const [openCreate, setOpenCreate] = useState<boolean>(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState<boolean>(false);
  const [openUpdate, setOpenUpdate] = useState<boolean>(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState<boolean>(false);
  const [openDelete, setOpenDelete] = useState<boolean>(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState<boolean>(false);
  const [openApprove, setOpenApprove] = useState<boolean>(false);
  const [openApproveSign, setOpenApproveSign] = useState<boolean>(false);
  const [openApproveSignSuccess, setOpenApproveSignSuccess] = useState<boolean>(false);
  const [openReject, setOpenReject] = useState<boolean>(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState<boolean>(false);
  const [openSchedule, setOpenSchedule] = useState<boolean>(false);
  const [openScheduleSuccess, setOpenScheduleSuccess] = useState<boolean>(false);
  const [openRenew, setOpenRenew] = useState<boolean>(false);
  const [openRenewSuccess, setOpenRenewSuccess] = useState<boolean>(false);
  const [openHistory, setOpenHistory] = useState<boolean>(false);

  const toggleHistory = () => setOpenHistory(!openHistory);

  const toggleRenewSuccess = () => setOpenRenewSuccess(!openRenewSuccess);

  const toggleRenew = () => setOpenRenew(!openRenew);

  const toggleFilter = () => setOpenFilter(!openFilter);

  const toggleCreate = () => setOpenCreate(!openCreate);

  const toggleDetail = () => setOpenDetail(!openDetail);

  const toggleCreateSuccess = () => setOpenCreateSuccess(!openCreateSuccess);

  const toggleUpdate = () => setOpenUpdate(!openUpdate);

  const toggleUpdateSuccess = () => setOpenUpdateSuccess(!openUpdateSuccess);

  const toggleDelete = () => setOpenDelete(!openDelete);

  const toggleDeleteSuccess = () => setOpenDeleteSuccess(!openDeleteSuccess);

  const toggleApprove = () => setOpenApprove(!openApprove);

  const toggleApproveSign = () => setOpenApproveSign(!openApproveSign);

  const toggleReject = () => setOpenReject(!openReject);

  const toggleApproveSignSuccess = () => setOpenApproveSignSuccess(!openApproveSignSuccess);

  const toggleRejectSuccess = () => setOpenRejectSuccess(!openRejectSuccess);

  const toggleSchedule = () => setOpenSchedule(!openSchedule);

  const toggleScheduleSuccess = () => setOpenScheduleSuccess(!openScheduleSuccess);

  return [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openDetail, toggleDetail },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openSchedule, toggleSchedule },
    { openScheduleSuccess, toggleScheduleSuccess },
    { openRenew, toggleRenew },
    { openRenewSuccess, toggleRenewSuccess },
    { openHistory, toggleHistory },
  ];
};

export const useModalsRecruitmentCandidates = () => {
  const [openFilter, setOpenFilter] = useState<boolean>(false);
  const [openDetail, setOpenDetail] = useState<boolean>(false);
  const [openUpdate, setOpenUpdate] = useState<boolean>(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState<boolean>(false);
  const [openUpdateCandidates, setOpenUpdateCandidates] = useState<boolean>(false);
  const [openUpdateCandidatesSuccess, setOpenUpdateCandidatesSuccess] = useState<boolean>(false);

  const toggleUpdateCandidatesSuccess = () => setOpenUpdateCandidatesSuccess(!openUpdateCandidatesSuccess);
  const toggleUpdateCandidates = () => setOpenUpdateCandidates(!openUpdateCandidates);
  const toggleFilter = () => setOpenFilter(!openFilter);
  const toggleDetail = () => setOpenDetail(!openDetail);
  const toggleUpdate = () => setOpenUpdate(!openUpdate);
  const toggleUpdateSuccess = () => setOpenUpdateSuccess(!openUpdateSuccess);

  return [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openUpdateCandidates, toggleUpdateCandidates },
    { openUpdateCandidatesSuccess, toggleUpdateCandidatesSuccess },
  ];
};
