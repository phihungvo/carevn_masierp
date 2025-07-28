import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import recruitmentMapping from '../recruitment-mapping';
import { INTERVIEW_RESULT, RECRUITMENT_PROCESS } from 'app/shared/model/enumerations/recruitment.model';
import { Typography } from 'app/components/typography/typography';
import { IRecruitmentParams } from 'app/shared/model/recruitment.model';
import { DEFAULT_PAGE } from 'app/constants/common';

const { recruitmentProcessTextMapping, interviewResultTextMapping } = recruitmentMapping;

interface IRecruitmentFilterModalsCandidatesProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IRecruitmentParams>>;
}

const RecruitmentFilterModalsCandidates = (props: IRecruitmentFilterModalsCandidatesProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [process, setProcess] = useState<RECRUITMENT_PROCESS>();
  const [result, setResult] = useState<INTERVIEW_RESULT>();

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      process,
      interviewResult: result,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setProcess(undefined);
    setResult(undefined);

    setFilter(prev => ({
      ...prev,
      process: undefined,
      interviewResult: undefined,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancelText="Đặt lại"
      okText="Áp dụng"
      className="modals-filter-recruitment"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Row>
        <Col md={6}>
          <FormGroup>
            <Label for="status">Kết quả</Label>
            <Input name="status" id="status" type="select" onChange={e => setResult(e.target.value as INTERVIEW_RESULT)} value={result}>
              <option selected disabled>
                Chọn kết quả
              </option>
              <option value={INTERVIEW_RESULT.PASS}>{interviewResultTextMapping(INTERVIEW_RESULT.PASS)}</option>
              <option value={INTERVIEW_RESULT.HOLD}>{interviewResultTextMapping(INTERVIEW_RESULT.HOLD)}</option>
              <option value={INTERVIEW_RESULT.FAIL}>{interviewResultTextMapping(INTERVIEW_RESULT.FAIL)}</option>
            </Input>
          </FormGroup>
        </Col>
        <Col md={6}>
          <FormGroup>
            <Label for="process">Tiến trình</Label>
            <Input
              name="process"
              id="process"
              type="select"
              onChange={e => setProcess(e.target.value as RECRUITMENT_PROCESS)}
              value={process}
            >
              <option selected disabled>
                Chọn tiến trình
              </option>
              <option value={RECRUITMENT_PROCESS.INTERVIEWED}>{recruitmentProcessTextMapping(RECRUITMENT_PROCESS.INTERVIEWED)}</option>
              <option value={RECRUITMENT_PROCESS.WAITING_INTERVIEW}>
                {recruitmentProcessTextMapping(RECRUITMENT_PROCESS.WAITING_INTERVIEW)}
              </option>
            </Input>
          </FormGroup>
        </Col>
      </Row>
    </Modal>
  );
};

export default RecruitmentFilterModalsCandidates;
