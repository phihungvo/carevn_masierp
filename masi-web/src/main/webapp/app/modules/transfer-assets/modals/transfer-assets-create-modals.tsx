import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import TransferAssetsForm from '../components/transfer-assets-form';

const { CREATE_TRANSFER_ASSETS } = MUTATION_KEY;

interface ITransferAssetsCreateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const TransferAssetsCreateModals = (props: ITransferAssetsCreateModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_TRANSFER_ASSETS],
  });
  return (
    <Modal
      className="supplies-create-modal"
      isOpen={isOpen}
      toggle={toggle}
      okText="Tạo mới"
      okSubmitForm={FORM.TRANSFER_ASSETS}
      disabledOk={!!isCreating}
      titleHeader="Tạo mới Mã VT"
    >
      <TransferAssetsForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default TransferAssetsCreateModals;
