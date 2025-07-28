import { useState } from 'react';

export const useModalsGroup = () => {
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);

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

  return [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDetail, toggleOpenDetail },
  ];
};

export const useModalsGroupUser = () => {
  const [openAccountSuccess, setOpenAccountSuccess] = useState(false);
  const [openAuthoritiesSuccess, setOpenAuthoritiesSuccess] = useState(false);

  const toggleAccountSuccess = () => {
    setOpenAccountSuccess(prev => !prev);
  };

  const toggleAuthoritiesSuccess = () => {
    setOpenAuthoritiesSuccess(prev => !prev);
  };

  return [
    { openAccountSuccess, toggleAccountSuccess },
    { openAuthoritiesSuccess, toggleAuthoritiesSuccess },
  ];
};
