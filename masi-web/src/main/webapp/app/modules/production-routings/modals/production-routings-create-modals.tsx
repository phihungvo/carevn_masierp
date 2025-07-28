import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ProductionWorkCentersForm from '../components/production-routings-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_PRODUCTION_ROUTING } = MUTATION_KEY;

interface IProductionRoutingsCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

const ProductionRoutingsCreateModals = (
  props: IProductionRoutingsCreateModals,
) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_PRODUCTION_ROUTING],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Lưu"
      cancelText="Hủy"
      okSubmitForm={FORM.PRODUCTION_ROUTINGS}
      disabledOk={!!isCreating}
      titleHeader="Tạo mới lưu kho"
      style={{ width: '600px' }}
    >
      <ProductionWorkCentersForm
        type="create"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
      />
    </Modal>
  );
};

export default ProductionRoutingsCreateModals;
