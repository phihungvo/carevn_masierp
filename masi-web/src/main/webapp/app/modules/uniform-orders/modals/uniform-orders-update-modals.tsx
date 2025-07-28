import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UniformForm from '../components/uniform-orders-form';

const { UPDATE_ORDER } = MUTATION_KEY;

interface IUniformOrdersUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const UniformOrdersUpdateModals = (props: IUniformOrdersUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, setSelectedRowKeys } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_ORDER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-default"
      fullscreen
      okText="Cập nhật"
      okSubmitForm={FORM.UNIFORM}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật đơn hàng'
    >
      <UniformForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        setSelectedRowKeys={setSelectedRowKeys}
      />
    </Modal>
  );
};

export default UniformOrdersUpdateModals;
