import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionPackagesUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionPackagesUpdateSuccessModals = (
  props: IProductionPackagesUpdateSuccessModals,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader="Cập nhật đóng gói sản phẩm"
    >
      <Typography level={4}>
        Bạn đã cập nhật đóng gói sản phẩm thành công
      </Typography>
    </Modal>
  );
};

export default ProductionPackagesUpdateSuccessModals;
