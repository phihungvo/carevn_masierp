import { useState } from 'react';

export const useModalsTimesheetBulkApproval = () => {
  const [openAccept, setOpenAccept] = useState(false);
  const [openReject, setOpenReject] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);

  const toggleAccept = () => {
    setOpenAccept(prev => !prev);
  };

  // TOGGLE MODAL REJECT BULK TIME SHEET APPROVAL
  const toggleReject = () => {
    setOpenReject(prev => !prev);
  };

  const toggleDetail = () => {
    setOpenDetail(prev => !prev);
  };

  return [
    { openAccept, toggleAccept },
    { openReject, toggleReject },
    { openDetail, toggleDetail },
  ];
};
