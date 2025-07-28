import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseStatusSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseStatusSuccessModals = (props: IPurchaseStatusSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-update-order-success" cancel={false}>
      <Typography level={3}>Cập nhật thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật tình trạng giao hàng thành công</Typography>
    </Modal>
  );
};

export default PurchaseStatusSuccessModals;
