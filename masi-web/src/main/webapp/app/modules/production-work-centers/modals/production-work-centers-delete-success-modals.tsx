import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionWorkCentersDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionWorkCentersDeleteSuccessModals = (props: IProductionWorkCentersDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xoá cụm máy sản xuất thành công'
    >
      <Typography level={4}>Bạn đã xoá cụm máy sản xuất thành công</Typography>
    </Modal>
  );
};

export default ProductionWorkCentersDeleteSuccessModals;
