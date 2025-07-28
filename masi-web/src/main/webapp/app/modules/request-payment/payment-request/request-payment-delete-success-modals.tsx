import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRequestPaymentDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RequestPaymentDeleteSuccessModals = (
  props: IRequestPaymentDeleteSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-voucher-request-payment-success"
      cancel={false}
      titleHeader="Xóa thành công"
    >
      <Typography level={4}>
        Bạn đã xóa đề nghị thanh toán thành công
      </Typography>
    </Modal>
  );
};

export default RequestPaymentDeleteSuccessModals;
