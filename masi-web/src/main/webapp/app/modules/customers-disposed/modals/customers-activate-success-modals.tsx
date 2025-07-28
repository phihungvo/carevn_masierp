import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ICustomerActivateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const CustomerActivateSuccessModals = (props: ICustomerActivateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-activate-process-success"
      cancel={false}
      titleHeader='Kích hoạt thành công'
    >
      <Typography level={4}>Bạn đã kích hoạt lại khách hàng thành công</Typography>
    </Modal>
  );
};

export default CustomerActivateSuccessModals;
