import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAdvanceRequestCreateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AdvanceRequestCreateSuccessModals = (
  props: IAdvanceRequestCreateSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-voucher-advance-repayment-success"
      cancel={false}
      titleHeader="Tạo đề nghị tạm ứng thành công"
    >
      <Typography level={4}>Bạn đã tạo đề nghị tạm ứng thành công</Typography>
    </Modal>
  );
};

export default AdvanceRequestCreateSuccessModals;
