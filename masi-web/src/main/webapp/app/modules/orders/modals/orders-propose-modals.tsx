import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelectMulti from 'app/components/form/form-select-multi';
import Modal from 'app/components/modal/modal';
import useEmployee from 'app/hooks/use-employee';
import useOrders from 'app/hooks/use-orders';
import employeesMapping from 'app/modules/employees/employees-mapping';
import { IEmployeeProfiles, IListDepartment } from 'app/shared/model/employee.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { CreateOrderReviewFormSchema, createOrderReviewSchema } from 'app/validation/order.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { employeeDepartmentMapping } = employeesMapping;
const { useGetEmployeeListDepartments } = useEmployee;
const { usePostOrderReviewMutation } = useOrders;

interface IOrdersProposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const OrdersProposeModals = (props: IOrdersProposeModalsProps) => {
  const { isOpen, toggle, selectedRecord, toggleSuccess, setSelectedRecord } = props;

  const { control, handleSubmit, reset, watch } = useForm({
    resolver: zodResolver(createOrderReviewSchema),
  });

  const { data: dataDepartments, isLoading: loadingDepartments } = useGetEmployeeListDepartments();
  const { mutate, isPending } = usePostOrderReviewMutation(toggle, toggleSuccess);

  const onSubmit: SubmitHandler<CreateOrderReviewFormSchema> = values => {
    mutate({
      documentId: selectedRecord,
      employeeId1: values.employeesHCNS?.[0]?.value,
      employeeId2: values.employeesHCNS?.[1]?.value,
      employeeId3: values.employeesSALE?.[0]?.value,
      employeeId4: values.employeesSALE?.[1]?.value,
      employeeId5: values.employeesWORKER?.[0]?.value,
      employeeId6: values.employeesWORKER?.[1]?.value,
      employeeId7: values.employeesLOGPUR?.[0]?.value,
      employeeId8: values.employeesLOGPUR?.[1]?.value,
    });
    setSelectedRecord(null);
    reset();
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-propose-order"
      okText="Xác nhận"
      disabledOk={isPending}
      okSubmitForm={FORM.ORDER_REVIEW}
      titleHeader='Yêu cầu xét duyệt'
    >
      <Form onSubmit={handleSubmit(onSubmit)} id={FORM.ORDER_REVIEW}>
        <Row>
          {dataDepartments &&
            Object.keys(dataDepartments).map((key: keyof IListDepartment) => (
              <Col md={6} key={key}>
                <FormSelectMulti
                  control={control}
                  id={key}
                  name={key}
                  placeholder="Chọn người duyệt"
                  label={employeeDepartmentMapping(key)}
                  options={dataDepartments?.[key]?.map((e: IEmployeeProfiles) => ({
                    label: e?.fullName,
                    value: e?.id,
                    isDisabled: watch(key)?.length === 2,
                  }))}
                  isLoading={loadingDepartments}
                />
              </Col>
            ))}
        </Row>
      </Form>
    </Modal>
  );
};

export default OrdersProposeModals;
