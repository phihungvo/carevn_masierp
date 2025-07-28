import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseRejectSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseRejectSuccessModals = (props: IPurchaseRejectSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-order-success" cancel={false}>
      <Typography level={3}>Từ chối xét duyệt thành công</Typography>
      <Typography level={4}>Bạn đã từ chối xét duyệt đề nghị thu mua thành công</Typography>
    </Modal>
  );
};

export default PurchaseRejectSuccessModals;
