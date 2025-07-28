import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IFactoriesActivateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const FactoriesActivateSuccessModals = (props: IFactoriesActivateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Kích hoạt thành công">
      <Typography level={4}>Bạn đã kích hoạt nhà máy thành công</Typography>
    </Modal>
  );
};

export default FactoriesActivateSuccessModals;
