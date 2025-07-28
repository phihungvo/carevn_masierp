import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionPackagesCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionPackagesCreateSuccessModals = (
  props: IProductionPackagesCreateSuccessModals,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      titleHeader="Tạo mới đóng gói sản phẩm thành công"
      cancel={false}
    >
      <Typography level={4}>
        Bạn đã tạo mới đóng gói sản phẩm thành công
      </Typography>
    </Modal>
  );
};

export default ProductionPackagesCreateSuccessModals;
