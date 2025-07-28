import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { ICallCenterFilterParams } from 'app/shared/model/call-center.model';
import React, { createContext } from 'react';

export interface ICompanyContext {
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
  isOpenConfirmInactive: boolean;
  toggleConfirmInactive: () => void;
  isOpenInactiveSuccess: boolean;
  toggleInactiveSuccess: () => void;
  isOpenConfirmActive: boolean;
  toggleConfirmActive: () => void;
  isOpenActiveSuccess: boolean;
  toggleActiveSuccess: () => void;
  isOpenCreateSuccess: boolean;
  toggleCreateSuccess: () => void;
  isOpenUpdateSuccess: boolean;
  toggleUpdateSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

export const CompanyContext = createContext<ICompanyContext>({
  filter: { page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE },
  setFilter: () => {},
  isOpenFilter: false,
  toggleFilter: () => {},
  attachment: { isOpen: false },
  setAttachment: () => {},
  isOpenConfirmInactive: false,
  toggleConfirmInactive: () => {},
  isOpenInactiveSuccess: false,
  toggleInactiveSuccess: () => {},
  isOpenConfirmActive: false,
  toggleConfirmActive: () => {},
  isOpenActiveSuccess: false,
  toggleActiveSuccess: () => {},
  isOpenCreateSuccess: false,
  toggleCreateSuccess: () => {},
  isOpenUpdateSuccess: false,
  toggleUpdateSuccess: () => {},
  selectedRecord: '',
  setSelectedRecord: () => {},
});

interface IComplainProviderProps {
  children: React.ReactNode;
}

const CompanyProvider = ({ children }: IComplainProviderProps) => {
  const [filter, setFilter] = React.useState<ICallCenterFilterParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });
  const [isOpenFilter, setIsOpenFilter] = React.useState(false);
  const [attachment, setAttachment] = React.useState({ isOpen: false });
  const [isOpenConfirmInactive, setIsOpenConfirmInactive] =
    React.useState(false);
  const [isOpenInactiveSuccess, setIsOpenInactiveSuccess] =
    React.useState(false);
  const [isOpenConfirmActive, setIsOpenConfirmActive] = React.useState(false);
  const [isOpenActiveSuccess, setIsOpenActiveSuccess] = React.useState(false);
  const [isOpenCreateSuccess, setIsOpenCreateSuccess] = React.useState(false);
  const [isOpenUpdateSuccess, setIsOpenUpdateSuccess] = React.useState(false);
  const [selectedRecord, setSelectedRecord] = React.useState('');

  const toggleFilter = () => {
    setIsOpenFilter(!isOpenFilter);
  };

  const toggleConfirmInactive = () => {
    setIsOpenConfirmInactive(!isOpenConfirmInactive);
  };

  const toggleInactiveSuccess = () => {
    setIsOpenInactiveSuccess(!isOpenInactiveSuccess);
  };

  const toggleConfirmActive = () => {
    setIsOpenConfirmActive(!isOpenConfirmActive);
  };

  const toggleActiveSuccess = () => {
    setIsOpenActiveSuccess(!isOpenActiveSuccess);
  };

  const toggleCreateSuccess = () => {
    setIsOpenCreateSuccess(!isOpenCreateSuccess);
  };

  const toggleUpdateSuccess = () => {
    setIsOpenUpdateSuccess(!isOpenUpdateSuccess);
  };

  return (
    <CompanyContext.Provider
      value={{
        filter,
        setFilter,
        isOpenFilter,
        toggleFilter,
        attachment,
        setAttachment,
        isOpenConfirmInactive,
        toggleConfirmInactive,
        isOpenInactiveSuccess,
        toggleInactiveSuccess,
        isOpenConfirmActive,
        toggleConfirmActive,
        isOpenActiveSuccess,
        toggleActiveSuccess,
        isOpenCreateSuccess,
        toggleCreateSuccess,
        isOpenUpdateSuccess,
        toggleUpdateSuccess,
        selectedRecord,
        setSelectedRecord,
      }}
    >
      {children}
    </CompanyContext.Provider>
  );
};

export default CompanyProvider;
