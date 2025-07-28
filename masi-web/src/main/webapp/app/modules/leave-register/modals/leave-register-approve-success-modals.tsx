import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ILeaveRegisterApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LeaveRegisterApproveSuccessModals = (props: ILeaveRegisterApproveSuccessModalsProps) => {
  const { isOpen, toggle } = props;
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Đồng ý xét duyệt'
    >
      <Typography level={4}>Bạn đã đồng ý xét duyệt đăng ký nghỉ chế độ thành công</Typography>
    </Modal>
  );
};

export default LeaveRegisterApproveSuccessModals;
