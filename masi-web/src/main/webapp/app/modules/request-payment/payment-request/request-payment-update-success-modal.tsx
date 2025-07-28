import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRequestPaymentUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const RequestPaymentUpdateSuccessModals = (
  props: IRequestPaymentUpdateSuccessModals,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-payment-update-success"
      cancel={false}
      titleHeader="Cập nhập đề nghị thanh toán thành công"
    >
      <Typography level={4}>
        Bạn đã cập nhập đề nghị thanh toán thành công
      </Typography>
    </Modal>
  );
};

export default RequestPaymentUpdateSuccessModals;
