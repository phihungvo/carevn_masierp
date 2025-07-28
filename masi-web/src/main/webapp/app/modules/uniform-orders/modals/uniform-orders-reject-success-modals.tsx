import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformOrdersRejectSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const UniformOrdersRejectSuccessModals = (props: IUniformOrdersRejectSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Từ chối xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã từ chối xét duyệt đơn hàng thành công</Typography>
    </Modal>
  );
};

export default UniformOrdersRejectSuccessModals;
