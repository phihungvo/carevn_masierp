import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformSettingsUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UniformSettingsUpdateSuccessModals = (props: IUniformSettingsUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-uniform-settings-success"
      cancel={false}
      titleHeade='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật đồng phục thành công</Typography>
    </Modal>
  );
};

export default UniformSettingsUpdateSuccessModals;
