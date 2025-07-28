import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import MachineryEquipmentForm from '../components/machinery-equipment-form';

const { UPDATE_MACHINERY_EQUIPMENT } = MUTATION_KEY;

interface IMachineryEquipmentUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

const MachineryEquipmentUpdateModals = (props: IMachineryEquipmentUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const isCreating = useIsMutating({
    mutationKey: [UPDATE_MACHINERY_EQUIPMENT],
  });
  return (
    <Modal
      className="machinery-equipment-update-modal"
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      okSubmitForm={FORM.MACHINERY_EQUIPMENT }
      disabledOk={!!isCreating}
      titleHeader="Cập nhật máy móc thiết bị"
    >
      <MachineryEquipmentForm type="update" toggle={toggle} toggleSuccess={toggleSuccess} selectedRecord={selectedRecord} />
    </Modal>
  );
};

export default MachineryEquipmentUpdateModals;
