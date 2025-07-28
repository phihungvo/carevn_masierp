import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import WarehouseTypeForm from '../components/warehouse-type-form';

const { CREATE_WAREHOUSE_TYPE } = MUTATION_KEY;

interface IWarehouseTypeCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const WarehouseTypeCreateModals = (props: IWarehouseTypeCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_WAREHOUSE_TYPE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-warehouse-type"
      okText="Tạo mới"
      okSubmitForm={FORM.WAREHOUSE_TYPE}
      disabledOk={!!isCreating}
      titleHeader='Tạo loại kho'
    >
      <WarehouseTypeForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default WarehouseTypeCreateModals;
