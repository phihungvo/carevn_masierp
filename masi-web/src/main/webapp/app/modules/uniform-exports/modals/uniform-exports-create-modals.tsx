import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UniformForm from '../components/uniform-exports-form';

const { CREATE_ORDER } = MUTATION_KEY;

interface IUniformExportsCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const UniformExportsCreateModals = (props: IUniformExportsCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_ORDER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-uniform-return"
      okText="Tạo mới"
      okSubmitForm={FORM.ORDER}
      disabledOk={!!isCreating}
      titleHeader='Hoàn ứng'
    >
      <UniformForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default UniformExportsCreateModals;
