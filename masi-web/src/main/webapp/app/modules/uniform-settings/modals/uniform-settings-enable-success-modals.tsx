import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IUniformSettingsEnableSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const UniformSettingsEnableSuccessModals = (props: IUniformSettingsEnableSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-Enable-uniform-settings-success"
      cancel={false}
      titleHeader='Kích hoạt đồng phục thành công'
    >
      <Typography level={4}>Bạn đã kích hoạt đồng phục thành công</Typography>
    </Modal>
  );
};

export default UniformSettingsEnableSuccessModals;
