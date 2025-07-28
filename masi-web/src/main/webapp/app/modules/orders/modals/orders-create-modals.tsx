import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import OrdersForm from '../components/orders-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const { CREATE_ORDER } = MUTATION_KEY;

interface IOrdersCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const OrdersCreateModals = (props: IOrdersCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_ORDER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-default modal-default-orders"
      size='xl'
      okText="Tạo mới"
      okSubmitForm={FORM.ORDER}
      disabledOk={!!isCreating}
      titleHeader='Tạo mới đơn đặt hàng'
    >
      <OrdersForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default OrdersCreateModals;
