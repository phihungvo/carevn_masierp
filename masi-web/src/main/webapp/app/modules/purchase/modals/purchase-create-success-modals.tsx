import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseCreateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseCreateSuccessModals = (props: IPurchaseCreateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-order-success" cancel={false}>
      <Typography level={3}>Tạo mới thành công</Typography>
      <Typography level={4}>Bạn đã tạo mới đề nghị thu mua thành công</Typography>
    </Modal>
  );
};

export default PurchaseCreateSuccessModals;
