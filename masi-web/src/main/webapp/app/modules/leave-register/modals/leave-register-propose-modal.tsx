import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import useEmployee from 'app/hooks/use-employee';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ProcessLeaveRegimeSchema, processLeaveRegimeSchema } from 'app/validation/leave-regime.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetEmployeesQuery } = useEmployee;
const { useProcessLeaveRegime } = useLeaveRegime;

interface ILeaveRegisterProposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const LeaveRegisterProposeModals = (props: ILeaveRegisterProposeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, reset } = useForm<ProcessLeaveRegimeSchema>({
    resolver: zodResolver(processLeaveRegimeSchema),
  });

  const onOkSuccess = () => {
    toggle();
    toggleSuccess();
    setSelectedRecord(null);
  };

  const { data, isLoading } = useGetEmployeesQuery();
  const { mutate, isPending } = useProcessLeaveRegime(selectedRecord, onOkSuccess);

  const onSubmit: SubmitHandler<ProcessLeaveRegimeSchema> = values => {
    mutate({
      approverIds: [values.employeeId1, values.employeeId2, values.employeeId3, values.employeeId4].filter(e => !!e),
    });
    reset();
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-propose-leave-register"
      okText="Xác nhận"
      disabledOk={isPending}
      okSubmitForm={FORM.LEAVE_REGISTER}
      titleHeader='Yêu cầu xét duyệt'
    >
      <Form onSubmit={handleSubmit(onSubmit)} id={FORM.LEAVE_REGISTER}>
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId1"
              name="employeeId1"
              placeholder="Chọn người duyệt"
              label="Người duyệt 1"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId2"
              name="employeeId2"
              placeholder="Chọn người duyệt"
              label="Người duyệt 2"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId3"
              name="employeeId3"
              placeholder="Chọn người duyệt"
              label="Người duyệt 3"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId4"
              name="employeeId4"
              placeholder="Chọn người duyệt"
              label="Người duyệt 4"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default LeaveRegisterProposeModals;
