import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRefundRequestDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RefundRequestDeleteSuccessModals = (
  props: IRefundRequestDeleteSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-voucher-request-of-advance-success"
      cancel={false}
      titleHeader="Xóa thành công"
    >
      <Typography level={4}>Bạn đã xóa hoàn tạm ứng thành công</Typography>
    </Modal>
  );
};

export default RefundRequestDeleteSuccessModals;
