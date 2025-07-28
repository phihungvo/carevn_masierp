import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IRecruitmentUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RecruitmentUpdateSuccessModals = (props: IRecruitmentUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật yêu cầu tuyển dụng thành công</Typography>
    </Modal>
  );
};

export default RecruitmentUpdateSuccessModals;
