import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCustomers from 'app/hooks/use-customers';
import useEmployee from 'app/hooks/use-employee';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { CustomerTransferFormSchema, customerTransferSchema } from 'app/validation/customer.validation';
import React from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';

const { useGetEmployeesQuery } = useEmployee;
const { useTransferCustomerMutation } = useCustomers;

interface ICustomersTransferModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

const CustomersTransferModals = (props: ICustomersTransferModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { control, handleSubmit, reset } = useForm<CustomerTransferFormSchema>({
    resolver: zodResolver(customerTransferSchema),
  });

  const { data, isLoading } = useGetEmployeesQuery();
  const { mutate } = useTransferCustomerMutation();

  const onSubmit: SubmitHandler<CustomerTransferFormSchema> = values => {
    mutate(
      {
        id: selectedRecord,
        newOwnerId: values.customerOwner,
      },
      {
        onSuccess: () => {
          reset();
          toggle();
          toggleSuccess();
        },
      },
    );
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-transfer-customer"
      cancel={false}
      okText="Chuyển giao"
      okSubmitForm={FORM.CUSTOMER_TRANSFER}
      titleHeader='Nhân viên cần chuyển giao'
    >
      <Form id={FORM.CUSTOMER_TRANSFER} onSubmit={handleSubmit(onSubmit)}>
        <FormSelect
          control={control}
          id="customerOwner"
          name="customerOwner"
          placeholder="Chọn nhân viên"
          label="Tên nhân viên"
          options={data?.data?.map(e => ({
            label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
            value: e?.id,
          }))}
          isLoading={isLoading}
        />
      </Form>
    </Modal>
  );
};

export default CustomersTransferModals;
