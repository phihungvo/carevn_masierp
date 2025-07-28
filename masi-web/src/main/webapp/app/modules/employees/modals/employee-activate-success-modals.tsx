import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IModalsActivateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeActivateSuccessModals = (props: IModalsActivateSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      titleHeader='Kích hoạt lại hồ sơ NV thành công'
    >
      <Typography level={4}>Bạn đã kích hoạt lại hồ sơ NV thành công</Typography>
    </Modal>
  );
};
