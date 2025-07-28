import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionMaintenanceDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionMaintenanceDeleteSuccessModals = (props: IProductionMaintenanceDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xóa quản lý bảo trì sản phẩm thành công'
    >
      <Typography level={4}>Bạn đã xoá quản lý bảo trì sản phẩm thành công</Typography>
    </Modal>
  );
};

export default ProductionMaintenanceDeleteSuccessModals;
