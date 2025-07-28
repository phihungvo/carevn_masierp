import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionRoutingsUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionRoutingsUpdateSuccessModals = (props: IProductionRoutingsUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-routings-success"
      cancel={false}
      titleHeader='Cập nhật cụm máy sản xuất thành công'
    >
      <Typography level={4}>Bạn đã cập nhật cụm máy sản xuất thành công</Typography>
    </Modal>
  );
};

export default ProductionRoutingsUpdateSuccessModals;
