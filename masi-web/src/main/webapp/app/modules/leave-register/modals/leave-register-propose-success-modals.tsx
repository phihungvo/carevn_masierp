import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ILeaveRegisterProposeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LeaveRegisterProposeSuccessModals = (props: ILeaveRegisterProposeSuccessModalsProps) => {
  const { isOpen, toggle } = props;
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Yêu cầu xét duyệt'
    >
      <Typography level={4}>Bạn đã yêu cầu xét duyệt đăng ký nghỉ chế độ thành công</Typography>
    </Modal>
  );
};

export default LeaveRegisterProposeSuccessModals;
