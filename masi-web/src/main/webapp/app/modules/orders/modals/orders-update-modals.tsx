import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import OrdersForm from '../components/orders-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const { UPDATE_ORDER } = MUTATION_KEY;

interface IOrdersUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const OrdersUpdateModals = (props: IOrdersUpdateModals) => {
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
      okSubmitForm={FORM.ORDER}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật đơn đặt hàng'
    >
      <OrdersForm
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

export default OrdersUpdateModals;
