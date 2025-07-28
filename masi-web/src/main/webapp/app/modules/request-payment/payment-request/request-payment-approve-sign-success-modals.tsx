import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRequestPaymentApproveSignSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RequestPaymentApproveSignSuccessModals = (
  props: IRequestPaymentApproveSignSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-payment-approval-sign-success"
      cancel={false}
      titleHeader="Xét duyệt thành công"
    >
      <Typography level={4}>
        Bạn đã duyệt đề nghị thanh toán thành công
      </Typography>
    </Modal>
  );
};

export default RequestPaymentApproveSignSuccessModals;
