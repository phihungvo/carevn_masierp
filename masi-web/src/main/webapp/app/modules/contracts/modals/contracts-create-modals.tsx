import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ContractsForm from '../components/contracts-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const { CREATE_CONTRACT } = MUTATION_KEY;

// const { useGetFilesContract } = useContracts;

interface IContractsCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

const ContractsCreateModals = (props: IContractsCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  // const { data } = useGetFilesContract();

  const isCreating = useIsMutating({
    mutationKey: [CREATE_CONTRACT],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-contracts modal-default"
      okText="Tạo mới"
      okSubmitForm={FORM.CUSTOMER}
      disabledOk={!!isCreating}
      size='xl'
      titleHeader='Tạo mới hợp đồng'
    >
      <ContractsForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default ContractsCreateModals;
