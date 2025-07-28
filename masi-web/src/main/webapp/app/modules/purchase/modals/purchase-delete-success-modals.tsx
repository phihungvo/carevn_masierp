import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseDeleteSuccessModals = (props: IPurchaseDeleteSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-order-success" cancel={false}>
      <Typography level={3}>Xoá đơn thành công</Typography>
      <Typography level={4}>Bạn đã xóa đơn đề nghị thành công</Typography>
    </Modal>
  );
};

export default PurchaseDeleteSuccessModals;
