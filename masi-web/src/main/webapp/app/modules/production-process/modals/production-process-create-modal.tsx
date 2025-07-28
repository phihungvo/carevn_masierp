import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { Typography } from 'app/components/typography/typography';
import { ProductionProcessForm } from '../production-process-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_PRODUCTION_PROCESS } = MUTATION_KEY;

// MODAL CREATE PROCESS
interface IModalCreateProcess {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

export const ModalCreateProcess = (props: IModalCreateProcess) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreatingProcess = useIsMutating({ mutationKey: [CREATE_PRODUCTION_PROCESS] });

  return (
    <Modal
      disabledOk={!!isCreatingProcess}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-process"
      okText="Tạo mới"
      okSubmitForm={FORM.PRODUCTION_PROCESS}
    >
      <Typography level={4}>Tạo mới công đoạn sản xuất</Typography>
      <ProductionProcessForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

// MODAL CREATE PROCESS SUCCESS
interface IModalCreateProcessSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalCreateProcessSuccess = (props: IModalCreateProcessSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-process-success" cancel={false}>
      <Typography level={3}>Tạo mới thành công</Typography>
      <Typography level={4}>Bạn đã tạo mới công đoạn sản xuất thành công</Typography>
    </Modal>
  );
};
