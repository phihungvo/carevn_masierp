import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import RecruitmentForm from '../components/recruitment-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_RECRUITMENT } = MUTATION_KEY;

interface IRecruitmentCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const RecruitmentCreateModals = (props: IRecruitmentCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_RECRUITMENT],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-recruitment modal-default"
      okText="Tạo mới"
      okSubmitForm={FORM.RECRUITMENT}
      disabledOk={!!isCreating}
      fullscreen
      titleHeader='Tạo mới yêu cầu tuyển dụng'
    >
      <RecruitmentForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default RecruitmentCreateModals;
