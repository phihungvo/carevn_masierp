import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOrdersCancelSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OrdersCancelSuccessModals = (props: IOrdersCancelSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-cancel-order-success"
      cancel={false}
      titleHeader='Huỷ đơn đặt hàng thành công'
    >
      <Typography level={4}>Bạn đã huỷ đơn đặt hàng thành công</Typography>
    </Modal>
  );
};

export default OrdersCancelSuccessModals;
