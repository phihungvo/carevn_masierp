import React from 'react';
import { useIsMutating } from '@tanstack/react-query';

import Modal from 'app/components/modal/modal';
import ScheduleForm from '../components/schedule-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';

const { CREATE_INTERVIEW } = MUTATION_KEY;

interface IRecruitmentSchedulUpdateeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const RecruitmentScheduleUpdateModals = (props: IRecruitmentSchedulUpdateeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isMutating = useIsMutating({
    mutationKey: [CREATE_INTERVIEW],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-schedule-recruitment"
      okText="Cập nhật"
      okSubmitForm={FORM.RECRUITMENT}
      disabledOk={!!isMutating}
      titleHeader='Cập nhật lịch phỏng vấn'
    >
      <ScheduleForm isOpen={isOpen} type='update' toggle={toggle} toggleSuccess={toggleSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />
    </Modal>
  );
};

export default RecruitmentScheduleUpdateModals;
