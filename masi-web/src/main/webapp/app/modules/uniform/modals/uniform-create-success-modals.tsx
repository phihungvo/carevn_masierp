import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformCreateSuccessModals = (props: IUniformCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xuất đồng phục thành công'
    >
      <Typography level={4}>Bạn đã xuất đồng phục thành công</Typography>
    </Modal>
  );
};

export default UniformCreateSuccessModals;
