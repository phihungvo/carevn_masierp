import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import FactoriesForm from '../components/factories-form';

const { CREATE_FACTORIES } = MUTATION_KEY;

interface IFactoriesCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const FactoriesCreateModals = (props: IFactoriesCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_FACTORIES],
  });
  return (
    <Modal
      className="factories-create-modal"
      isOpen={isOpen}
      toggle={toggle}
      okText="Tạo mới"
      okSubmitForm={FORM.FACTORIES}
      disabledOk={!!isCreating}
      titleHeader="Tạo mới nhà máy"
    >
      <FactoriesForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default FactoriesCreateModals;
