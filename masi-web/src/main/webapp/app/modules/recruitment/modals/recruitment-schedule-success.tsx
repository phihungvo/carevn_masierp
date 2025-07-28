import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IRecruitmentScheduleSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RecruitmentScheduleSuccessModals = (props: IRecruitmentScheduleSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Lên lịch phỏng vấn thành công'
    >
      <Typography level={4}>Bạn đã lên lịch phỏng vấn thành công</Typography>
    </Modal>
  );
};

export default RecruitmentScheduleSuccessModals;
