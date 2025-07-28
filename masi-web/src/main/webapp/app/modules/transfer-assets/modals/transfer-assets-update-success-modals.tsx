import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface ITransferAssetsUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const TransferAssetsUpdateSuccessModals = (props: ITransferAssetsUpdateSuccessModals) => {
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
      titleHeader="Cập nhật thành công"
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã cập nhật điều chuyển thành công</Typography>
    </Modal>
  );
};

export default TransferAssetsUpdateSuccessModals;
