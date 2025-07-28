import { useState } from 'react';

export const useModalsTimeSheetExplanation = () => {
  const [openFilter, setOpenFilter] = useState(false);
  const [openAcceptReq, setOpenAcceptReq] = useState(false);
  const [openAcceptSuccessReq, setOpenAcceptSuccessReq] = useState(false);
  const [openRejectReq, setOpenRejectReq] = useState(false);
  const [openRejectSuccessReq, setOpenRejectSuccessReq] = useState(false);
  const [openUpdateReq, setOpenUpdateReq] = useState(false);
  const [openUpdateSuccessReq, setOpenUpdateSuccessReq] = useState(false);
  const [openErrorUpdateReq, setOpenErrorUpdateReq] = useState(false);
  const [openCancelReq, setOpenCancelReq] = useState(false);
  const [openErrorCancelReq, setOpenErrorCancelReq] = useState(false);
  const [openSuccessCancelReq, setOpenSuccessCancelReq] = useState(false);
  const [openDeleteReq, setOpenDeleteReq] = useState(false);
  const [openErrorDeleteReq, setOpenErrorDeleteReq] = useState(false);
  const [openSuccessDeleteReq, setOpenSuccessDeleteReq] = useState(false);

  const toggleFilter = () => {
    setOpenFilter(prev => !prev);
  };

  const toggleAcceptReq = () => {
    setOpenAcceptReq(prev => !prev);
  };

  const toggleAcceptSuccessReq = () => {
    setOpenAcceptSuccessReq(prev => !prev);
  };

  const toggleRejectReq = () => {
    setOpenRejectReq(prev => !prev);
  };

  const toggleRejectSuccessReq = () => {
    setOpenRejectSuccessReq(prev => !prev);
  };

  const toggleUpdateReq = () => {
    setOpenUpdateReq(prev => !prev);
  };

  const toggleUpdateSuccessReq = () => {
    setOpenUpdateSuccessReq(prev => !prev);
  };

  const toggleErrorUpdateReq = () => {
    setOpenErrorUpdateReq(prev => !prev);
  };

  const toggleCancelReq = () => {
    setOpenCancelReq(prev => !prev);
  };

  const toggleErrorCancelReq = () => {
    setOpenErrorCancelReq(prev => !prev);
  };

  const toggleSuccessCancelReq = () => {
    setOpenSuccessCancelReq(prev => !prev);
  };

  const toggleDeleteReq = () => {
    setOpenDeleteReq(prev => !prev);
  };

  const toggleErrorDeleteReq = () => {
    setOpenErrorDeleteReq(prev => !prev);
  };

  const toggleSuccessDeleteReq = () => {
    setOpenSuccessDeleteReq(prev => !prev);
  };

  return [
    { openFilter, toggleFilter },
    { openAcceptReq, toggleAcceptReq },
    { openAcceptSuccessReq, toggleAcceptSuccessReq },
    { openRejectReq, toggleRejectReq },
    { openRejectSuccessReq, toggleRejectSuccessReq },
    { openUpdateReq, toggleUpdateReq },
    { openErrorUpdateReq, toggleErrorUpdateReq },
    { openUpdateSuccessReq, toggleUpdateSuccessReq },
    { openCancelReq, toggleCancelReq },
    { openErrorCancelReq, toggleErrorCancelReq },
    { openSuccessCancelReq, toggleSuccessCancelReq },
    { openDeleteReq, toggleDeleteReq },
    { openErrorDeleteReq, toggleErrorDeleteReq },
    { openSuccessDeleteReq, toggleSuccessDeleteReq },
  ];
};
