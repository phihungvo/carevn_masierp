import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ITransferAssetsActivateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const TransferAssetsActivateSuccessModals = (props: ITransferAssetsActivateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Hoàn thành điều chuyển thành công">
      <Typography level={4}>Bạn hoàn thành điều chuyển thành công</Typography>
    </Modal>
  );
};

export default TransferAssetsActivateSuccessModals;
