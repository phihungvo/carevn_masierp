import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';
import { IOrderParams } from 'app/shared/model/order.model';
import React, { useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import ordersMapping from '../orders-mapping';
import { DEFAULT_PAGE } from 'app/constants/common';

const { ordersTextMapping } = ordersMapping;

interface IOrdersFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IOrderParams>>;
}

const OrdersFilterModals = (props: IOrdersFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [orderStatus, setOrderStatus] = useState<ORDER_STATUS[]>([]);

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: ORDER_STATUS) => {
    if (e.target.checked && e.target.value === value) {
      setOrderStatus(prev => [...prev, value]);
    } else {
      setOrderStatus(prev => prev.filter(item => item !== value));
    }
  };

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      statuses: orderStatus,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setOrderStatus([]);
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
      className="orders-filter-modals"
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
                {ordersTextMapping(ORDER_STATUS.NEW)}
              </Label>
              <Input
                id="new"
                name="new"
                type="checkbox"
                value={ORDER_STATUS.NEW}
                checked={orderStatus.includes(ORDER_STATUS.NEW)}
                onChange={e => onChangeStatus(e, ORDER_STATUS.NEW)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="accepted">
                {ordersTextMapping(ORDER_STATUS.APPROVED)}
              </Label>
              <Input
                id="accepted"
                name="accepted"
                type="checkbox"
                value={ORDER_STATUS.APPROVED}
                checked={orderStatus.includes(ORDER_STATUS.APPROVED)}
                onChange={e => onChangeStatus(e, ORDER_STATUS.APPROVED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="pending">
                {ordersTextMapping(ORDER_STATUS.WAITING_APPROVAL)}
              </Label>
              <Input
                id="pending"
                name="pending"
                type="checkbox"
                value={ORDER_STATUS.WAITING_APPROVAL}
                checked={orderStatus.includes(ORDER_STATUS.WAITING_APPROVAL)}
                onChange={e => onChangeStatus(e, ORDER_STATUS.WAITING_APPROVAL)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="rejected">
                {ordersTextMapping(ORDER_STATUS.REJECTED)}
              </Label>
              <Input
                id="rejected"
                name="rejected"
                type="checkbox"
                value={ORDER_STATUS.REJECTED}
                checked={orderStatus.includes(ORDER_STATUS.REJECTED)}
                onChange={e => onChangeStatus(e, ORDER_STATUS.REJECTED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="cancelled">
                {ordersTextMapping(ORDER_STATUS.CANCELLED)}
              </Label>
              <Input
                id="cancelled"
                name="cancelled"
                type="checkbox"
                value={ORDER_STATUS.CANCELLED}
                checked={orderStatus.includes(ORDER_STATUS.CANCELLED)}
                onChange={e => onChangeStatus(e, ORDER_STATUS.CANCELLED)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default OrdersFilterModals;
