import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IIncomingInvoiceUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const IncomingInvoiceUpdateSuccessModals = (props: IIncomingInvoiceUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật hoá đơn thành công</Typography>
    </Modal>
  );
};

export default IncomingInvoiceUpdateSuccessModals;
