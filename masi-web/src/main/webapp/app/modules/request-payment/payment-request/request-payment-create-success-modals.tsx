import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRequestPaymentCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const RequestPaymentCreateSuccessModals = (
  props: IRequestPaymentCreateSuccessModals,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-payment-create-success"
      cancel={false}
      titleHeader="Tạo đề nghị thanh toán thành công"
    >
      <Typography level={4}>
        Bạn đã tạo đề nghị thanh toán thành công
      </Typography>
    </Modal>
  );
};

export default RequestPaymentCreateSuccessModals;
