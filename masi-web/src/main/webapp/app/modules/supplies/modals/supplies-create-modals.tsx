import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import SuppliesForm from '../components/supplies-form';

const { CREATE_SUPPLIES } = MUTATION_KEY;

interface ISuppliesCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const SuppliesCreateModals = (props: ISuppliesCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_SUPPLIES],
  });
  return (
    <Modal
      className="supplies-create-modal"
      isOpen={isOpen}
      toggle={toggle}
      okText="Tạo mới"
      okSubmitForm={FORM.SUPPLIES}
      disabledOk={!!isCreating}
      titleHeader="Tạo mới Mã VT"
    >
      <SuppliesForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default SuppliesCreateModals;
