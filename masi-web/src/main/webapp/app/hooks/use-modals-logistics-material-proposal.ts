import { useState } from 'react';

export const useModalsLogisticsMaterialProposal = () => {
    const [openFilter, setOpenFilter] = useState<boolean>(false);
    const [openCreate, setOpenCreate] = useState<boolean>(false);
    const [openCreateSuccess, setOpenCreateSuccess] = useState<boolean>(false);
    const [openUpdate, setOpenUpdate] = useState<boolean>(false);
    const [openUpdateSuccess, setOpenUpdateSuccess] = useState<boolean>(false);
    const [openDetail, setOpenDetail] = useState<boolean>(false);
    const [openPropose, setOpenPropose] = useState<boolean>(false);
    const [openProposeSuccess, setOpenProposeSuccess] = useState<boolean>(false);
    const [openApprove, setOpenApprove] = useState<boolean>(false);
    const [openApproveSign, setOpenApproveSign] = useState<boolean>(false);
    const [openApproveSignSuccess, setOpenApproveSignSuccess] = useState<boolean>(false);
    const [openReject, setOpenReject] = useState<boolean>(false);
    const [openRejectSuccess, setOpenRejectSuccess] = useState<boolean>(false);
    const [openDelete, setOpenDelete] = useState<boolean>(false);
    const [openDeleteSuccess, setOpenDeleteSuccess] = useState<boolean>(false);
    const [openCancel, setOpenCancel] = useState<boolean>(false);
    const [openCancelSuccess, setOpenCancelSuccess] = useState<boolean>(false);

    const toggleCancelSuccess = () => setOpenCancelSuccess(!openCancelSuccess);
    const toggleCancel = () => setOpenCancel(!openCancel);
    const toggleDeleteSuccess = () => setOpenDeleteSuccess(!openDeleteSuccess);
    const toggleDelete = () => setOpenDelete(!openDelete);
    const toggleRejectSuccess = () => setOpenRejectSuccess(!openRejectSuccess);
    const toggleReject = () => setOpenReject(!openReject);
    const toggleApproveSignSuccess = () => setOpenApproveSignSuccess(!openApproveSignSuccess);
    const toggleApproveSign = () => setOpenApproveSign(!openApproveSign);
    const toggleApprove = () => setOpenApprove(!openApprove);
    const toggleProposeSuccess = () => setOpenProposeSuccess(!openProposeSuccess);
    const togglePropose = () => setOpenPropose(!openPropose);
    const toggleDetail = () => setOpenDetail(!openDetail);
    const toggleUpdateSuccess = () => setOpenUpdateSuccess(!openUpdateSuccess);
    const toggleUpdate = () => setOpenUpdate(!openUpdate);
    const toggleCreateSuccess = () => setOpenCreateSuccess(!openCreateSuccess);
    const toggleCreate = () => setOpenCreate(!openCreate);
    const toggleFilter = () => setOpenFilter(!openFilter);

    return [
        { openFilter, toggleFilter },
        { openCreate, toggleCreate },
        { openCreateSuccess, toggleCreateSuccess },
        { openUpdate, toggleUpdate },
        { openUpdateSuccess, toggleUpdateSuccess },
        { openDetail, toggleDetail },
        { openPropose, togglePropose },
        { openProposeSuccess, toggleProposeSuccess },
        { openApprove, toggleApprove },
        { openApproveSign, toggleApproveSign },
        { openApproveSignSuccess, toggleApproveSignSuccess },
        { openReject, toggleReject },
        { openRejectSuccess, toggleRejectSuccess },
        { openDelete, toggleDelete },
        { openDeleteSuccess, toggleDeleteSuccess },
        { openCancel, toggleCancel },
        { openCancelSuccess, toggleCancelSuccess },
    ];
};

export const useModalsVoucherRequestPaymentChangeLog = () => {
    const [openDetail, setOpenDetail] = useState(false);

    const toggleDetail = () => {
        setOpenDetail(!openDetail);
    };

    return [{ openDetail, toggleDetail }];
};
