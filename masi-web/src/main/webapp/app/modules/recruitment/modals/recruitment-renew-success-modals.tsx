import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRecruitmentRenewSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RecruitmentRenewSuccessModals = (props: IRecruitmentRenewSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-recruiment-renew-success"
      cancel={false}
      titleHeader='Gia hạn thành công'
    >
      <Typography level={4}>Bạn đã gia hạn yêu cầu tuyển dụng thành công</Typography>
    </Modal>
  );
};

export default RecruitmentRenewSuccessModals;
