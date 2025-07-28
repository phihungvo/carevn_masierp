import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import PurchaseForm from '../components/purchase-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_PURCHASE } = MUTATION_KEY;

interface IPurchaseUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const PurchaseUpdateModals = (props: IPurchaseUpdateModalsProps) => {
  const { isOpen, toggle, selectedRecord, toggleSuccess, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_PURCHASE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-purchase"
      okText="Cập nhật"
      okSubmitForm={FORM.PURCHASE}
      disabledOk={!!isUpdating}
    >
      <Typography level={3}>Cập nhật đề nghị thu mua</Typography>

      <PurchaseForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default PurchaseUpdateModals;
