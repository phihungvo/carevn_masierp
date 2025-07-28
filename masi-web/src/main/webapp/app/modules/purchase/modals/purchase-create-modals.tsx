import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import PurchaseForm from '../components/purchase-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_PURCHASE } = MUTATION_KEY;

interface IPurchaseCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const PurchaseCreateModals = (props: IPurchaseCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_PURCHASE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-purchase"
      okText="Tạo mới"
      okSubmitForm={FORM.PURCHASE}
      disabledOk={!!isCreating}
    >
      <Typography level={4}>Tạo mới đề nghị thu mua</Typography>

      <PurchaseForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default PurchaseCreateModals;
