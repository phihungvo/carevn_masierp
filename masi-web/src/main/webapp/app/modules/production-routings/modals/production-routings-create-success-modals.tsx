import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionRoutingsCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionRoutingsCreateSuccessModals = (props: IProductionRoutingsCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-routings-success"
      cancel={false}
      titleHeader='Tạo mới định tuyến kho sản xuất thành công'
    >
      <Typography level={4}>Bạn đã tạo định tuyến kho sản xuất thành công</Typography>
    </Modal>
  );
};

export default ProductionRoutingsCreateSuccessModals;
