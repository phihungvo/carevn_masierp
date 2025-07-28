import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IWarehouseTypeUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const WarehouseTypeUpdateSuccessModals = (props: IWarehouseTypeUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-warehouse-type-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật loại kho thành công</Typography>
    </Modal>
  );
};

export default WarehouseTypeUpdateSuccessModals;
