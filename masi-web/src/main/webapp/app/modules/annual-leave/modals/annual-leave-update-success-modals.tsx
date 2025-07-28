import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IAnnualLeaveUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AnnualLeaveUpdateSuccessModals = (props: IAnnualLeaveUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật phép năm thành công</Typography>
    </Modal>
  );
};

export default AnnualLeaveUpdateSuccessModals;
