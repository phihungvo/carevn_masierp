import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IWarehouseUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const WarehouseUpdateSuccessModals = (props: IWarehouseUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-warehouse-success"
      cancel={false}
      titleHeader='Cập nhật kho thành công'
    >
      <Typography level={4}>Bạn đã cập nhật kho thành công</Typography>
    </Modal>
  );
};

export default WarehouseUpdateSuccessModals;
