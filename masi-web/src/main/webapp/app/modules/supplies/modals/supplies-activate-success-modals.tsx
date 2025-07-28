import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ISuppliesActivateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const SuppliesActivateSuccessModals = (props: ISuppliesActivateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Kích hoạt thành công">
      <Typography level={4}>Bạn đã kích hoạt VT - CCDC thành công</Typography>
    </Modal>
  );
};

export default SuppliesActivateSuccessModals;
