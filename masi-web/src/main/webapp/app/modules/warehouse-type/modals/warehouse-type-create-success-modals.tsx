import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IWarehouseTypeCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const WarehouseTypeCreateSuccessModals = (props: IWarehouseTypeCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-warehouse-type-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới loại kho thành công</Typography>
    </Modal>
  );
};

export default WarehouseTypeCreateSuccessModals;
