import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ICustomerDisposeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const CustomerDisposeSuccessModals = (props: ICustomerDisposeSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-dispose-process-success"
      cancel={false}
      titleHeader='Vô hiệu thành công'
    >
      <Typography level={4}>Bạn đã vô hiệu khách hàng thành công</Typography>
    </Modal>
  );
};

export default CustomerDisposeSuccessModals;
