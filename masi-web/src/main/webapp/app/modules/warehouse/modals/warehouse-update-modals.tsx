import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import WarehouseForm from '../components/warehouse-form';

const { UPDATE_WAREHOUSE } = MUTATION_KEY;

interface IWarehouseUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const WarehouseUpdateModals = (props: IWarehouseUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_WAREHOUSE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-warehouse"
      okText="Cập nhật"
      okSubmitForm={FORM.WAREHOUSE}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật kho'
    >
      <WarehouseForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default WarehouseUpdateModals;
