import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformExportsCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformExportsCreateSuccessModals = (props: IUniformExportsCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Hoàn ứng đồng phục thành công'
    >
      <Typography level={4}>Bạn đã hoàn ứng đồng phục thành công</Typography>
    </Modal>
  );
};

export default UniformExportsCreateSuccessModals;
