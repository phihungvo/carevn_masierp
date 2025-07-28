import Modal from 'app/components/modal/modal';
import React from 'react';
import ConfirmLeaveForm from '../form/confirm-leave-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { PATCH_CONFIRM_LEAVE } = MUTATION_KEY;

interface IEmployeeConfirmLeaveModalProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const EmployeeConfirmLeaveModal = (props: IEmployeeConfirmLeaveModalProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isMutating = useIsMutating({
    mutationKey: [PATCH_CONFIRM_LEAVE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="confirm-leave-modal"
      okSubmitForm={FORM.CONFIRM_LEAVE}
      disabledOk={!!isMutating}
      titleHeader='Xác nhận đã nghỉ'
    >
      <ConfirmLeaveForm
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default EmployeeConfirmLeaveModal;
