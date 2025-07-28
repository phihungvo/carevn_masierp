import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import AnnualLeaveForm from '../components/annual-leave-form';
import useAnnualLeave from 'app/hooks/use-annual-leave';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const { PATCH_ANNUAL_LEAVE } = MUTATION_KEY;
const { useAnnualLeaves } = useAnnualLeave;

interface IAnnualLeaveCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

const AnnualLeaveCreateModals = (props: IAnnualLeaveCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isMutating = useIsMutating({
    mutationKey: [PATCH_ANNUAL_LEAVE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-annual-leave"
      okText="Cập nhật"
      okSubmitForm={FORM.ANNUAL_LEAVE}
      disabledOk={!!isMutating}
      titleHeader='Cập nhật phép năm'
    >
      <AnnualLeaveForm toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default AnnualLeaveCreateModals;
