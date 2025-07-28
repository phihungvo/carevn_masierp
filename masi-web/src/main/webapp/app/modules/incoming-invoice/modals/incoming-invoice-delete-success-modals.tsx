import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IIncomingInvoiceDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const IncomingInvoiceDeleteSuccessModals = (props: IIncomingInvoiceDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xóa hoá đơn thành công'
    >
      <Typography level={4}>Bạn đã xóa hoá đơn thành công</Typography>
    </Modal>
  );
};

export default IncomingInvoiceDeleteSuccessModals;
