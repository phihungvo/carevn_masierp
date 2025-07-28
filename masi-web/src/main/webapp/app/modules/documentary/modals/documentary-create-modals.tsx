import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import DocumentaryForm from '../components/documentary-form';

const { CREATE_DOCUMENTARY } = MUTATION_KEY;

interface IDocumentaryCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const DocumentaryCreateModals = (props: IDocumentaryCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_DOCUMENTARY],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-documentary"
      okText="Tạo mới"
      okSubmitForm={FORM.ORDER}
      disabledOk={!!isCreating}
      titleHeader='Tạo mới công văn'
    >
      <DocumentaryForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default DocumentaryCreateModals;
