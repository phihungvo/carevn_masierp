import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ILeaveRegisterCreateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LeaveRegisterCreateSuccessModals = (props: ILeaveRegisterCreateSuccessModalsProps) => {
  const { isOpen, toggle } = props;
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Tạo đăng ký nghỉ chế độ'
    >
      <Typography level={4}>Bạn đã tạo đăng ký nghỉ chế độ thành công</Typography>
    </Modal>
  );
};

export default LeaveRegisterCreateSuccessModals;
