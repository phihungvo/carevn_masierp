import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformOrdersStockSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformOrdersStockSuccessModals = (props: IUniformOrdersStockSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-cancel-order-success"
      cancel={false}
      titleHeader='Nhập kho đơn hàng thành công'
    >
      <Typography level={4}>Bạn đã nhập kho đơn hàng thành công</Typography>
    </Modal>
  );
};

export default UniformOrdersStockSuccessModals;
