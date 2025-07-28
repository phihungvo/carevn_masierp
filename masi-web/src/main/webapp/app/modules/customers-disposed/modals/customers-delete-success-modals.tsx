import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ICustomerDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const CustomerDeleteSuccessModals = (props: ICustomerDeleteSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-process-success"
      cancel={false}
      titleHeader='Xóa khách hàng thành công'
    >
      <Typography level={4}>Bạn đã xóa thành công khách hàng này</Typography>
    </Modal>
  );
};

export default CustomerDeleteSuccessModals;
