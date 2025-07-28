import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UomForm from '../components/uom-form';

const { CREATE_UOM } = MUTATION_KEY;

interface IUomCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const UomCreateModals = (props: IUomCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_UOM],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-uom"
      okText="Tạo mới"
      okSubmitForm={FORM.UOM}
      disabledOk={!!isCreating}
      titleHeader='Tạo đơn vị'
    >
      <UomForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default UomCreateModals;
