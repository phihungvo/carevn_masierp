import { useState } from 'react';

export const useBtnDownload = () => {
  const [openModalDownload, setOpenModalDownload] = useState(false);
  const [openDownloadSuccessful, setOpenDownloadSuccessful] = useState(false);

  const toggleModalDownload = () => setOpenModalDownload(prev => !prev);

  const toggleDownloadSuccessful = () => setOpenDownloadSuccessful(prev => !prev);

  return [
    { openModalDownload, toggleModalDownload },
    { openDownloadSuccessful, toggleDownloadSuccessful },
  ];
};
