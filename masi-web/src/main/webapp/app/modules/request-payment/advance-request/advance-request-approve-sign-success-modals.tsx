import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAdvanceRequestApproveSignSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AdvanceRequestApproveSignSuccessModals = (
  props: IAdvanceRequestApproveSignSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-advance-repayment-sign-success"
      cancel={false}
      titleHeader="Xét duyệt thành công"
    >
      <Typography level={4}>
        Bạn đã xét duyệt đề nghị tạm ứng thành công
      </Typography>
    </Modal>
  );
};

export default AdvanceRequestApproveSignSuccessModals;
