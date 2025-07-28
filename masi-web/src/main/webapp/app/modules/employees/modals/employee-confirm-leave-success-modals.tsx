import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IConfirmLeaveSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeConfirmLeaveSuccessModals = (props: IConfirmLeaveSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      titleHeader='Xác nhận đã nghỉ thành công'
    >
      <Typography level={4}>Bạn đã xác nhận nhân viên đã nghỉ thành công</Typography>
    </Modal>
  );
};
