import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface ILeaveRequestNoticesModal {
  isOpen: boolean;
  toggle: () => void;
  textNotices?: string;
}

export const LeaveRequestNoticesModal = (props: ILeaveRequestNoticesModal) => {
  const { isOpen, toggle, textNotices } = props;

  return (
    <Modal
      isOpen={isOpen}
      cancel={false}
      toggle={toggle}
      titleHeader='Thông báo'
    >
      <Typography level={4}>Đơn nghỉ phép này {textNotices}</Typography>
    </Modal>
  );
};
