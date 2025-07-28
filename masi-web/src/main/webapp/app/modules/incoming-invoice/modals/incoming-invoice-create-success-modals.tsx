import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IIncomingInvoiceCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const IncomingInvoiceCreateSuccessModals = (props: IIncomingInvoiceCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới hoá đơn thành công</Typography>
    </Modal>
  );
};

export default IncomingInvoiceCreateSuccessModals;
