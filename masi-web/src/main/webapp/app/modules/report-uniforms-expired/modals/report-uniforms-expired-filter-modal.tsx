import React, { useState } from 'react';

import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import reportUniformsExpiredMapping from '../report-uniforms-expired-mapping'
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { Typography } from 'app/components/typography/typography';
import { IRecruitmentParams } from 'app/shared/model/recruitment.model';
import { EUniformStatus } from "app/shared/model/enumerations/report-uniforms-expired";

const { formatUniformExpirationStatusMapping } = reportUniformsExpiredMapping

interface IReportUniformsExpiredFilterModalProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IRecruitmentParams>>;
}

const ReportUniformsExpiredFilterModal = (props: IReportUniformsExpiredFilterModalProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [type, setType] = useState<EUniformStatus>(EUniformStatus.UNALLOCATED);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      type
    }));
    toggle();
  };

  const onCancel = () => {
    setType(undefined)

    setFilter(prev => ({
      ...prev,
      type: EUniformStatus.UNALLOCATED,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancelText="Đặt lại"
      okText="Áp dụng"
      className="modals-filter-report-uniform-expired"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Row>
        <Col md={12}>
          <FormGroup>
            <Label for="type">Loại:</Label>
            <Row>
              <Col md={6} >
                <FormGroup check>
                  <Label check for="unallocated">
                    {formatUniformExpirationStatusMapping(EUniformStatus.UNALLOCATED)}
                  </Label>
                  <Input
                    id="unallocated"
                    name="unallocated"
                    type="checkbox"
                    value={EUniformStatus.UNALLOCATED}
                    checked={type === EUniformStatus.UNALLOCATED}
                    onChange={e => setType(e.target.value as EUniformStatus)}
                  />
                </FormGroup>
              </Col>
              <Col md={6}>
                <FormGroup check>
                  <Label check for="allocated">
                    {formatUniformExpirationStatusMapping(EUniformStatus.ALLOCATED)}
                  </Label>
                  <Input
                    id="allocated"
                    name="allocated"
                    type="checkbox"
                    value={EUniformStatus.ALLOCATED}
                    checked={type === EUniformStatus.ALLOCATED}
                    onChange={e => setType(e.target.value as EUniformStatus)}
                  />
                </FormGroup>
              </Col>
            </Row>
          </FormGroup>
        </Col>
      </Row>
    </Modal>
  );
};

export default ReportUniformsExpiredFilterModal;
