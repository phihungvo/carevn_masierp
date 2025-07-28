import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import ItemForm from '../components/item-form';

const { UPDATE_ITEM } = MUTATION_KEY;

interface IItemUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const ItemUpdateModals = (props: IItemUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_ITEM],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-item"
      okText="Cập nhật"
      okSubmitForm={FORM.ITEM}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật vật phẩm'
    >
      <ItemForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default ItemUpdateModals;
