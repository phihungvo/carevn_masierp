import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ILeaveRegisterDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LeaveRegisterDeleteSuccessModals = (props: ILeaveRegisterDeleteSuccessModalsProps) => {
  const { isOpen, toggle } = props;
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Cập nhật đăng ký nghỉ chế độ'
    >
      <Typography level={4}>Bạn đã xoá đăng ký nghỉ chế độ thành công</Typography>
    </Modal>
  );
};

export default LeaveRegisterDeleteSuccessModals;
