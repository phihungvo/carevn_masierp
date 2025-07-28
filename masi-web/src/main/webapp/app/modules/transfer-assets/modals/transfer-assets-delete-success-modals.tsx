import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface ITransferAssetsDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const TransferAssetsDeleteSuccessModals = (props: ITransferAssetsDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-transfer-assets-success" cancel={false} titleHeader="Xóa điều chuyển TS thành công">
      <Typography level={4}>Bạn đã xóa điều chuyển TS thành công</Typography>
    </Modal>
  );
};

export default TransferAssetsDeleteSuccessModals;
