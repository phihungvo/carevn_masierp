import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ITransferAssetsDisposeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const TransferAssetsDisposeSuccessModals = (props: ITransferAssetsDisposeSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-dispose-process-success" cancel={false} titleHeader="Vô hiệu thành công">
      <Typography level={4}>Bạn đã huỷ điều chuyển thành công</Typography>
    </Modal>
  );
};

export default TransferAssetsDisposeSuccessModals;
