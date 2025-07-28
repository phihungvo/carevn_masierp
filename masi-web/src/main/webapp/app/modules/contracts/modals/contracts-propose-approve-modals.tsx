import { Col, Row } from 'reactstrap';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

import Form from 'app/components/form/form';
import Modal from 'app/components/modal/modal';
import useEmployee from 'app/hooks/use-employee';
import useContracts from 'app/hooks/use-contracts';
import FormSelect from 'app/components/form/form-select';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import { proposeApproveContractsSchema } from 'app/validation/contract.validation';

const { usePatchContractProposeApprove } = useContracts;
const { useGetEmployeeProfilesQuery } = useEmployee;

interface IContractsProposeApproveModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (id: string) => void;
}

const ContractsProposeApproveModals = (props: IContractsProposeApproveModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, setValue, reset } = useForm({
    resolver: zodResolver(proposeApproveContractsSchema),
  });

  const { data: employees, isLoading } = useGetEmployeeProfilesQuery();
  const { mutate } = usePatchContractProposeApprove(selectedRecord, toggle, toggleSuccess);

  const onSubmit = values => {
    mutate({
      documentId: selectedRecord,
      employeeIds: [values?.employeesOne, values?.employeesTwo, values?.employeesThree, values?.employeesFour].filter(item => item != undefined)
    })
  }

  useEffect(() => {
    if (!isOpen) {
      setValue('employeesOne', '')
      setValue('employeesTwo', '')
      setValue('employeesThree', '')
      setValue('employeesFour', '')
    }
  }, [isOpen])

  useEffect(() => {
    if (!isOpen) {
      reset();
      setSelectedRecord(null);
    };
  }, [isOpen])

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      // onOk={onOk}
      okSubmitForm={FORM.APPROVAL_CONTRACT}
      className='contracts-proposo-approve-modals'
      titleHeader='Đề xuất xét duyệt HĐ'
    >
      <Form onSubmit={handleSubmit(onSubmit)} id={FORM.APPROVAL_CONTRACT}>
        <Row>
          <Col md={6} >
            <FormSelect
              control={control}
              id='employeesOne'
              name='employeesOne'
              placeholder="Chọn người duyệt 1"
              label='Người duyệt 1'
              options={employees?.data?.map((e: IEmployeeProfiles) => ({
                label: e?.fullName,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6} >
            <FormSelect
              control={control}
              id='employeesTwo'
              name='employeesTwo'
              placeholder="Chọn người duyệt 2"
              label='Người duyệt 2'
              options={employees?.data?.map((e: IEmployeeProfiles) => ({
                label: e?.fullName,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6} >
            <FormSelect
              control={control}
              id='employeesThree'
              name='employeesThree'
              placeholder="Chọn người duyệt 3"
              label='Người duyệt 3'
              options={employees?.data?.map((e: IEmployeeProfiles) => ({
                label: e?.fullName,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6} >
            <FormSelect
              control={control}
              id='employeesFour'
              name='employeesFour'
              placeholder="Chọn người duyệt 4"
              label='Người duyệt 4'
              options={employees?.data?.map((e: IEmployeeProfiles) => ({
                label: e?.fullName,
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

export default ContractsProposeApproveModals;
