import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import IncomingInvoiceForm from '../components/incoming-invoices-form';

const { UPDATE_INCOMING_INVOICE } = MUTATION_KEY;

interface IIncomingInvoiceUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

const IncomingInvoiceUpdateModals = (props: IIncomingInvoiceUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const isCreating = useIsMutating({
    mutationKey: [UPDATE_INCOMING_INVOICE],
  });
  return (
    <Modal
      className="incoming-invoice-update-modal modal-default"
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      okSubmitForm={FORM.INCOMING_INVOICE}
      disabledOk={!!isCreating}
      fullscreen
      titleHeader="Cập nhật hoá đơn đầu vào"
    >
      <IncomingInvoiceForm type="update" toggle={toggle} toggleSuccess={toggleSuccess} selectedRecord={selectedRecord} />
    </Modal>
  );
};

export default IncomingInvoiceUpdateModals;
