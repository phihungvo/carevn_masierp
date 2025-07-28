import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IRecruitmentDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RecruitmentDeleteSuccessModals = (props: IRecruitmentDeleteSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-recruitment-success"
      cancel={false}
      titleHeader='Xoá đơn thành công'
    >
      <Typography level={4}>Bạn đã xóa yêu cầu tuyển dụng thành công</Typography>
    </Modal>
  );
};

export default RecruitmentDeleteSuccessModals;
