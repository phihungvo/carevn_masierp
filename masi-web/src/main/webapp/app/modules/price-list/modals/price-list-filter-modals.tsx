import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { IQuotationParams } from 'app/shared/model/quotation.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import priceListMapping from '../price-list-mapping';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';
import { DEFAULT_PAGE } from 'app/constants/common';

const { priceListMappingText } = priceListMapping;

interface IPriceListFilterModals {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IQuotationParams>>;
}

const PriceListFilterModals = (props: IPriceListFilterModals) => {
  const { isOpen, toggle, setFilter } = props;

  const [status, setStatus] = useState<QUOTATION_STATUS[]>([]);

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: QUOTATION_STATUS) => {
    if (e.target.checked && e.target.value === value) {
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
    setFilter(prev => ({
      ...prev,
      status: [],
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="price-list-filter-modals"
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
                {priceListMappingText(QUOTATION_STATUS.NEW)}
              </Label>
              <Input
                id="new"
                name="new"
                type="checkbox"
                value={QUOTATION_STATUS.NEW}
                checked={status.includes(QUOTATION_STATUS.NEW)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.NEW)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="pending">
                {priceListMappingText(QUOTATION_STATUS.WAITING_APPROVAL)}
              </Label>
              <Input
                id="pending"
                name="pending"
                type="checkbox"
                value={QUOTATION_STATUS.WAITING_APPROVAL}
                checked={status.includes(QUOTATION_STATUS.WAITING_APPROVAL)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.WAITING_APPROVAL)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="accepted">
                {priceListMappingText(QUOTATION_STATUS.APPROVED)}
              </Label>
              <Input
                id="accepted"
                name="accepted"
                type="checkbox"
                value={QUOTATION_STATUS.APPROVED}
                checked={status.includes(QUOTATION_STATUS.APPROVED)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.APPROVED)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="customerAccepted">
                {priceListMappingText(QUOTATION_STATUS.CUSTOMER_APPROVED)}
              </Label>
              <Input
                id="customerAccepted"
                name="customerAccepted"
                type="checkbox"
                value={QUOTATION_STATUS.CUSTOMER_APPROVED}
                checked={status.includes(QUOTATION_STATUS.CUSTOMER_APPROVED)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.CUSTOMER_APPROVED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="needUpdate">
                {priceListMappingText(QUOTATION_STATUS.NEED_UPDATE)}
              </Label>
              <Input
                id="needUpdate"
                name="needUpdate"
                type="checkbox"
                value={QUOTATION_STATUS.NEED_UPDATE}
                checked={status.includes(QUOTATION_STATUS.NEED_UPDATE)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.NEED_UPDATE)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="customerSent">
                {priceListMappingText(QUOTATION_STATUS.SENT)}
              </Label>
              <Input
                id="customerSent"
                name="customerSent"
                type="checkbox"
                value={QUOTATION_STATUS.SENT}
                checked={status.includes(QUOTATION_STATUS.SENT)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.SENT)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="rejected">
                {priceListMappingText(QUOTATION_STATUS.REJECTED)}
              </Label>
              <Input
                id="rejected"
                name="rejected"
                type="checkbox"
                value={QUOTATION_STATUS.REJECTED}
                checked={status.includes(QUOTATION_STATUS.REJECTED)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.REJECTED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="cancel">
                {priceListMappingText(QUOTATION_STATUS.CANCELLED)}
              </Label>
              <Input
                id="cancel"
                name="cancel"
                type="checkbox"
                value={QUOTATION_STATUS.CANCELLED}
                checked={status.includes(QUOTATION_STATUS.CANCELLED)}
                onChange={e => onChangeStatus(e, QUOTATION_STATUS.CANCELLED)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default PriceListFilterModals;
