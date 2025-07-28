import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UniformSettingsForm from '../components/uniform-settings-form';

const { CREATE_UNIFORM } = MUTATION_KEY;

interface IUniformSettingsCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const UniformSettingsCreateModals = (props: IUniformSettingsCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_UNIFORM],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-uniform-settings"
      okText="Tạo mới"
      okSubmitForm={FORM.UNIFORM}
      disabledOk={!!isCreating}
      titleHeader='Tạo đồng phục'
    >
      <UniformSettingsForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default UniformSettingsCreateModals;
