import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseUpdateSuccessModals = (props: IPurchaseUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-update-order-success" cancel={false}>
      <Typography level={3}>Cập nhật thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật đề nghị thu mua thành công</Typography>
    </Modal>
  );
};

export default PurchaseUpdateSuccessModals;
