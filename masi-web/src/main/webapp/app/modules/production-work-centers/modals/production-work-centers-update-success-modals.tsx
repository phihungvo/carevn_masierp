import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionWorkCentersUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionWorkCentersUpdateSuccessModals = (props: IProductionWorkCentersUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật cụm máy sản xuất thành công'
    >
      <Typography level={4}>Bạn đã cập nhật cụm máy sản xuất thành công</Typography>
    </Modal>
  );
};

export default ProductionWorkCentersUpdateSuccessModals;
