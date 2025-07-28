import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRefundRequestCancelSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RefundRequestCancelSuccessModals = (
  props: IRefundRequestCancelSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-of-advance-cancel-success"
      cancel={false}
      titleHeader="Hủy thành công"
    >
      <Typography level={4}>Bạn đã hủy hoàn tạm ứng thành công</Typography>
    </Modal>
  );
};

export default RefundRequestCancelSuccessModals;
