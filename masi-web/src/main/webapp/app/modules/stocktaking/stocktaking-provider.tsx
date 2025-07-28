import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IStocktakingFilterParams } from 'app/shared/model/stocktaking.model';
import React, { createContext } from 'react';

export interface IStocktakingContext {
  filter: IStocktakingFilterParams;
  setFilter: React.Dispatch<React.SetStateAction<IStocktakingFilterParams>>;
  isOpenFilter: boolean;
  toggleFilter: () => void;
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
}

export const StocktakingContext = createContext<IStocktakingContext>({
  filter: { page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE },
  setFilter: () => {},
  isOpenFilter: false,
  toggleFilter: () => {},
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
  selectedRecord: '',
  setSelectedRecord: () => {},
});

interface IStocktakingProviderProps {
  children: React.ReactNode;
}

const StocktakingProvider = ({ children }: IStocktakingProviderProps) => {
  const [filter, setFilter] = React.useState<IStocktakingFilterParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });
  const [isOpenFilter, setIsOpenFilter] = React.useState(false);
  const [isOpenConfirmDelete, setIsOpenConfirmDelete] = React.useState(false);
  const [isOpenDeleteSuccess, setIsOpenDeleteSuccess] = React.useState(false);
  const [isOpenApprove, setIsOpenApprove] = React.useState(false);
  const [isOpenApproveSuccess, setIsOpenApproveSuccess] = React.useState(false);
  const [isOpenReject, setIsOpenReject] = React.useState(false);
  const [isOpenRejectSuccess, setIsOpenRejectSuccess] = React.useState(false);
  const [isOpenReview, setIsOpenReview] = React.useState(false);
  const [isOpenReviewSuccess, setIsOpenReviewSuccess] = React.useState(false);
  const [isOpenCreateSuccess, setIsOpenCreateSuccess] = React.useState(false);
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

  return (
    <StocktakingContext.Provider
      value={{
        filter,
        setFilter,
        isOpenFilter,
        toggleFilter,
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
      }}
    >
      {children}
    </StocktakingContext.Provider>
  );
};

export default StocktakingProvider;
