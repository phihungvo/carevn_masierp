import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRequestPaymentCancelSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RequestPaymentCancelSuccessModals = (
  props: IRequestPaymentCancelSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-payment-cancel-success"
      cancel={false}
      titleHeader="Hủy thành công"
    >
      <Typography level={4}>
        Bạn đã hủy đề nghị thanh toán thành công
      </Typography>
    </Modal>
  );
};

export default RequestPaymentCancelSuccessModals;
