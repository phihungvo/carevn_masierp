import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseDeleteErrorModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseDeleteErrorModals = (props: IPurchaseDeleteErrorModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-delete-purchase-error" cancel={false}>
      <Typography level={3}>Xoá đơn thất bại </Typography>
      <Typography level={4}>Không thể xóa ĐNTM khi trạng thái là “Đã duyệt”.</Typography>
    </Modal>
  );
};

export default PurchaseDeleteErrorModals;
