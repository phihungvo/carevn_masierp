import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOrdersDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OrdersDeleteSuccessModals = (props: IOrdersDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xóa đơn đặt hàng thành công'
    >
      <Typography level={4}>Bạn đã xóa đơn đặt hàng thành công</Typography>
    </Modal>
  );
};

export default OrdersDeleteSuccessModals;
