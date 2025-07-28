import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UniformSettingsForm from '../components/uniform-settings-form';

const { UPDATE_UNIFORM } = MUTATION_KEY;

interface IUniformSettingsUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const UniformSettingsUpdateModals = (props: IUniformSettingsUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_UNIFORM],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-uniform-settings"
      okText="Cập nhật"
      okSubmitForm={FORM.UNIFORM}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật đồng phục'
    >
      <UniformSettingsForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default UniformSettingsUpdateModals;
