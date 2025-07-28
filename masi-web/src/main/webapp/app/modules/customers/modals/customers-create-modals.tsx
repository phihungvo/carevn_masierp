import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import CustomerForm from '../components/customers-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_CUSTOMER } = MUTATION_KEY;

interface ICustomerCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const CustomerCreateModals = (props: ICustomerCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_CUSTOMER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Tạo mới"
      okSubmitForm={FORM.CUSTOMER}
      disabledOk={!!isCreating}
      className='modals-create-customers'
      titleHeader='Tạo mới khách hàng'
    >
      <CustomerForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default CustomerCreateModals;
