import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IModalsDeactivateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeDeactivateSuccessModals = (props: IModalsDeactivateSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      titleHeader='Vô hiệu nhân viên thành công'
    >
      <Typography level={4}>Bạn đã vô hiệu nhân viên thành công</Typography>
    </Modal>
  );
};
