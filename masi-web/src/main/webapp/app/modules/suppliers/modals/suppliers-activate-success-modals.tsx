import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ISupplierActivateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const SupplierActivateSuccessModals = (props: ISupplierActivateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Kích hoạt thành công">
      <Typography level={4}>Bạn đã kích hoạt NCC thành công</Typography>
    </Modal>
  );
};

export default SupplierActivateSuccessModals;
