import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IModalsUploadSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeUploadSuccessModals = (props: IModalsUploadSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      titleHeader='Tải lên hồ sơ đính kèm thành công'
    >
      <Typography level={4}>Bạn đã tải lên hồ sơ đính kèm thành công</Typography>
    </Modal>
  );
};
