import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAdvanceRequestDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AdvanceRequestDeleteSuccessModals = (
  props: IAdvanceRequestDeleteSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-voucher-advance-repayment-success"
      cancel={false}
      titleHeader="Xóa thành công"
    >
      <Typography level={4}>Bạn đã xóa đề nghị tạm ứng thành công</Typography>
    </Modal>
  );
};

export default AdvanceRequestDeleteSuccessModals;
