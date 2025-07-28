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
import { createContractReviewSchema } from 'app/validation/contract.validation';

const { useGetEmployeeProfilesQuery } = useEmployee;
const { usePatchContractRequestLiquidationReview } = useContracts;

interface IContractsProposeLiquidModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (id: string) => void;
}

const ContractsProposeLiquidModals = (props: IContractsProposeLiquidModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, setValue } = useForm({
    resolver: zodResolver(createContractReviewSchema),
  });

  const { data: employees, isLoading } = useGetEmployeeProfilesQuery();
  const { mutate } = usePatchContractRequestLiquidationReview(selectedRecord, toggle, toggleSuccess)

  const onSubmit = values => {
    mutate({
      documentId: selectedRecord,
      employeeIds: [values?.employeesOne, values?.employeesTwo]
    })
    setSelectedRecord(null);
  }

  useEffect(() => {
    if (!isOpen) {
      setValue('employeesOne', '')
      setValue('employeesTwo', '')
    }
  }, [isOpen])

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className='contracts-propose-liquid-modals'
      okSubmitForm={FORM.CONTRACT_REVIEW}
      titleHeader='Đề xuất thanh lý HĐ'
    >
      <Form onSubmit={handleSubmit(onSubmit)} id={FORM.CONTRACT_REVIEW}>
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
        </Row>
      </Form>
    </Modal>
  );
};

export default ContractsProposeLiquidModals;
