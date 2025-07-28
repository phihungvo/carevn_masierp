import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAdvanceRequestApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AdvanceRequestApproveSuccessModals = (
  props: IAdvanceRequestApproveSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-advance-repayment-sign-success"
      cancel={false}
      titleHeader="Trình duyệt thành công"
    >
      <Typography level={4}>
        Bạn đã trình duyệt đề nghị tạm ứng thành công
      </Typography>
    </Modal>
  );
};

export default AdvanceRequestApproveSuccessModals;
