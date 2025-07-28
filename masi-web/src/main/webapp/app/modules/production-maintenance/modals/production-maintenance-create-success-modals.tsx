import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IProductionMaintenanceCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionMaintenanceCreateSuccessModals = (props: IProductionMaintenanceCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-maintenance-success"
      cancel={false}
      titleHeader='Tạo mới bảo trì sản phẩm thành công'
    >
      <Typography level={4}>Bạn đã tạo mới bảo trì sản phẩm thành công</Typography>
    </Modal>
  );
};

export default ProductionMaintenanceCreateSuccessModals;
