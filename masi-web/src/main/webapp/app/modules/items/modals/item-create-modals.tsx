import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ItemForm from '../components/item-form';

const { CREATE_ITEM } = MUTATION_KEY;

interface IItemCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const ItemCreateModals = (props: IItemCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_ITEM],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-item modal-default"
      okText="Tạo mới"
      okSubmitForm={FORM.ITEM}
      disabledOk={!!isCreating}
      fullscreen
      titleHeader='Tạo tài sản'
    >
      <ItemForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default ItemCreateModals;
