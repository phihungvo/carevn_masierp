import { useState } from 'react';

export const useModalProductionCommand = () => {
  const [openCreateOrders, setOpenCreateOrders] = useState(false);
  const [openCreateStandard, setOpenCreateStandard] = useState(false);
  const [openUpdateOrders, setOpenUpdateOrders] = useState(false);
  const [openUpdateStandard, setOpenUpdateStandard] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openCancel, setOpenCancel] = useState(false);
  const [openFilter, setOpenFilter] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openCancelSuccess, setOpenCancelSuccess] = useState(false);
  const [openConfirmDelete, setOpenConfirmDelete] = useState(false);
  const [openDrawerOrders, setOpenDrawerOrders] = useState(false);
  const [openDrawerStandard, setOpenDrawerStandard] = useState(false);
  const [openPurchaseProposalOrders, setOpenPurchaseProposalOrders] = useState(false);
  const [openPurchaseProposalStandard, setOpenPurchaseProposalStandard] = useState(false);
  const [openCheckProposalFormOrders, setOpenCheckProposalFormOrders] = useState(false);
  const [openCheckProposalFormStandard, setOpenCheckProposalFormStandard] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openCheckThePowder, setOpenCheckThePowder] = useState(false);

  const toggleCheckThePowder = () => setOpenCheckThePowder(!openCheckThePowder);
  const toggleDetail = () => setOpenDetail(!openDetail);
  const toggleCheckProposalFormStandard = () => setOpenCheckProposalFormStandard(!openCheckProposalFormStandard);
  const toggleCheckProposalFormOrders = () => setOpenCheckProposalFormOrders(!openCheckProposalFormOrders);
  const togglePurchaseProposalOrders = () => setOpenPurchaseProposalOrders(!openPurchaseProposalOrders);
  const togglePurchaseProposalStandard = () => setOpenPurchaseProposalStandard(!openPurchaseProposalStandard);

  const toggleCreateOrders = () => setOpenCreateOrders(!openCreateOrders);
  const toggleCreateStandard = () => setOpenCreateStandard(!openCreateStandard);
  const toggleDrawerStandard = () => setOpenDrawerStandard(!openDrawerStandard);
  const toggleDrawerOrders = () => setOpenDrawerOrders(!openDrawerOrders);
  const toggleUpdateOrders = () => setOpenUpdateOrders(!openUpdateOrders);
  const toggleUpdateStandard = () => setOpenUpdateStandard(!openUpdateStandard);


  const toggleDelete = () => {
    setOpenDelete(prev => !prev);
  };

  const toggleCancel = () => {
    setOpenCancel(prev => !prev);
  };

  const toggleOpenFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleCreateSuccess = () => {
    setOpenCreateSuccess(prev => !prev);
  };

  const toggleUpdateSuccess = () => {
    setOpenUpdateSuccess(prev => !prev);
  };

  const toggleCancelSuccess = () => {
    setOpenCancelSuccess(prev => !prev);
  };

  const toggleConfirmDelete = () => {
    setOpenConfirmDelete(prev => !prev);
  };


  return [
    { openCreateOrders, toggleCreateOrders },
    { openCreateStandard, toggleCreateStandard },
    { openUpdateOrders, toggleUpdateOrders },
    { openUpdateStandard, toggleUpdateStandard },
    { openDelete, toggleDelete },
    { openCancel, toggleCancel },
    { openFilter, toggleOpenFilter },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openCancelSuccess, toggleCancelSuccess },
    { openConfirmDelete, toggleConfirmDelete },
    { openDrawerOrders, toggleDrawerOrders },
    { openDrawerStandard, toggleDrawerStandard },
    { openPurchaseProposalOrders, togglePurchaseProposalOrders },
    { openPurchaseProposalStandard, togglePurchaseProposalStandard },
    { openCheckProposalFormOrders, toggleCheckProposalFormOrders },
    { openCheckProposalFormStandard, toggleCheckProposalFormStandard },
    { openDetail, toggleDetail },
    { openCheckThePowder, toggleCheckThePowder },
  ];
};
