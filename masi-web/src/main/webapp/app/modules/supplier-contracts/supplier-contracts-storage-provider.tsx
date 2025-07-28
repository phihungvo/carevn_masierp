import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { ISupplierContractFilterParams } from 'app/shared/model/supplier-contract.model';
import React, { createContext } from 'react';

export interface ISupplierContractsContext {
  filter: ISupplierContractFilterParams;
  setFilter: React.Dispatch<
    React.SetStateAction<ISupplierContractFilterParams>
  >;
  isOpenFilter: boolean;
  toggleFilter: () => void;
  attachment: {
    isOpen: boolean;
    id?: 'invoiceId' | 'suppliesRequestId';
  };
  setAttachment: React.Dispatch<
    React.SetStateAction<{
      isOpen: boolean;
      id: 'invoiceId' | 'suppliesRequestId';
    }>
  >;
  isOpenConfirmDelete: boolean;
  toggleConfirmDelete: () => void;
  isOpenDeleteSuccess: boolean;
  toggleDeleteSuccess: () => void;
  isOpenApprove: boolean;
  toggleApprove: () => void;
  isOpenApproveSuccess: boolean;
  toggleApproveSuccess: () => void;
  isOpenReject: boolean;
  toggleReject: () => void;
  isOpenRejectSuccess: boolean;
  toggleRejectSuccess: () => void;
  isOpenReview: boolean;
  toggleReview: () => void;
  isOpenReviewSuccess: boolean;
  toggleReviewSuccess: () => void;
  isOpenCreateSuccess: boolean;
  toggleCreateSuccess: () => void;
  isOpenUpdateSuccess: boolean;
  toggleUpdateSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;

  isOpenChangeStatus: boolean;
  toggleChangeStatus: (status: string) => void;
  toggleChangeStatusSuccess: () => void;

  status: string;
}

export const SupplierContractsContext =
  createContext<ISupplierContractsContext>({
    filter: { page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE },
    setFilter: () => {},
    isOpenFilter: false,
    toggleFilter: () => {},
    attachment: { isOpen: false },
    setAttachment: () => {},
    isOpenConfirmDelete: false,
    toggleConfirmDelete: () => {},
    isOpenDeleteSuccess: false,
    toggleDeleteSuccess: () => {},
    isOpenApprove: false,
    toggleApprove: () => {},
    isOpenApproveSuccess: false,
    toggleApproveSuccess: () => {},
    isOpenReject: false,
    toggleReject: () => {},
    isOpenRejectSuccess: false,
    toggleRejectSuccess: () => {},
    isOpenReview: false,
    toggleReview: () => {},
    isOpenReviewSuccess: false,
    toggleReviewSuccess: () => {},
    isOpenCreateSuccess: false,
    toggleCreateSuccess: () => {},
    isOpenUpdateSuccess: false,
    toggleUpdateSuccess: () => {},
    isOpenChangeStatus: false,
    toggleChangeStatus: (status) => {},
    toggleChangeStatusSuccess: () => {},
    status: '',
    selectedRecord: '',
    setSelectedRecord: () => {},
  });

interface ISupplierContractsProviderProps {
  children: React.ReactNode;
}

const SupplierContractsProvider = ({
  children,
}: ISupplierContractsProviderProps) => {
  const [filter, setFilter] = React.useState<ISupplierContractFilterParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });
  const [isOpenFilter, setIsOpenFilter] = React.useState(false);
  const [attachment, setAttachment] = React.useState({
    isOpen: false,
  });
  const [isOpenConfirmDelete, setIsOpenConfirmDelete] = React.useState(false);
  const [isOpenDeleteSuccess, setIsOpenDeleteSuccess] = React.useState(false);
  const [isOpenApprove, setIsOpenApprove] = React.useState(false);
  const [isOpenApproveSuccess, setIsOpenApproveSuccess] = React.useState(false);
  const [isOpenReject, setIsOpenReject] = React.useState(false);
  const [isOpenRejectSuccess, setIsOpenRejectSuccess] = React.useState(false);
  const [isOpenReview, setIsOpenReview] = React.useState(false);
  const [isOpenReviewSuccess, setIsOpenReviewSuccess] = React.useState(false);
  const [isOpenCreateSuccess, setIsOpenCreateSuccess] = React.useState(false);

  const [status, setStatus] = React.useState('');
  const [isOpenChangeStatus, setIsOpenChangeStatus] = React.useState(false);
  const [isOpenChangeStatusSuccess, setIsOpenChangeStatusSuccess] = React.useState(false);

  const [isOpenUpdateSuccess, setIsOpenUpdateSuccess] = React.useState(false);
  const [selectedRecord, setSelectedRecord] = React.useState('');

  const toggleFilter = () => {
    setIsOpenFilter(!isOpenFilter);
  };

  const toggleConfirmDelete = () => {
    setIsOpenConfirmDelete(!isOpenConfirmDelete);
  };

  const toggleDeleteSuccess = () => {
    setIsOpenDeleteSuccess(!isOpenDeleteSuccess);
  };

  const toggleApprove = () => {
    setIsOpenApprove(!isOpenApprove);
  };

  const toggleApproveSuccess = () => {
    setIsOpenApproveSuccess(!isOpenApproveSuccess);
  };

  const toggleReject = () => {
    setIsOpenReject(!isOpenReject);
  };

  const toggleRejectSuccess = () => {
    setIsOpenRejectSuccess(!isOpenRejectSuccess);
  };

  const toggleReview = () => {
    setIsOpenReview(!isOpenReview);
  };

  const toggleReviewSuccess = () => {
    setIsOpenReviewSuccess(!isOpenReviewSuccess);
  };

  const toggleCreateSuccess = () => {
    setIsOpenCreateSuccess(!isOpenCreateSuccess);
  };

  const toggleUpdateSuccess = () => {
    setIsOpenUpdateSuccess(!isOpenUpdateSuccess);
  };


  const toggleChangeStatus = (statusVal: string) => {
    setIsOpenChangeStatus(!isOpenChangeStatus);
    setStatus(statusVal);
  };

  const toggleChangeStatusSuccess = () => {
    setIsOpenChangeStatusSuccess(!isOpenChangeStatusSuccess);
  };

  return (
    <SupplierContractsContext.Provider
      value={{
        filter,
        setFilter,
        isOpenFilter,
        toggleFilter,
        attachment,
        setAttachment,
        isOpenConfirmDelete,
        toggleConfirmDelete,
        isOpenDeleteSuccess,
        toggleDeleteSuccess,
        isOpenApprove,
        toggleApprove,
        isOpenApproveSuccess,
        toggleApproveSuccess,
        isOpenReject,
        toggleReject,
        isOpenRejectSuccess,
        toggleRejectSuccess,
        isOpenReview,
        toggleReview,
        isOpenReviewSuccess,
        toggleReviewSuccess,
        isOpenCreateSuccess,
        toggleCreateSuccess,
        isOpenUpdateSuccess,
        toggleUpdateSuccess,
        selectedRecord,
        setSelectedRecord,
        isOpenChangeStatus,
        toggleChangeStatus,
        toggleChangeStatusSuccess,
        status
      }}
    >
      {children}
    </SupplierContractsContext.Provider>
  );
};

export default SupplierContractsProvider;
