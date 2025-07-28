import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListDeleteModals {
  isOpen: boolean;
  toggle: () => void;
  onOk?: () => void;
  onCancel?: () => void;
}

const PriceListDeleteMaterialModals = (props: IPriceListDeleteModals) => {
  const { isOpen, toggle, onOk, onCancel } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      okText="Xác nhận"
      onOk={onOk}
      onCancel={onCancel}
      titleHeader='Xóa loại hàng'
    >
      <Typography level={4}>Bạn muốn xoá loại hàng này?</Typography>
    </Modal>
  );
};

export default PriceListDeleteMaterialModals;
