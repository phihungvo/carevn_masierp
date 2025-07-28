import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UniformOrdersForm from '../components/uniform-orders-form';

const { CREATE_ORDER } = MUTATION_KEY;

interface IUniformOrdersCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const UniformOrdersCreateModals = (props: IUniformOrdersCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_ORDER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-default"
      fullscreen
      okText="Tạo mới"
      okSubmitForm={FORM.UNIFORM}
      disabledOk={!!isCreating}
      titleHeader='Tạo đơn hàng đồng phục'
    >
      <UniformOrdersForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default UniformOrdersCreateModals;
