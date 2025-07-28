import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAdvanceRequestUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AdvanceRequestUpdateSuccessModals = (
  props: IAdvanceRequestUpdateSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-update-voucher-advance-repayment-success"
      cancel={false}
      titleHeader="Cập nhập đề nghị tạm ứng thành công"
    >
      <Typography level={4}>
        Bạn đã cập nhập đề nghị tạm ứng thành công
      </Typography>
    </Modal>
  );
};

export default AdvanceRequestUpdateSuccessModals;
