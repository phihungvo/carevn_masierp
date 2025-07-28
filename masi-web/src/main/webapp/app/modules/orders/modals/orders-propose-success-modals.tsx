import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOrdersProposeSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OrdersProposeSuccessModals = (props: IOrdersProposeSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Yêu cầu xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã yêu cầu xét duyệt thành công</Typography>
    </Modal>
  );
};

export default OrdersProposeSuccessModals;
