import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPurchaseRequestErrorModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PurchaseRequestErrorModals = (props: IPurchaseRequestErrorModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-delete-purchase-error" cancel={false}>
      <Typography level={3}>Yêu cầu xét duyệt</Typography>
      <Typography level={4}>Không thể yêu cầu xét duyệt khi trạng thái đang là: Đã duyệt hoặc Từ chối</Typography>
    </Modal>
  );
};

export default PurchaseRequestErrorModals;
