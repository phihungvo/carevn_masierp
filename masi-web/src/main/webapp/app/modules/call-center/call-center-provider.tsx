import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { ICallCenterFilterParams } from 'app/shared/model/call-center.model';
import React, { createContext } from 'react';

export interface ICallCenterContext {
  filter: ICallCenterFilterParams;
  setFilter: React.Dispatch<React.SetStateAction<ICallCenterFilterParams>>;
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
  isOpenComplete: boolean;
  toggleComplete: () => void;
  isOpenCompleteSuccess: boolean;
  toggleCompleteSuccess: () => void;
  isOpenCreateSuccess: boolean;
  toggleCreateSuccess: () => void;
  isOpenUpdateSuccess: boolean;
  toggleUpdateSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;

  isOpenClose: boolean;
  toggleClose: () => void;
  isOpenCloseSuccess: boolean;
  toggleCloseSuccess: () => void;
}

export const CallCenterContext = createContext<ICallCenterContext>({
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
  isOpenComplete: false,
  toggleComplete: () => {},
  isOpenCompleteSuccess: false,
  toggleCompleteSuccess: () => {},
  isOpenCreateSuccess: false,
  toggleCreateSuccess: () => {},
  isOpenUpdateSuccess: false,
  toggleUpdateSuccess: () => {},
  selectedRecord: '',
  setSelectedRecord: () => {},

  isOpenClose: false,
  toggleClose: () => {},
  isOpenCloseSuccess: false,
  toggleCloseSuccess: () => {},
});

interface IComplainProviderProps {
  children: React.ReactNode;
}

const CallCenterProvider = ({ children }: IComplainProviderProps) => {
  const [filter, setFilter] = React.useState<ICallCenterFilterParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });
  const [isOpenFilter, setIsOpenFilter] = React.useState(false);
  const [attachment, setAttachment] = React.useState({ isOpen: false });
  const [isOpenConfirmDelete, setIsOpenConfirmDelete] = React.useState(false);
  const [isOpenDeleteSuccess, setIsOpenDeleteSuccess] = React.useState(false);
  const [isOpenApprove, setIsOpenApprove] = React.useState(false);
  const [isOpenApproveSuccess, setIsOpenApproveSuccess] = React.useState(false);
  const [isOpenComplete, setIsOpenComplete] = React.useState(false);
  const [isOpenCompleteSuccess, setIsOpenCompleteSuccess] =
    React.useState(false);
  const [isOpenCreateSuccess, setIsOpenCreateSuccess] = React.useState(false);
  const [isOpenUpdateSuccess, setIsOpenUpdateSuccess] = React.useState(false);
  const [selectedRecord, setSelectedRecord] = React.useState('');
  const [isOpenClose, setIsOpenClose] = React.useState(false);
  const [isOpenCloseSuccess, setIsOpenCloseSuccess] = React.useState(false);

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

  const toggleComplete = () => {
    setIsOpenComplete(!isOpenComplete);
  };

  const toggleCompleteSuccess = () => {
    setIsOpenCompleteSuccess(!isOpenCompleteSuccess);
  };

  const toggleCreateSuccess = () => {
    setIsOpenCreateSuccess(!isOpenCreateSuccess);
  };

  const toggleUpdateSuccess = () => {
    setIsOpenUpdateSuccess(!isOpenUpdateSuccess);
  };

  const toggleClose = () => {
    setIsOpenClose(!isOpenClose);
  };

  const toggleCloseSuccess = () => {
    setIsOpenCloseSuccess(!isOpenCompleteSuccess);
  };

  return (
    <CallCenterContext.Provider
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
        isOpenComplete,
        toggleComplete,
        isOpenCompleteSuccess,
        toggleCompleteSuccess,
        isOpenCreateSuccess,
        toggleCreateSuccess,
        isOpenUpdateSuccess,
        toggleUpdateSuccess,
        selectedRecord,
        setSelectedRecord,
        isOpenClose,
        toggleClose,
        isOpenCloseSuccess,
        toggleCloseSuccess,
      }}
    >
      {children}
    </CallCenterContext.Provider>
  );
};

export default CallCenterProvider;
