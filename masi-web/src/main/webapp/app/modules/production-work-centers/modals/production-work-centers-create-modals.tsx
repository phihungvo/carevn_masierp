import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ProductionWorkCentersForm from '../components/production-work-centers-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const { CREATE_WORK_CENTER } = MUTATION_KEY;

interface IProductionWorkCentersCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

const ProductionWorkCentersCreateModals = (
  props: IProductionWorkCentersCreateModals,
) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_WORK_CENTER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Tạo mới"
      okSubmitForm={FORM.PRODUCTION_WORK_CENTERS}
      disabledOk={!!isCreating}
      titleHeader="Tạo mới cụm máy sản xuất"
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

export default ProductionWorkCentersCreateModals;
