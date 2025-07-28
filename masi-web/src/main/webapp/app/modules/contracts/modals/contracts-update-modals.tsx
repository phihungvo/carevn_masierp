import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ContractsForm from '../components/contracts-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_CONTRACT } = MUTATION_KEY;

// const { useGetFilesContract } = useContracts;

interface IContractsUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
  contractStatus: string;
}

const ContractsUpdateModals = (props: IContractsUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, contractStatus } = props;

  // const { data } = useGetFilesContract();

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_CONTRACT],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-contracts modal-default"
      okText="Cập nhật"
      okSubmitForm={FORM.CUSTOMER}
      disabledOk={!!isUpdating}
      fullscreen
      titleHeader='Cập nhật hợp đồng'
    >
      <ContractsForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        contractStatus={contractStatus}
      />
    </Modal>
  );
};

export default ContractsUpdateModals;
