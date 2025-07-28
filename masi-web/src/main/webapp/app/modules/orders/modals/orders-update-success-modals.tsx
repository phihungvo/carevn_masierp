import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOrdersUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OrdersUpdateSuccessModals = (props: IOrdersUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật đơn hàng thành công</Typography>
    </Modal>
  );
};

export default OrdersUpdateSuccessModals;
