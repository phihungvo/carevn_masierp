import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ISupplierDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const SupplierDeleteSuccessModals = (props: ISupplierDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-delete-supplier-success" cancel={false} titleHeader="Xóa thành công">
      <Typography level={4}>Bạn đã xóa nhà cung cấp thành công</Typography>
    </Modal>
  );
};

export default SupplierDeleteSuccessModals;
