import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React, { useState } from 'react';
import LeaveRegisterForm from '../components/leave-register-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_LEAVE_REGIME } = MUTATION_KEY;

interface ILeaveRegisterCreateModal {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

export const LeaveRegisterCreateModal = (props: ILeaveRegisterCreateModal) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const [isCheck, setIscheck] = useState<boolean>(false);

  const isCreating = useIsMutating({
    mutationKey: [CREATE_LEAVE_REGIME],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-leave-register"
      okText="Tạo mới"
      okSubmitForm={FORM.LEAVE_REGISTER}
      disabledOk={!!isCreating || isCheck}
      loadingOk={!!isCreating}
      titleHeader='Tạo đăng ký nghỉ chế độ'
    >
      <LeaveRegisterForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} setIscheck={setIscheck} />
    </Modal>
  );
};
