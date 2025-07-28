import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IUniformSettingsDisableSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const UniformSettingsDisableSuccessModals = (props: IUniformSettingsDisableSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-Disable-uniform-settings-success"
      cancel={false}
      titleHeader='Vô hiệu hóa đồng phục thành công'
    >
      <Typography level={4}>Bạn đã vô hiệu hóa đồng phục thành công</Typography>
    </Modal>
  );
};

export default UniformSettingsDisableSuccessModals;
