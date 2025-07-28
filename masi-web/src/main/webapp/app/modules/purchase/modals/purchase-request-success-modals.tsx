import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseRequestSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseRequestSuccessModals = (props: IPurchaseRequestSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-update-order-success" cancel={false}>
      <Typography level={3}>Gửi yêu cầu thành công</Typography>
      <Typography level={4}>Bạn đã gửi yêu cầu xét duyệt thành công</Typography>
    </Modal>
  );
};

export default PurchaseRequestSuccessModals;
