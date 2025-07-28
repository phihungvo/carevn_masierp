import { useState } from 'react';

export const useModalsTimesheet = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [isOpenUpdateTimeCheck, setIsOpenUpdateTimeCheck] = useState<boolean>(false);
  const [isUpdateTimeCheckSuccess, setIsUpdateTimeCheckSuccess] = useState<boolean>(false);
  const [isOpenModalExport, setIsOpenModalExport] = useState<boolean>(false);
  const [openAccept, setOpenAccept] = useState(false);
  const [openReject, setOpenReject] = useState(false);

  // TOGGLE MODAL CHECKING TIME SHEET
  const toggle = () => setIsOpen(prev => !prev);

  // TOGGLE MODAL UPDATE TIME CHECK
  const toggleUpdateTimeCheck = () => {
    setIsOpenUpdateTimeCheck(prev => !prev);
  };

  // TOGGLE MODAL UPDATE TIME CHECK SUCCESS
  const toggleUpdateSuccess = () => setIsUpdateTimeCheckSuccess(prev => !prev);

  // TOGGLE MODAL EXPORT TIME SHEET
  const toggleModalExport = () => setIsOpenModalExport(prev => !prev);

  // TOGGLE MODAL ACCEPT BULK TIME SHEET APPROVAL
  const toggleAccept = () => {
    setOpenAccept(prev => !prev);
  };

  // TOGGLE MODAL REJECT BULK TIME SHEET APPROVAL
  const toggleReject = () => {
    setOpenReject(prev => !prev);
  };

  return [
    { isOpen, toggle },
    { isOpenUpdateTimeCheck, toggleUpdateTimeCheck },
    { isUpdateTimeCheckSuccess, toggleUpdateSuccess },
    { isOpenModalExport, toggleModalExport },
    { openAccept, toggleAccept },
    { openReject, toggleReject },
  ];
};
