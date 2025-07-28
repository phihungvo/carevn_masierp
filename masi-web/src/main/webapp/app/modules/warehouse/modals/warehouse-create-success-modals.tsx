import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IWarehouseCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const WarehouseCreateSuccessModals = (props: IWarehouseCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-warehouse-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới kho thành công</Typography>
    </Modal>
  );
};

export default WarehouseCreateSuccessModals;
