import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import DocumentaryForm from '../components/documentary-form';

const { UPDATE_DOCUMENTARY } = MUTATION_KEY;

interface IDocumentaryUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const DocumentaryUpdateModals = (props: IDocumentaryUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, setSelectedRowKeys } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_DOCUMENTARY],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-update-documentary"
      okText="Cập nhật"
      okSubmitForm={FORM.ORDER}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật công văn'
    >
      <DocumentaryForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        setSelectedRowKeys={setSelectedRowKeys}
      />
    </Modal>
  );
};

export default DocumentaryUpdateModals;
