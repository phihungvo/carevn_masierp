import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import FactoriesForm from '../components/factories-form';

const { UPDATE_FACTORIES } = MUTATION_KEY;

interface IFactoriesUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

const FactoriesUpdateModals = (props: IFactoriesUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const isCreating = useIsMutating({
    mutationKey: [UPDATE_FACTORIES],
  });
  return (
    <Modal
      className="factories-update-modal"
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      okSubmitForm={FORM.FACTORIES}
      disabledOk={!!isCreating}
      titleHeader="Cập nhật nhà máy"
    >
      <FactoriesForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
      />
    </Modal>
  );
};

export default FactoriesUpdateModals;
