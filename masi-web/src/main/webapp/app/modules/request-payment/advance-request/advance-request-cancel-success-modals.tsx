import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAdvanceRequestCancelSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AdvanceRequestCancelSuccessModals = (
  props: IAdvanceRequestCancelSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-advance-repayment-cancel-success"
      cancel={false}
      titleHeader="Hủy thành công"
    >
      <Typography level={4}>Bạn đã hủy đề nghị tạm ứng thành công</Typography>
    </Modal>
  );
};

export default AdvanceRequestCancelSuccessModals;
