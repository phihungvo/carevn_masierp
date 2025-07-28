import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOrdersCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OrdersCreateSuccessModals = (props: IOrdersCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới đơn hàng thành công</Typography>
    </Modal>
  );
};

export default OrdersCreateSuccessModals;
