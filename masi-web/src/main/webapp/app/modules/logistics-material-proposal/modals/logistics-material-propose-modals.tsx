import { Col, Row } from 'reactstrap';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';

import Form from 'app/components/form/form';
import Modal from 'app/components/modal/modal';
import useEmployee from 'app/hooks/use-employee';
import FormSelect from 'app/components/form/form-select';
import { zodResolver } from '@hookform/resolvers/zod';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Typography } from 'app/components/typography/typography';
import { logisticsMaterialProposalApproveSchema, LogisticsMaterialProposalApproveSchema } from 'app/validation/logistics-material-proposal';

interface ILogisticsMaterialProposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const { useGetEmployeesQuery } = useEmployee;

const LogisticsMaterialProposeModals = (props: ILogisticsMaterialProposeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, reset } = useForm<LogisticsMaterialProposalApproveSchema>({
    resolver: zodResolver(logisticsMaterialProposalApproveSchema),
  });

  const { data, isLoading } = useGetEmployeesQuery();

  const onSubmit = (data: LogisticsMaterialProposalApproveSchema) => {
    toggleSuccess()
    reset();
    setSelectedRecord(null);
  };

  // useEffect(() => {
  //   if (isOpen) {
  //     reset();
  //   }
  // }, [isOpen]);

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-propose-modals" okText="Gửi" okSubmitForm={FORM.LOGISTICS_MATERIAL_PROPOSAL_APPROVE}>
      <Typography level={3}>Gửi yêu cầu xét duyệt</Typography>
      <Form id={FORM.LOGISTICS_MATERIAL_PROPOSAL_APPROVE} onSubmit={handleSubmit(onSubmit)}>
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

export default LogisticsMaterialProposeModals;
