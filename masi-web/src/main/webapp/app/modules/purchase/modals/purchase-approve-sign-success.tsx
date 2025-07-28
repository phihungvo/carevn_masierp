import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseApproveSuccessModals = (props: IPurchaseApproveSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-order-success" cancel={false}>
      <Typography level={3}>Xét duyệt thành công</Typography>
      <Typography level={4}>Bạn đã xét duyệt đề nghị thu mua thành công</Typography>
    </Modal>
  );
};

export default PurchaseApproveSuccessModals;
