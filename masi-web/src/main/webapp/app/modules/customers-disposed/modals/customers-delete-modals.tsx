import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCustomers from 'app/hooks/use-customers';
import React from 'react';

const { useDeleteCustomerMutation } = useCustomers;

interface ICustomersDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

const CustomersDeleteModals = (props: ICustomersDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useDeleteCustomerMutation();

  const handleDelete = () => {
    if (selectedRecord) {
      mutate(selectedRecord, {
        onSuccess: () => {
          toggle();
          toggleSuccess && toggleSuccess();
        },
      });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-customer-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={handleDelete}
      titleHeader='Xóa khách hàng'
    >
      <Typography level={4}>Bạn muốn xóa khách hàng này?</Typography>
    </Modal>
  );
};

export default CustomersDeleteModals;
