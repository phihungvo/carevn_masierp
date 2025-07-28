import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformSettingsCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformSettingsCreateSuccessModals = (props: IUniformSettingsCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-uniform-settings-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới đồng phục thành công</Typography>
    </Modal>
  );
};

export default UniformSettingsCreateSuccessModals;
