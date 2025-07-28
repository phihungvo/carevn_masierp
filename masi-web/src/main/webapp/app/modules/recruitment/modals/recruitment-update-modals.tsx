import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import RecruitmentForm from '../components/recruitment-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_RECRUITMENT } = MUTATION_KEY;

interface IRecruitmentUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const RecruitmentUpdateModals = (props: IRecruitmentUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_RECRUITMENT],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-default"
      okText="Cập nhật"
      okSubmitForm={FORM.RECRUITMENT}
      disabledOk={!!isUpdating}
      fullscreen
      titleHeader='Cập nhật yêu cầu tuyển dụng'
    >
      <RecruitmentForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default RecruitmentUpdateModals;
