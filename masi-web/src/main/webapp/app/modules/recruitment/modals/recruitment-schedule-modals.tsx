import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ScheduleForm from '../components/schedule-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const { CREATE_INTERVIEW } = MUTATION_KEY;

interface IRecruitmentScheduleModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const RecruitmentScheduleModals = (props: IRecruitmentScheduleModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isMutating = useIsMutating({
    mutationKey: [CREATE_INTERVIEW],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-schedule-recruitment"
      okText="Xác nhận"
      okSubmitForm={FORM.RECRUITMENT}
      disabledOk={!!isMutating}
      titleHeader='Lên lịch phỏng vấn'
    >
      <ScheduleForm type='create' toggle={toggle} toggleSuccess={toggleSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />
    </Modal>
  );
};

export default RecruitmentScheduleModals;
