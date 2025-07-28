import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import recruitmentMapping from '../recruitment-mapping';
import { INTERVIEW_RESULT } from 'app/shared/model/enumerations/recruitment.model';
import useRecruitment from 'app/hooks/use-recruitment';
import { interviewResultSchema, InterviewResultSchema } from 'app/validation/recruitment.validation';
import { zodResolver } from '@hookform/resolvers/zod';

const { interviewResultTextMapping } = recruitmentMapping;
const { usePatchInterviewResult } = useRecruitment;

interface IRecruitmentResultModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const RecruitmentResultModals = (props: IRecruitmentResultModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, reset } = useForm<InterviewResultSchema>({
    resolver: zodResolver(interviewResultSchema),
  });

  const { mutate, isPending } = usePatchInterviewResult(selectedRecord, toggle, toggleSuccess);

  const onSubmit = (data: InterviewResultSchema) => {
    mutate(
      {
        interviewResult: data.interviewResult,
        rate: data.rate,
      },
      {
        onSuccess: () => {
          setSelectedRecord(null);
          reset();
        },
      },
    );
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-result-recruitment"
      okText="Xác nhận"
      okSubmitForm={FORM.RECRUITMENT}
      disabledOk={isPending}
      titleHeader='Cập nhật kết quả phỏng vấn'
    >
      <Form id={FORM.RECRUITMENT} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="interviewResult" name="interviewResult" label="Kết quả" type="select">
              <option selected disabled>
                Chọn kết quả
              </option>
              <option value={INTERVIEW_RESULT.FAIL}>{interviewResultTextMapping(INTERVIEW_RESULT.FAIL)}</option>
              <option value={INTERVIEW_RESULT.PASS}>{interviewResultTextMapping(INTERVIEW_RESULT.PASS)}</option>
              <option value={INTERVIEW_RESULT.HOLD}>{interviewResultTextMapping(INTERVIEW_RESULT.HOLD)}</option>
            </FormInput>
          </Col>
          <Col md={6}>
            <FormInput control={control} id="rate" name="rate" label="Đánh giá" />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default RecruitmentResultModals;
