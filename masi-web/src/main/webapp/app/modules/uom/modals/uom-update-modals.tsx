import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UomForm from '../components/uom-form';

const { UPDATE_UOM } = MUTATION_KEY;

interface IUomUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const UomUpdateModals = (props: IUomUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_UOM],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-uom"
      okText="Cập nhật"
      okSubmitForm={FORM.UOM}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật đơn vị'
    >
      <UomForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default UomUpdateModals;
