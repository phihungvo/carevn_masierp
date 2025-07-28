import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UniformForm from '../components/uniform-form';

const { CREATE_UNIFORM_RELEASE } = MUTATION_KEY;

interface IUniformCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const UniformCreateModals = (props: IUniformCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_UNIFORM_RELEASE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-default"
      fullscreen
      okText="Tạo mới"
      okSubmitForm={FORM.ORDER}
      disabledOk={!!isCreating}
      titleHeader='Tạo mới'
    >
      <UniformForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default UniformCreateModals;
