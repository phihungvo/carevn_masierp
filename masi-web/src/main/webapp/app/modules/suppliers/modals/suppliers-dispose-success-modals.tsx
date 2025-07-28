import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ISupplierDisposeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const SupplierDisposeSuccessModals = (props: ISupplierDisposeSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Vô hiệu thành công">
      <Typography level={4}>Bạn đã vô hiệu NCC thành công</Typography>
    </Modal>
  );
};

export default SupplierDisposeSuccessModals;
