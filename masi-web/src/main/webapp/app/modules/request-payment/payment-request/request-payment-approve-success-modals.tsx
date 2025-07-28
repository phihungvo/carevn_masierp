import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRequestPaymentApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RequestPaymentApproveSuccessModals = (
  props: IRequestPaymentApproveSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-payment-approval-sign-success"
      cancel={false}
      titleHeader="Trình duyệt thành công"
    >
      <Typography level={4}>
        Bạn đã trình duyệt đề nghị thanh toán thành công
      </Typography>
    </Modal>
  );
};

export default RequestPaymentApproveSuccessModals;
