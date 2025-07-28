import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformOrdersApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const UniformOrdersApproveSuccessModals = (props: IUniformOrdersApproveSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã xét duyệt đơn hàng thành công</Typography>
    </Modal>
  );
};

export default UniformOrdersApproveSuccessModals;
