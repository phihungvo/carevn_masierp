import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { Typography } from 'app/components/typography/typography';
import { ProductionProcessForm } from '../production-process-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_PRODUCTION_PROCESS } = MUTATION_KEY;

// MODAL UPDATE PROCESS
interface IModalUpdateProcess {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

export const ModalUpdateProcess = (props: IModalUpdateProcess) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const isUpdatingProdProcess = useIsMutating({ mutationKey: [UPDATE_PRODUCTION_PROCESS] });

  return (
    <Modal
      disabledOk={!!isUpdatingProdProcess}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-process"
      okText="Cập nhật"
      okSubmitForm={FORM.PRODUCTION_PROCESS}
    >
      <Typography level={4}>Cập nhật công đoạn sản xuất</Typography>
      <ProductionProcessForm type="update" toggle={toggle} toggleSuccess={toggleSuccess} selectedRecord={selectedRecord} />
    </Modal>
  );
};

// MODAL UPDATE PROCESS SUCCESS
interface IModalUpdateProcessSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalUpdateProcessSuccess = (props: IModalUpdateProcessSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-process-success" okText="Đồng ý" cancel={false}>
      <Typography level={3}>Cập nhật thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật công đoạn sản xuất thành công</Typography>
    </Modal>
  );
};
