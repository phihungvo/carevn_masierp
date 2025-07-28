import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IWarehouseTypeDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const WarehouseTypeDeleteSuccessModals = (props: IWarehouseTypeDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-warehouse-type-success"
      cancel={false}
      titleHeader='Xóa loại kho thành công'
    >
      <Typography level={4}>Bạn đã xóa loại kho thành công</Typography>
    </Modal>
  );
};

export default WarehouseTypeDeleteSuccessModals;
