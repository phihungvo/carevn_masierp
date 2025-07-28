import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ProductionMaintenanceForm from '../components/production-maintenance-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_PRODUCTION_MAINTAIN } = MUTATION_KEY;

interface IProductionMaintenanceCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

const ProductionMaintenanceCreateModals = (
  props: IProductionMaintenanceCreateModals,
) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_PRODUCTION_MAINTAIN],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-prod-maintenance"
      okText="Tạo mới"
      okSubmitForm={FORM.PRODUCTION_MAINTENANCE}
      disabledOk={!!isCreating}
      titleHeader="Tạo mới lô hàng"
      style={{ width: '600px' }}
    >
      <ProductionMaintenanceForm
        type="create"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
      />
    </Modal>
  );
};

export default ProductionMaintenanceCreateModals;
