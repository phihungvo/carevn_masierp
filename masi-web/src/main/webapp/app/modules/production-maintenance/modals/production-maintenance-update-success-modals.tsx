import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionMaintenanceUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionMaintenanceUpdateSuccessModals = (props: IProductionMaintenanceUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-maintenance-success"
      cancel={false}
      titleHeader='Cập nhật bảo trì sản phẩm'
    >
      <Typography level={4}>Bạn đã cập nhật bảo trì sản phẩm thành công</Typography>
    </Modal>
  );
};

export default ProductionMaintenanceUpdateSuccessModals;
