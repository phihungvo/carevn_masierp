import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import usePaymentRequest from 'app/hooks/use-payment-request';

const { useCancelPaymentRequest } = usePaymentRequest;

interface IRefundRequestCancelModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const RefundRequestCancelModals = (props: IRefundRequestCancelModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useCancelPaymentRequest();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        toggle();
        toggleSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-of-advance-cancel"
      okText="Xác nhận"
      onOk={onOk}
      titleHeader="Hủy hoàn tạm ứng"
    >
      <Typography level={4}>Bạn muốn hủy hoàn tạm ứng này ?</Typography>
    </Modal>
  );
};

export default RefundRequestCancelModals;
