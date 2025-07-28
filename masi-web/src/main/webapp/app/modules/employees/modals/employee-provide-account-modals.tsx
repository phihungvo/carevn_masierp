import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ProvideAccountForm from '../form/provide-account-form';

const { CREATE_EMPLOYEE_ACCOUNT } = MUTATION_KEY;

interface IEmployeeProvideAccountModalProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const EmployeeProvideAccountModals = (props: IEmployeeProvideAccountModalProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;
  const isMutating = useIsMutating({
    mutationKey: [CREATE_EMPLOYEE_ACCOUNT],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="employee-provide-account-modal"
      okSubmitForm={FORM.CREATE_ACCOUNT}
      disabledOk={!!isMutating}
      titleHeader='Cấp tài khoản cho nhân viên'
    >
      <ProvideAccountForm
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default EmployeeProvideAccountModals;
