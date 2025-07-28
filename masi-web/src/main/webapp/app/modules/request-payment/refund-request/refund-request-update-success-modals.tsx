import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRefundRequestUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RefundRequestUpdateSuccessModals = (
  props: IRefundRequestUpdateSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-of-advance-update-success"
      cancel={false}
      titleHeader="Cập nhập hoàn tạm ứng thành công"
    >
      <Typography level={4}>Bạn đã cập nhập hoàn tạm ứng thành công</Typography>
    </Modal>
  );
};

export default RefundRequestUpdateSuccessModals;
