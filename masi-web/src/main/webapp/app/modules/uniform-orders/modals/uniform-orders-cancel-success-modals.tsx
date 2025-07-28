import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformOrdersCancelSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformOrdersCancelSuccessModals = (props: IUniformOrdersCancelSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-cancel-order-success"
      cancel={false}
      titleHeader='Huỷ đơn hàng thành công'
    >
      <Typography level={4}>Bạn đã huỷ đơn hàng thành công</Typography>
    </Modal>
  );
};

export default UniformOrdersCancelSuccessModals;
