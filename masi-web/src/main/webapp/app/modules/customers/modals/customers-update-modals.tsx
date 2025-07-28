import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import CustomerForm from '../components/customers-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_CUSTOMER } = MUTATION_KEY;

interface ICustomerUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const CustomersUpdateModals = (props: ICustomerUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_CUSTOMER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      okSubmitForm={FORM.CUSTOMER}
      disabledOk={!!isUpdating}
      className='modals-update-customers'
      titleHeader='Cập nhật thông tin khách hàng'
    >
      <CustomerForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default CustomersUpdateModals;
