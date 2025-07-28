import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import OfficesForm from '../components/offices-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_WORKSPACE } = MUTATION_KEY;

interface IOfficesCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

const OfficesCreateModals = (props: IOfficesCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_WORKSPACE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-offices"
      okText="Tạo mới"
      okSubmitForm={FORM.OFFICES}
      disabledOk={!!isCreating}
    >
      <Typography level={5}>Tạo văn phòng/ nhà máy</Typography>
      <OfficesForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default OfficesCreateModals;
