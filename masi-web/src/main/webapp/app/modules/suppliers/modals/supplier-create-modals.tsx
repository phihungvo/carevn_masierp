import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import SupplierForm from '../components/supplier-form';

const { CREATE_SUPPLIER } = MUTATION_KEY;

interface ISupplierCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  toggleOpenItem?: () => void;
}

const SupplierCreateModals = (props: ISupplierCreateModals) => {
  const { isOpen, toggle, toggleSuccess, toggleOpenItem } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_SUPPLIER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-supplier modal-default"
      okText="Tạo mới"
      okSubmitForm={FORM.SUPPLIER}
      disabledOk={!!isCreating}
      toggleOpenItem={toggleOpenItem}
      fullscreen
      titleHeader="Tạo nhà cung cấp"
    >
      <SupplierForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} toggleSelectItem={toggleOpenItem} />
    </Modal>
  );
};

export default SupplierCreateModals;
