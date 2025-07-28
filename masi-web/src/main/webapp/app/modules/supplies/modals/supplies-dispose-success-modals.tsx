import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ISuppliesDisposeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const SuppliesDisposeSuccessModals = (props: ISuppliesDisposeSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Vô hiệu thành công">
      <Typography level={4}>Bạn đã vô hiệu VT - CCDC thành công</Typography>
    </Modal>
  );
};

export default SuppliesDisposeSuccessModals;
