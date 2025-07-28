import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ILeaveRegisterRejectSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LeaveRegisterRejectSuccessModals = (props: ILeaveRegisterRejectSuccessModalsProps) => {
  const { isOpen, toggle } = props;
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Từ chối xét duyệt'
    >
      <Typography level={4}>Bạn đã từ chối xét duyệt đăng ký nghỉ chế độ thành công</Typography>
    </Modal>
  );
};

export default LeaveRegisterRejectSuccessModals;
