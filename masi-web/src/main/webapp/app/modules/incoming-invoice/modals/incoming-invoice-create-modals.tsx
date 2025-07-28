import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import IncomingInvoiceForm from '../components/incoming-invoices-form';

const { CREATE_INCOMING_INVOICE } = MUTATION_KEY;

interface IIncomingInvoiceCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const IncomingInvoiceCreateModals = (props: IIncomingInvoiceCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_INCOMING_INVOICE],
  });
  return (
    <Modal
      className="incoming-invoice-create-modal modal-default"
      isOpen={isOpen}
      toggle={toggle}
      okText="Tạo mới"
      okSubmitForm={FORM.INCOMING_INVOICE}
      disabledOk={!!isCreating}
      fullscreen
      titleHeader="Tạo mới hoá đơn đầu vào"
    >
      <IncomingInvoiceForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default IncomingInvoiceCreateModals;
