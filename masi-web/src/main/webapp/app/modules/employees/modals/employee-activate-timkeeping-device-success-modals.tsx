import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IEmployeeActivateTimkeepingDeviceSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeActivateTimkeepingDeviceSuccessModals = (props: IEmployeeActivateTimkeepingDeviceSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      titleHeader='Kích hoạt máy chấm công thành công'
    >
      <Typography level={4}>Bạn đã kích hoạt máy chấm công thành công</Typography>
    </Modal>
  );
};
