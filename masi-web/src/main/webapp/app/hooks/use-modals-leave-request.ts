import { useState } from 'react';

// MODALS HOOKS FOR LEAVE REQUEST
export const useModalsLeaveRequest = () => {
  const [openLeaveRequest, setOpenLeaveRequest] = useState(false);
  const [openRequestSuccess, setOpenRequestSuccess] = useState(false);
  const [openCancelLeaveRequest, setOpenCancelLeaveRequest] = useState(false);
  const [openCancelError, setOpenCancelError] = useState(false);
  const [openDeleteRequest, setOpenDeleteRequest] = useState(false);
  const [openFilterRequest, setOpenFilterRequest] = useState(false);
  const [openNoteRequest, setOpenNoteRequest] = useState(false);
  const [openRejectRequest, setOpenRejectRequest] = useState(false);
  const [openAcceptRequest, setOpenAcceptRequest] = useState(false);
  const [openDetailRequest, setOpenDetailRequest] = useState(false);
  const [openNotices, setOpenNotices] = useState(false);

  const toggleNotices = () => {
    setOpenNotices(prev => !prev);
  };

  const toggleDetailRequest = () => {
    setOpenDetailRequest(prev => !prev);
  };

  // TOGGLE MODAL LEAVE REQUEST REQUEST
  const toggleLeaveRequest = () => {
    setOpenLeaveRequest(prev => !prev);
  };

  // TOGGLE MODAL REQUEST SUCCESS
  const toggleRequestSuccess = () => {
    setOpenRequestSuccess(prev => !prev);
  };

  // TOGGLE MODAL CANCEL LEAVE REQUEST
  const toggleCancelLeaveRequest = () => {
    setOpenCancelLeaveRequest(prev => !prev);
  };

  // TOGGLE MODAL CANCEL ERROR
  const toggleCancelError = () => {
    setOpenCancelError(prev => !prev);
  };

  // TOGGLE MODAL DELETE REQUEST
  const toggleDeleteRequest = () => {
    setOpenDeleteRequest(prev => !prev);
  };

  // TOGGLE MODAL FILTER REQUEST
  const toggleFilterRequest = () => {
    setOpenFilterRequest(prev => !prev);
  };

  // TOGGLE MODAL NOTE REQUEST
  const toggleNoteRequest = () => {
    setOpenNoteRequest(prev => !prev);
  };

  // TOGGLE MODAL REJECT REQUEST
  const toggleAcceptRequest = () => {
    setOpenAcceptRequest(prev => !prev);
  };

  // TOGGLE MODAL ACCEPT REQUEST
  const toggleRejectRequest = () => {
    setOpenRejectRequest(prev => !prev);
  };

  return [
    { openLeaveRequest, toggleLeaveRequest },
    { openRequestSuccess, toggleRequestSuccess },
    { openCancelLeaveRequest, toggleCancelLeaveRequest },
    { openCancelError, toggleCancelError },
    { openDeleteRequest, toggleDeleteRequest },
    { openFilterRequest, toggleFilterRequest },
    { openNoteRequest, toggleNoteRequest },
    { openRejectRequest, toggleRejectRequest },
    { openAcceptRequest, toggleAcceptRequest },
    { openDetailRequest, toggleDetailRequest },
    { openNotices, toggleNotices },
  ];
};
