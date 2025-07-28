import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ICustomerUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const CustomerTransferSuccessModals = (props: ICustomerUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      ancel={false}
      titleHeader='Chuyển giao nhân viên thành công'
    >
      <Typography level={4}>Bạn đã chuyển giao nhân viên thành công</Typography>
    </Modal>
  );
};

export default CustomerTransferSuccessModals;
