import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React, { useState } from 'react';
import { LeaveRequestForm } from '../leave-request-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const { CREATE_LEAVE_REQUEST } = MUTATION_KEY;

// MODAL REQUEST LEAVE REQUEST
interface IModalRequestLeaveRequest {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

export const RequestLeaveRequestModal = (props: IModalRequestLeaveRequest) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const [isCheck, setIscheck] = useState<boolean>(false);

  const isCreatingLeaveRequest = useIsMutating({
    mutationKey: [CREATE_LEAVE_REQUEST],
  });

  return (
    <Modal
      disabledOk={!!isCreatingLeaveRequest || isCheck}
      loadingOk={!!isCreatingLeaveRequest}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-request-day-off"
      okText="Tạo mới"
      okSubmitForm={FORM.LEAVE_REQUEST}
      titleHeader='Lập đơn nghỉ phép'
    >
      <LeaveRequestForm toggle={toggle} toggleSuccess={toggleSuccess} setIscheck={setIscheck} />
    </Modal>
  );
};

// MODAL REQUEST SUCCESS
interface IModalRequestSuccess {
  isOpen: boolean;
  toggle: () => void;
  cancel: boolean;
}

export const RequestSuccessModal = (props: IModalRequestSuccess) => {
  const { isOpen, toggle, cancel } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={cancel}
      titleHeader='Gửi đơn thành công'
    >
      <Typography level={4}>Bạn đã gửi đơn nghỉ phép thành công</Typography>
    </Modal>
  );
};
