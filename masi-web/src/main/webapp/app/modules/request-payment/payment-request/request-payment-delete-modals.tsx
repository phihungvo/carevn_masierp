import React from 'react';

import Modal from 'app/components/modal/modal';
import useRecruitment from 'app/hooks/use-recruitment';
import { Typography } from 'app/components/typography/typography';
import usePaymentRequest from 'app/hooks/use-payment-request';

const { useDeletePaymentRequest } = usePaymentRequest;

interface IRequestPaymentDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
}

const RequestPaymentDeleteModals = (
  props: IRequestPaymentDeleteModalsProps,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const { mutate, isPending } = useDeletePaymentRequest(toggle, toggleSuccess);

  const onOk = () => {
    mutate(selectedRecord);
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-voucher-request-payment"
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader="Xóa đề nghị thanh toán"
    >
      <Typography level={4}>Bạn muốn xóa đề nghị thanh toán này ?</Typography>
    </Modal>
  );
};

export default RequestPaymentDeleteModals;
