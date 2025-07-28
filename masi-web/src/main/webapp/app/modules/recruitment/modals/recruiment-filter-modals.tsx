import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import recruitmentMapping from '../recruitment-mapping';
import {
  INTERVIEW_RESULT,
  RECRUITMENT_POSITION,
  RECRUITMENT_PROCESS,
  RECRUITMENT_STATUS,
} from 'app/shared/model/enumerations/recruitment.model';
import { IRecruitmentParams } from 'app/shared/model/recruitment.model';
import { DEFAULT_PAGE } from 'app/constants/common';

const { recruitmentStatusTextMapping, recruitmentProcessTextMapping, interviewResultTextMapping, recruitmentPositionTextMapping } =
  recruitmentMapping;

interface IRecruitmentFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IRecruitmentParams>>;
}

const RecruitmentFilterModals = (props: IRecruitmentFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  // const [status, setStatus] = useState<RECRUITMENT_STATUS>();
  const [process, setProcess] = useState<RECRUITMENT_PROCESS>();
  const [result, setResult] = useState<INTERVIEW_RESULT>();
  const [position, setPosition] = useState<RECRUITMENT_POSITION>();
  const [status, setStatus] = useState<RECRUITMENT_STATUS[]>([]);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      status,
      process,
      result,
      position,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: RECRUITMENT_STATUS) => {
    if (e.target.checked && e.target.value === value) {
      setStatus(prev => [...prev, value]);
    } else {
      setStatus(prev => prev.filter(item => item !== value));
    }
  };

  const onCancel = () => {
    setStatus([]);
    setProcess(undefined);
    setResult(undefined);
    setPosition(undefined);

    setFilter(prev => ({
      ...prev,
      status: undefined,
      process: undefined,
      result: undefined,
      position: undefined,
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
        <Col md={12}>
          {/* <FormGroup>
            <Label for="status">Trạng thái</Label>
            <Input name="status" id="status" type="select" onChange={e => setStatus(e.target.value as RECRUITMENT_STATUS)} value={status}>
              <option selected disabled>
                Chọn trạng thái
              </option>
              <option value={RECRUITMENT_STATUS.WAITING_APPROVAL}>
                {recruitmentStatusTextMapping(RECRUITMENT_STATUS.WAITING_APPROVAL)}
              </option>
              <option value={RECRUITMENT_STATUS.APPROVED}>{recruitmentStatusTextMapping(RECRUITMENT_STATUS.APPROVED)}</option>
              <option value={RECRUITMENT_STATUS.REJECTED}>{recruitmentStatusTextMapping(RECRUITMENT_STATUS.REJECTED)}</option>
            </Input>
          </FormGroup> */}
          <FormGroup>
            <Label for="status">Trạng thái:</Label>
            <Row>
              <Col md={3}>
                <FormGroup check>
                  <Label check for="approved">
                    {recruitmentStatusTextMapping(RECRUITMENT_STATUS.APPROVED)}
                  </Label>
                  <Input
                    id="approved"
                    name="approved"
                    type="checkbox"
                    value={RECRUITMENT_STATUS.APPROVED}
                    checked={status?.includes(RECRUITMENT_STATUS.APPROVED)}
                    onChange={e => onChangeStatus(e, RECRUITMENT_STATUS.APPROVED)}
                  />
                </FormGroup>
              </Col>

              <Col md={3}>
                <FormGroup check>
                  <Label check for="rejected">
                    {recruitmentStatusTextMapping(RECRUITMENT_STATUS.REJECTED)}
                  </Label>
                  <Input
                    id="rejected"
                    name="rejected"
                    type="checkbox"
                    value={RECRUITMENT_STATUS.REJECTED}
                    checked={status?.includes(RECRUITMENT_STATUS.REJECTED)}
                    onChange={e => onChangeStatus(e, RECRUITMENT_STATUS.REJECTED)}
                  />
                </FormGroup>
              </Col>

              <Col md={3}>
                <FormGroup check>
                  <Label check for="waitingApproval">
                    {recruitmentStatusTextMapping(RECRUITMENT_STATUS.WAITING_APPROVAL)}
                  </Label>
                  <Input
                    id="waitingApproval"
                    name="waitingApproval"
                    type="checkbox"
                    value={RECRUITMENT_STATUS.WAITING_APPROVAL}
                    checked={status?.includes(RECRUITMENT_STATUS.WAITING_APPROVAL)}
                    onChange={e => onChangeStatus(e, RECRUITMENT_STATUS.WAITING_APPROVAL)}
                  />
                </FormGroup>
              </Col>

              <Col md={3}>
                <FormGroup check>
                  <Label check for="waitingInterview">
                    {recruitmentStatusTextMapping(RECRUITMENT_STATUS.WAITING_INTERVIEW)}
                  </Label>
                  <Input
                    id="waitingInterview"
                    name="waitingInterview"
                    type="checkbox"
                    value={RECRUITMENT_STATUS.WAITING_INTERVIEW}
                    checked={status?.includes(RECRUITMENT_STATUS.WAITING_INTERVIEW)}
                    onChange={e => onChangeStatus(e, RECRUITMENT_STATUS.WAITING_INTERVIEW)}
                  />
                </FormGroup>
              </Col>

              <Col md={3}>
                <FormGroup check>
                  <Label check for="completed">
                    {recruitmentStatusTextMapping(RECRUITMENT_STATUS.COMPLETED)}
                  </Label>
                  <Input
                    id="completed"
                    name="completed"
                    type="checkbox"
                    value={RECRUITMENT_STATUS.COMPLETED}
                    checked={status?.includes(RECRUITMENT_STATUS.COMPLETED)}
                    onChange={e => onChangeStatus(e, RECRUITMENT_STATUS.COMPLETED)}
                  />
                </FormGroup>
              </Col>
            </Row>
          </FormGroup>
        </Col>
        {/* <Col md={6}>
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
        </Col> */}
        {/* <Col md={6}>
          <FormGroup>
            <Label for="result">Kết quả</Label>
            <Input name="result" id="result" type="select" onChange={e => setResult(e.target.value as INTERVIEW_RESULT)} value={result}>
              <option selected disabled>
                Chọn kết quả
              </option>
              <option value={INTERVIEW_RESULT.FAIL}>{interviewResultTextMapping(INTERVIEW_RESULT.FAIL)}</option>
              <option value={INTERVIEW_RESULT.HOLD}>{interviewResultTextMapping(INTERVIEW_RESULT.HOLD)}</option>
              <option value={INTERVIEW_RESULT.PASS}>{interviewResultTextMapping(INTERVIEW_RESULT.PASS)}</option>
            </Input>
          </FormGroup>
        </Col> */}
        <Col md={12}>
          <FormGroup>
            <Label for="position">Vị trí</Label>
            <Input
              name="position"
              id="position"
              type="select"
              onChange={e => setPosition(e.target.value as RECRUITMENT_POSITION)}
              value={position}
            >
              <option selected disabled>
                Chọn vị trí
              </option>
              <option value={RECRUITMENT_POSITION.EMPLOYEE}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.EMPLOYEE)}</option>
              <option value={RECRUITMENT_POSITION.TEAM_LEADER}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.TEAM_LEADER)}</option>
              <option value={RECRUITMENT_POSITION.SUPERVISOR}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.SUPERVISOR)}</option>
              <option value={RECRUITMENT_POSITION.DIRECTOR}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.DIRECTOR)}</option>
            </Input>
          </FormGroup>
        </Col>
      </Row>
    </Modal>
  );
};

export default RecruitmentFilterModals;
