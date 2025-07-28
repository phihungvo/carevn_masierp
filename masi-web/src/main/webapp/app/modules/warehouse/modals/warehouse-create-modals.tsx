import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import WarehouseForm from '../components/warehouse-form';

const { CREATE_WAREHOUSE } = MUTATION_KEY;

interface IWarehouseCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const WarehouseCreateModals = (props: IWarehouseCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_WAREHOUSE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-warehouse"
      okText="Tạo mới"
      okSubmitForm={FORM.WAREHOUSE}
      disabledOk={!!isCreating}
      titleHeader='Tạo kho'
    >
      <WarehouseForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default WarehouseCreateModals;
