import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IDownloadSuccessfulModalsProps {
  isOpen: boolean;
  toggleSuccess: () => void;
  title: string;
}

export const DownloadSuccessfulModals = ({ isOpen, toggleSuccess, title }: IDownloadSuccessfulModalsProps) => {
  return (
    <Modal isOpen={isOpen} cancel={false} toggle={toggleSuccess} titleHeader="Tải xuống thành công">
      <Typography level={4}>Bạn đã tải {title} thành công</Typography>
    </Modal>
  );
};
