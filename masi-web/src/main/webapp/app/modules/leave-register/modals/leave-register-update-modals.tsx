import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React, { useState } from 'react';
import LeaveRegisterForm from '../components/leave-register-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_LEAVE_REGIME } = MUTATION_KEY;

interface ILeaveRegisterUpdateModal {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

export const LeaveRegisterUpdateModal = (props: ILeaveRegisterUpdateModal) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const [isCheck, setIscheck] = useState<boolean>(false);

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_LEAVE_REGIME],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-leave-register"
      okText="Cập nhật"
      okSubmitForm={FORM.LEAVE_REGISTER}
      disabledOk={!!isUpdating || isCheck}
      loadingOk={!!isUpdating}
      titleHeader='Cập nhật đăng ký nghỉ chế độ'
    >
      <LeaveRegisterForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectRecord={setSelectedRecord}
        setIscheck={setIscheck}
      />
    </Modal>
  );
};
