import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCustomers from 'app/hooks/use-customers';
import React from 'react';

const { useActivateCustomerMutation } = useCustomers;

interface ICustomersActivateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

const CustomersActivateModals = (props: ICustomersActivateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useActivateCustomerMutation();

  const handleActivate = () => {
    if (selectedRecord) {
      mutate(selectedRecord, {
        onSuccess: () => {
          toggleSuccess && toggleSuccess();
          toggle();
        },
      });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-activate-customer-success"
      okText="Xác nhận"
      onOk={handleActivate}
      disabledOk={!!isPending}
      titleHeader='Kích hoạt lại khách hàng'
    >
      <Typography level={4}>Bạn có muốn kích hoạt lại khách hàng này?</Typography>
    </Modal>
  );
};

export default CustomersActivateModals;
