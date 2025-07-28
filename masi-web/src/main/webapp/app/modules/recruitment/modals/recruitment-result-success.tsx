import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IRecruitmentResultSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RecruitmentResultSuccessModals = (props: IRecruitmentResultSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật kết quả phỏng vấn'
    >
      <Typography level={4}>Bạn đã cập nhật kết quả phỏng vấn thành công</Typography>
    </Modal>
  );
};

export default RecruitmentResultSuccessModals;
