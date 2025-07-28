import { Col, Row } from 'reactstrap';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';

import Form from 'app/components/form/form';
import Modal from 'app/components/modal/modal';
import useEmployee from 'app/hooks/use-employee';
import useRecruitment from 'app/hooks/use-recruitment';
import FormSelect from 'app/components/form/form-select';
import FormDatePicker from 'app/components/form/form-date-picker';
import { zodResolver } from '@hookform/resolvers/zod';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { RecruitmentRenewFormSchema, recruitmentRenewSchema } from 'app/validation/recruitment.validation';

interface IRecruitmentRenewModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const { useGetEmployeesQuery } = useEmployee;
const { useGetRecruitmentById, usePatchRecruitmentAdjourn } = useRecruitment;

const RecruitmentRenewModals = (props: IRecruitmentRenewModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, setValue, reset, formState, watch } = useForm<RecruitmentRenewFormSchema>({
    resolver: zodResolver(recruitmentRenewSchema),
  });

  const { data: detail } = useGetRecruitmentById(selectedRecord);
  const { mutate: renew, isPending } = usePatchRecruitmentAdjourn(selectedRecord, toggle, toggleSuccess);
  const { data, isLoading } = useGetEmployeesQuery();

  const onSubmit = (data: RecruitmentRenewFormSchema) => {
    const { deadline, deadlineOld, ...rest } = data;

    renew({
      ...detail,
      deadline: deadline?.toDate()?.toISOString(),
      empIds: Object.values(rest)?.filter(value => value !== undefined),
    });

    reset();
    setSelectedRecord(null);
  };

  useEffect(() => {
    if (detail) {
      setValue('deadline', new DateObject());
      setValue('deadlineOld', new DateObject(detail?.deadline).add(7, 'hours'));
    }
  }, [detail]);

  useEffect(() => {
    if (isOpen) {
      reset();
      setValue('deadline', new DateObject());
    }
  }, [isOpen]);

  const list = data?.data?.map(e => ({
    label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
    value: e?.id,
  }))

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-renew-recruitment"
      loadingOk={isPending}
      okText="Gia hạn"
      okSubmitForm={FORM.RECRUITMENT_RENEW}
      titleHeader='Gia hạn yêu cầu tuyển dụng'
    >
      <Form id={FORM.RECRUITMENT_RENEW} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={12}>
            <FormDatePicker setValue={setValue} control={control} id="deadline" name="deadline" label="Ngày gia hạn" formState={formState} />
          </Col>

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
              options={list}
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
              options={list}
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
              options={list}
              isLoading={isLoading}
            />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default RecruitmentRenewModals;
