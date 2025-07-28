import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformOrdersUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformOrdersUpdateSuccessModals = (props: IUniformOrdersUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật đồng phục thành công</Typography>
    </Modal>
  );
};

export default UniformOrdersUpdateSuccessModals;
