import { useState } from 'react';

export const useModalsContracts = () => {
  const [openFilter, setOpenFilter] = useState<boolean>(false);
  const [openDetail, setOpenDetail] = useState<boolean>(false);
  const [openCreate, setOpenCreate] = useState<boolean>(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState<boolean>(false);
  const [openUpdate, setOpenUpdate] = useState<boolean>(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState<boolean>(false);
  const [openUpdateError, setOpenUpdateError] = useState<boolean>(false);
  const [openDelete, setOpenDelete] = useState<boolean>(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState<boolean>(false);
  const [openRestore, setOpenRestore] = useState<boolean>(false);
  const [openRestoreSuccess, setOpenRestoreSuccess] = useState<boolean>(false);
  const [openProposeApprove, setOpenProposeApprove] = useState<boolean>(false);
  const [openProposeApproveSuccess, setOpenProposeApproveSuccess] = useState<boolean>(false);
  const [openApprove, setOpenApprove] = useState<boolean>(false);
  const [openApproveSuccess, setOpenApproveSuccess] = useState<boolean>(false);
  const [openApproveLiquid, setOpenApproveLiquid] = useState<boolean>(false);
  const [openApproveLiquidSign, setOpenApproveLiquidSign] = useState<boolean>(false);
  const [openApproveLiquidSuccess, setOpenApproveLiquidSuccess] = useState<boolean>(false);
  const [openRejectLiquid, setOpenRejectLiquid] = useState<boolean>(false);
  const [openRejectLiquidSuccess, setOpenRejectLiquidSuccess] = useState<boolean>(false);
  const [openProposeLiquid, setOpenProposeLiquid] = useState<boolean>(false);
  const [openProposeLiquidSuccess, setOpenProposeLiquidSuccess] = useState<boolean>(false);
  const [openApproveSign, setOpenApproveSign] = useState<boolean>(false);
  const [openApproveSignSuccess, setOpenApproveSignSuccess] = useState<boolean>(false);
  const [openReject, setOpenReject] = useState<boolean>(false);
  const [openRejectSuccess, setOpenRejectSuccess] = useState<boolean>(false);
  const [openMaskFinished, setOpenMaskFinished] = useState<boolean>(false);
  const [openMaskFinishedSuccess, setOpenMaskFinishedSuccess] = useState<boolean>(false);

  const toggleMaskFinishedSuccess = () => setOpenMaskFinishedSuccess(!openMaskFinishedSuccess)
  const toggleMaskFinished = () => setOpenMaskFinished(!openMaskFinished)
  const toggleRejectSuccess = () => setOpenRejectSuccess(!openRejectSuccess)
  const toggleReject = () => setOpenReject(!openReject)
  const toggleApproveSignSuccess = () => setOpenApproveSignSuccess(!openApproveSignSuccess)
  const toggleApproveSign = () => setOpenApproveSign(!openApproveSign)

  const toggleFilter = () => {
    setOpenFilter(!openFilter);
  };

  const toggleDetail = () => {
    setOpenDetail(!openDetail);
  };

  const toggleCreate = () => {
    setOpenCreate(!openCreate);
  };

  const toggleCreateSuccess = () => {
    setOpenCreateSuccess(!openCreateSuccess);
  };

  const toggleUpdate = () => {
    setOpenUpdate(!openUpdate);
  };

  const toggleUpdateSuccess = () => {
    setOpenUpdateSuccess(!openUpdateSuccess);
  };

  const toggleUpdateError = () => {
    setOpenUpdateError(!openUpdateError);
  };

  const toggleDelete = () => {
    setOpenDelete(!openDelete);
  };

  const toggleDeleteSuccess = () => {
    setOpenDeleteSuccess(!openDeleteSuccess);
  };

  const toggleRestore = () => {
    setOpenRestore(!openRestore);
  };

  const toggleRestoreSuccess = () => {
    setOpenRestoreSuccess(!openRestoreSuccess);
  };

  const toggleProposeApprove = () => {
    setOpenProposeApprove(!openProposeApprove);
  };

  const toggleProposeApproveSuccess = () => {
    setOpenProposeApproveSuccess(!openProposeApproveSuccess);
  };

  const toggleApprove = () => {
    setOpenApprove(!openApprove);
  };

  const toggleApproveSuccess = () => {
    setOpenApproveSuccess(!openApproveSuccess);
  };

  const toggleApproveLiquid = () => {
    setOpenApproveLiquid(!openApproveLiquid);
  };

  const toggleApproveLiquidSign = () => {
    setOpenApproveLiquidSign(!openApproveLiquidSign);
  };

  const toggleApproveLiquidSuccess = () => {
    setOpenApproveLiquidSuccess(!openApproveLiquidSuccess);
  };

  const toggleRejectLiquid = () => {
    setOpenRejectLiquid(!openRejectLiquid);
  };

  const toggleRejectLiquidSuccess = () => {
    setOpenRejectLiquidSuccess(!openRejectLiquidSuccess);
  };

  const toggleProposeLiquid = () => {
    setOpenProposeLiquid(!openProposeLiquid);
  };

  const toggleProposeLiquidSuccess = () => {
    setOpenProposeLiquidSuccess(!openProposeLiquidSuccess);
  };

  return [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openUpdateError, toggleUpdateError },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openRestore, toggleRestore },
    { openRestoreSuccess, toggleRestoreSuccess },
    { openProposeApprove, toggleProposeApprove },
    { openProposeApproveSuccess, toggleProposeApproveSuccess },
    { openApprove, toggleApprove },
    { openApproveSuccess, toggleApproveSuccess },
    { openApproveLiquid, toggleApproveLiquid },
    { openApproveLiquidSign, toggleApproveLiquidSign },
    { openApproveLiquidSuccess, toggleApproveLiquidSuccess },
    { openRejectLiquid, toggleRejectLiquid },
    { openRejectLiquidSuccess, toggleRejectLiquidSuccess },
    { openProposeLiquid, toggleProposeLiquid },
    { openProposeLiquidSuccess, toggleProposeLiquidSuccess },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openMaskFinished, toggleMaskFinished },
    { openMaskFinishedSuccess, toggleMaskFinishedSuccess },
  ];
};

export const useModalsContractsDeleted = () => {
  const [openFilter, setOpenFilter] = useState<boolean>(false);

  const [openCreate, setOpenCreate] = useState<boolean>(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState<boolean>(false);

  const [openRestore, setOpenRestore] = useState<boolean>(false);
  const [openRestoreSuccess, setOpenRestoreSuccess] = useState<boolean>(false);

  const toggleFilter = () => {
    setOpenFilter(!openFilter);
  };

  const toggleCreate = () => {
    setOpenCreate(!openCreate);
  };

  const toggleCreateSuccess = () => {
    setOpenCreateSuccess(!openCreateSuccess);
  };

  const toggleRestore = () => {
    setOpenRestore(!openRestore);
  };

  const toggleRestoreSuccess = () => {
    setOpenRestoreSuccess(!openRestoreSuccess);
  };

  return [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openRestore, toggleRestore },
    { openRestoreSuccess, toggleRestoreSuccess },
  ];
};
