import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import { WORK_CENTER_STATUS } from 'app/shared/model/enumerations/work-center.model';
import { IWorkCenterParams } from 'app/shared/model/work-center.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { mapProdWorkCenterStatusText } from '../production-work-centers-mapping';

interface IProductionWorkCentersFilterModals {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IWorkCenterParams>>;
}

const ProductionWorkCentersFilterModals = (
  props: IProductionWorkCentersFilterModals,
) => {
  const { isOpen, toggle, setFilter } = props;

  const [status, setStatus] = useState<WORK_CENTER_STATUS[]>([]);

  const onChangeStatus = (
    e: React.ChangeEvent<HTMLInputElement>,
    value: WORK_CENTER_STATUS,
  ) => {
    if (e.target.checked && e.target.value === (value as any)) {
      setStatus(prev => [...prev, value]);
    } else {
      setStatus(prev => prev.filter(item => item !== value));
    }
  };

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      status,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setStatus([]);
    setFilter(prev => ({ ...prev, status: [], page: DEFAULT_PAGE }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="production-work-center-filter-modals"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Label for="approved">Trạng thái</Label>
        <Row>
          <Col md={3}>
            <FormGroup check>
              <Label check for="new">
                {mapProdWorkCenterStatusText(WORK_CENTER_STATUS.ACTIVE)}
              </Label>
              <Input
                id="new"
                name="new"
                type="checkbox"
                value={WORK_CENTER_STATUS.ACTIVE}
                checked={status.includes(WORK_CENTER_STATUS.ACTIVE)}
                onChange={e => onChangeStatus(e, WORK_CENTER_STATUS.ACTIVE)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="pending">
                {mapProdWorkCenterStatusText(WORK_CENTER_STATUS.DAMAGED)}
              </Label>
              <Input
                id="pending"
                name="pending"
                type="checkbox"
                value={WORK_CENTER_STATUS.DAMAGED}
                checked={status.includes(WORK_CENTER_STATUS.DAMAGED)}
                onChange={e => onChangeStatus(e, WORK_CENTER_STATUS.DAMAGED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="accepted">
                {mapProdWorkCenterStatusText(WORK_CENTER_STATUS.REPAIR)}
              </Label>
              <Input
                id="accepted"
                name="accepted"
                type="checkbox"
                value={WORK_CENTER_STATUS.REPAIR}
                checked={status.includes(WORK_CENTER_STATUS.REPAIR)}
                onChange={e => onChangeStatus(e, WORK_CENTER_STATUS.REPAIR)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="customerAccepted">
                {mapProdWorkCenterStatusText(WORK_CENTER_STATUS.PENDING)}
              </Label>
              <Input
                id="customerAccepted"
                name="customerAccepted"
                type="checkbox"
                value={WORK_CENTER_STATUS.PENDING}
                checked={status.includes(WORK_CENTER_STATUS.PENDING)}
                onChange={e => onChangeStatus(e, WORK_CENTER_STATUS.PENDING)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="liquidate">
                {mapProdWorkCenterStatusText(WORK_CENTER_STATUS.LIQUIDATE)}
              </Label>
              <Input
                id="liquidate"
                name="liquidate"
                type="checkbox"
                value={WORK_CENTER_STATUS.LIQUIDATE}
                checked={status.includes(WORK_CENTER_STATUS.LIQUIDATE)}
                onChange={e => onChangeStatus(e, WORK_CENTER_STATUS.LIQUIDATE)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default ProductionWorkCentersFilterModals;
