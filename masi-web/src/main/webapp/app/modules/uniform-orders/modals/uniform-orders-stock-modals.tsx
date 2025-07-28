import Modal from 'app/components/modal/modal';
import React from 'react';
import UniformStockForm from '../components/uniform-stock-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';
import { FORM } from 'app/shared/model/enumerations/form.model';

const { CREATE_UNIFORM_ORDER_STOCK } = MUTATION_KEY;

interface IUniformOrdersStockModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const UniformOrdersStockModals = (props: IUniformOrdersStockModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggle();
    toggleSuccess();
    setSelectedRecord(null);
  };

  const isMutating = useIsMutating({ mutationKey: [CREATE_UNIFORM_ORDER_STOCK] });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-order-stock"
      okText="Xác nhận"
      disabledOk={!!isMutating}
      okSubmitForm={FORM.UNIFORM}
      titleHeader='Nhập kho đơn hàng đồng phục'
    >
      <UniformStockForm onOkSuccess={onOkSuccess} selectedRecord={selectedRecord} />
    </Modal>
  );
};

export default UniformOrdersStockModals;
