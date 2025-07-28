import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRefundRequestCreateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RefundRequestCreateSuccessModals = (
  props: IRefundRequestCreateSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-voucher-request-of-advance-success"
      cancel={false}
      titleHeader="Tạo hoàn tạm ứng thành công"
    >
      <Typography level={4}>Bạn đã tạo hoàn tạm ứng thành công</Typography>
    </Modal>
  );
};

export default RefundRequestCreateSuccessModals;
