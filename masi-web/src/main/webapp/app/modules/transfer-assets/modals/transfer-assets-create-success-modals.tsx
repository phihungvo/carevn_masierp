import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface ITransferAssetsCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const TransferAssetsCreateSuccessModals = (props: ITransferAssetsCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  const onOk = () => {
    toggle();
    navigate(PATH.TRANSFER_ASSETS);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-transfer-assets-success"
      cancel={false}
      titleHeader="Tạo mới thành công"
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã tạo mới điều chuyển thành công</Typography>
    </Modal>
  );
};

export default TransferAssetsCreateSuccessModals;
