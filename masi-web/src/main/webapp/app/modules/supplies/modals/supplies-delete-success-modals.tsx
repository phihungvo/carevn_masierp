import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ISuppliesDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const SuppliesDeleteSuccessModals = (props: ISuppliesDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-supplies-success" cancel={false} titleHeader="Xóa hoá đơn thành công">
      <Typography level={4}>Bạn đã xóa mã VT - CCDC thành công</Typography>
    </Modal>
  );
};

export default SuppliesDeleteSuccessModals;
