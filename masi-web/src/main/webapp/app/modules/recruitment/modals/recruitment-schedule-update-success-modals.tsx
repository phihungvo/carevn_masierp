import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRecruitmentScheduleUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RecruitmentScheduleUpdateSuccessModals = (props: IRecruitmentScheduleUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật lịch phỏng vấn thành công'
    >
      <Typography level={4}>Bạn đã cập nhật lịch phỏng vấn thành công</Typography>
    </Modal>
  );
};

export default RecruitmentScheduleUpdateSuccessModals;
