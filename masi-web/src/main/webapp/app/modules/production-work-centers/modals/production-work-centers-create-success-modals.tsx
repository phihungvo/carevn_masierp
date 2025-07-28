import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionWorkCentersCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionWorkCentersCreateSuccessModals = (props: IProductionWorkCentersCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Tạo mới cụm máy sản xuất thành công'
    >
      <Typography level={4}>Bạn đã tạo mới cụm máy sản xuất thành công</Typography>
    </Modal>
  );
};

export default ProductionWorkCentersCreateSuccessModals;
