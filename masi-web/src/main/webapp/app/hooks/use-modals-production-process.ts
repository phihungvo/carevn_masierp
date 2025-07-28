import { useState } from 'react';

export const useModalsProductionProcess = () => {
  const [isOpenModalCreate, setIsOpenModalCreate] = useState(false);
  const [isOpenModalCreateSuccess, setIsOpenModalCreateSuccess] = useState(false);
  const [isOpenModalUpdate, setIsOpenModalUpdate] = useState(false);
  const [isOpenModalUpdateSuccess, setIsOpenModalUpdateSuccess] = useState(false);
  const [isOpenModalDelete, setIsOpenModalDelete] = useState(false);
  const [isOpenModalStart, setIsOpenModalStart] = useState(false);
  const [isOpenModalStop, setIsOpenModalStop] = useState(false);
  const [isOpenModalComplete, setIsOpenModalComplete] = useState(false);
  const [isOpenModalFilter, setIsOpenModalFilter] = useState(false);
  const [isOpenNoticesUpdate, setIsOpenNoticesUpdate] = useState<boolean>(false);

  const toggleNoticesUpdate = () => setIsOpenNoticesUpdate(!isOpenNoticesUpdate);

  // TOGGLE MODAL CREATE PRODUCTION PROCESS
  const toggleModalCreate = () => {
    setIsOpenModalCreate(prev => !prev);
  };

  // TOGGLE MODAL CREATE PRODUCTION PROCESS SUCCESS
  const toggleModalCreateSuccess = () => {
    setIsOpenModalCreateSuccess(prev => !prev);
  };

  // TOGGLE MODAL UPDATE PRODUCTION PROCESS
  const toggleModalUpdate = () => {
    setIsOpenModalUpdate(prev => !prev);
  };

  // TOGGLE MODAL UPDATE PRODUCTION PROCESS SUCCESS
  const toggleModalUpdateSuccess = () => {
    setIsOpenModalUpdateSuccess(prev => !prev);
  };

  // TOGGLE MODAL DELETE PRODUCTION PROCESS
  const toggleModalDelete = () => {
    setIsOpenModalDelete(prev => !prev);
  };

  // TOGGLE MODAL START PRODUCTION PROCESS
  const toggleModalStart = () => {
    setIsOpenModalStart(prev => !prev);
  };

  // TOGGLE MODAL STOP PRODUCTION PROCESS
  const toggleModalStop = () => {
    setIsOpenModalStop(prev => !prev);
  };

  // TOGGLE MODAL COMPLETE PRODUCTION PROCESS
  const toggleModalComplete = () => {
    setIsOpenModalComplete(prev => !prev);
  };

  // TOGGLE MODAL FILTER PRODUCTION PROCESS
  const toggleModalFilter = () => {
    setIsOpenModalFilter(prev => !prev);
  };

  return [
    { isOpenModalCreate, toggleModalCreate },
    { isOpenModalCreateSuccess, toggleModalCreateSuccess },
    { isOpenModalUpdate, toggleModalUpdate },
    { isOpenModalUpdateSuccess, toggleModalUpdateSuccess },
    { isOpenModalDelete, toggleModalDelete },
    { isOpenModalStart, toggleModalStart },
    { isOpenModalStop, toggleModalStop },
    { isOpenModalComplete, toggleModalComplete },
    { isOpenModalFilter, toggleModalFilter },
    { isOpenNoticesUpdate, toggleNoticesUpdate },
  ];
};
