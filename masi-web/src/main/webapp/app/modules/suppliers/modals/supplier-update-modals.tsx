import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import SupplierForm from '../components/supplier-form';

const { UPDATE_SUPPLIER } = MUTATION_KEY;

interface ISupplierUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  toggleSelectItem?: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const SupplierUpdateModals = (props: ISupplierUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, toggleSelectItem } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_SUPPLIER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-supplier modal-default"
      okText="Cập nhật"
      okSubmitForm={FORM.SUPPLIER}
      disabledOk={!!isUpdating}
      fullscreen
      titleHeader='Cập nhật nhà cung cấp'
    >
      <SupplierForm
        toggleSelectItem={toggleSelectItem}
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default SupplierUpdateModals;
