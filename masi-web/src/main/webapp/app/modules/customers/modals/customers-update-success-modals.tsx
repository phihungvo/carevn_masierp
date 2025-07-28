import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ICustomerUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const CustomerUpdateSuccessModals = (props: ICustomerUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật thông tin khách hàng thành công</Typography>
    </Modal>
  );
};

export default CustomerUpdateSuccessModals;
