import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCustomers from 'app/hooks/use-customers';
import React from 'react';

const { useDisableCustomerMutation } = useCustomers;

interface ICustomersDisposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const CustomersDisposeModals = (props: ICustomersDisposeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useDisableCustomerMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        toggle();
        toggleSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-dispose-customer-success"
      okText="Xác nhận"
      onOk={onOk}
      isabledOk={isPending}
      titleHeader='Vô hiệu khách hàng'
    >
      <Typography level={4}>Bạn muốn vô hiệu khách hàng này?</Typography>
    </Modal>
  );
};

export default CustomersDisposeModals;
