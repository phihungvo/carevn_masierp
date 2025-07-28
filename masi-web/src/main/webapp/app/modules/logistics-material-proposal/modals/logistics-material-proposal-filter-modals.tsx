import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';

import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import logisticsMaterialProposalMapping from 'app/modules/logistics-material-proposal/logistics-material-proposal-mapping'
import { DEFAULT_PAGE } from 'app/constants/common';
import { Typography } from 'app/components/typography/typography';
import { LOGISTICS_MATERIAL_RPOPOSAL_STATUS } from 'app/shared/model/enumerations/logistics-material-proposal';
import { ILogisticsMaterialProposalParams } from 'app/shared/model/logistics-material-proposal';

const { logisticsMaterialProposalTextMapping } = logisticsMaterialProposalMapping


interface ILogisticsMaterialProposalFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ILogisticsMaterialProposalParams>>;
}

const LogisticsMaterialProposalFilterModals = (props: ILogisticsMaterialProposalFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [logisticsMaterialProposalStatus, setlogisticsMaterialProposalStatus] = useState<LOGISTICS_MATERIAL_RPOPOSAL_STATUS[]>([]);

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: LOGISTICS_MATERIAL_RPOPOSAL_STATUS) => {
    if (e.target.checked && e.target.value === value) {
      setlogisticsMaterialProposalStatus(prev => [...prev, value]);
    } else {
      setlogisticsMaterialProposalStatus(prev => prev.filter(item => item !== value));
    }
  };

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setlogisticsMaterialProposalStatus([]);
    setFilter(prev => ({
      ...prev,
      statuses: [],
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="logistics-material-proposal-filter-modals"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Typography level={5}>Bộ lọc</Typography>
      <FormGroup>
        <Label for="approved">Tình trạng</Label>
        <Row>
          <Col md={3}>
            <FormGroup check>
              <Label check for="new">
                {logisticsMaterialProposalTextMapping(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW)}
              </Label>
              <Input
                id="new"
                name="new"
                type="checkbox"
                value={LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW}
                checked={logisticsMaterialProposalStatus.includes(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW)}
                onChange={e => onChangeStatus(e, LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="accepted">
                {logisticsMaterialProposalTextMapping(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED)}
              </Label>
              <Input
                id="accepted"
                name="accepted"
                type="checkbox"
                value={LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED}
                checked={logisticsMaterialProposalStatus.includes(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED)}
                onChange={e => onChangeStatus(e, LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="pending">
                {logisticsMaterialProposalTextMapping(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL)}
              </Label>
              <Input
                id="pending"
                name="pending"
                type="checkbox"
                value={LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL}
                checked={logisticsMaterialProposalStatus.includes(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL)}
                onChange={e => onChangeStatus(e, LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="rejected">
                {logisticsMaterialProposalTextMapping(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED)}
              </Label>
              <Input
                id="rejected"
                name="rejected"
                type="checkbox"
                value={LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED}
                checked={logisticsMaterialProposalStatus.includes(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED)}
                onChange={e => onChangeStatus(e, LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="is-delivering">
                {logisticsMaterialProposalTextMapping(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.IS_DELIVERING)}
              </Label>
              <Input
                id="is-delivering"
                name="is-delivering"
                type="checkbox"
                value={LOGISTICS_MATERIAL_RPOPOSAL_STATUS.IS_DELIVERING}
                checked={logisticsMaterialProposalStatus.includes(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.IS_DELIVERING)}
                onChange={e => onChangeStatus(e, LOGISTICS_MATERIAL_RPOPOSAL_STATUS.IS_DELIVERING)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="completed">
                {logisticsMaterialProposalTextMapping(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.COMPLETED)}
              </Label>
              <Input
                id="completed"
                name="completed"
                type="checkbox"
                value={LOGISTICS_MATERIAL_RPOPOSAL_STATUS.COMPLETED}
                checked={logisticsMaterialProposalStatus.includes(LOGISTICS_MATERIAL_RPOPOSAL_STATUS.COMPLETED)}
                onChange={e => onChangeStatus(e, LOGISTICS_MATERIAL_RPOPOSAL_STATUS.COMPLETED)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default LogisticsMaterialProposalFilterModals;
