import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformOrdersDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformOrdersDeleteSuccessModals = (props: IUniformOrdersDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xóa đơn hàng thành công'
    >
      <Typography level={4}>Bạn đã xóa đơn hàng thành công</Typography>
    </Modal>
  );
};

export default UniformOrdersDeleteSuccessModals;
