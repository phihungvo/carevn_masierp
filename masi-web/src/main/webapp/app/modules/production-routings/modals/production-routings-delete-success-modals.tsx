import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionRoutingsDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionRoutingsDeleteSuccessModals = (props: IProductionRoutingsDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xoá định tuyến kho sản xuất thành công'
    >
      <Typography level={4}>Bạn đã xoá định tuyến kho sản xuất thành công</Typography>
    </Modal>
  );
};

export default ProductionRoutingsDeleteSuccessModals;
