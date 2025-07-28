import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import SuppliesForm from '../components/supplies-form';

const { UPDATE_SUPPLIES } = MUTATION_KEY;

interface ISuppliesUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

const SuppliesUpdateModals = (props: ISuppliesUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const isCreating = useIsMutating({
    mutationKey: [UPDATE_SUPPLIES],
  });
  return (
    <Modal
      className="supplies-update-modal"
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      okSubmitForm={FORM.SUPPLIES}
      disabledOk={!!isCreating}
      titleHeader="Cập nhật mã VT - CCDC"
    >
      <SuppliesForm type="update" toggle={toggle} toggleSuccess={toggleSuccess} selectedRecord={selectedRecord} />
    </Modal>
  );
};

export default SuppliesUpdateModals;
