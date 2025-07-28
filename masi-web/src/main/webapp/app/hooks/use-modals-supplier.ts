import { useState } from 'react';

export const useModalsSupplier = () => {
  const [openDetail, setOpenDetail] = useState(false);
  const [openCreate, setOpenCreate] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [openDeleteSuccess, setOpenDeleteSuccess] = useState(false);
  const [openFilter, setOpenFilter] = useState(false);
  const [openItem, setOpenItem] = useState(false);
  const [openDispose, setOpenDispose] = useState(false);
  const [openDisposeSuccess, setOpenDisposeSuccess] = useState(false);
  const [openActivate, setOpenActivate] = useState(false);
  const [openActivateSuccess, setOpenActivateSuccess] = useState(false);

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

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

  const toggleItem = () => {
    setOpenItem(prev => !prev);
  };

  const toggleDispose = () => {
    setOpenDispose(prev => !prev);
  };

  const toggleDisposeSuccess = () => {
    setOpenDisposeSuccess(prev => !prev);
  };

  const toggleActivate = () => {
    setOpenActivate(prev => !prev);
  };

  const toggleActivateSuccess = () => {
    setOpenActivateSuccess(prev => !prev);
  };

  return [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openItem, toggleItem },
    { openDispose, toggleDispose },
    { openDisposeSuccess, toggleDisposeSuccess },
    { openActivate, toggleActivate },
    { openActivateSuccess, toggleActivateSuccess },
  ];
};
