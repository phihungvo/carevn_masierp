import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IWarehouseDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const WarehouseDeleteSuccessModals = (props: IWarehouseDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-warehouse-success"
      cancel={false}
      titleHeader='Xóa kho thành công'
    >
      <Typography level={4}>Bạn đã xóa kho thành công</Typography>
    </Modal>
  );
};

export default WarehouseDeleteSuccessModals;
