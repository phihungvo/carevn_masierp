import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IFactoriesDisposeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const FactoriesDisposeSuccessModals = (props: IFactoriesDisposeSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Vô hiệu thành công">
      <Typography level={4}>Bạn đã vô hiệu nhà máy thành công</Typography>
    </Modal>
  );
};

export default FactoriesDisposeSuccessModals;
