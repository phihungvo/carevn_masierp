import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ICustomerCreateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const CustomerCreateSuccessModals = (props: ICustomerCreateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới khách hàng thành công</Typography>
    </Modal>
  );
};

export default CustomerCreateSuccessModals;
