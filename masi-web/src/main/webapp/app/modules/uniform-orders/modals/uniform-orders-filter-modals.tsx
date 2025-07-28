import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React, { useCallback, useRef, useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { DEFAULT_PAGE } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';
import DatePicker from 'app/components/date-picker/date-picker';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import uniformOrdersMapping from '../uniform-orders-mapping';
import { UNIFORM_ORDER_STATUS } from 'app/shared/model/enumerations/uniform.model';
import { IUniformOrderParams } from 'app/shared/model/uniform.model';

const { uniformOrderStatusMapping } = uniformOrdersMapping;

interface IUniformOrdersFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IUniformOrderParams>>;
}

const UniformOrdersFilterModals = (props: IUniformOrdersFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [orderStatus, setOrderStatus] = useState<UNIFORM_ORDER_STATUS[]>([]);

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: UNIFORM_ORDER_STATUS) => {
    if (e.target.checked && e.target.value === value) {
      setOrderStatus(prev => [...prev, value]);
    } else {
      setOrderStatus(prev => prev.filter(item => item !== value));
    }
  };

  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);

  const datePickerRef = useRef<DatePickerRef | null>(null);

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      status: orderStatus,
      startDate: selectedDate[0]?.toDate()?.toISOString(),
      endDate: selectedDate[1] ? selectedDate[1].toDate()?.toISOString() : selectedDate[0]?.toDate()?.toISOString(),
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setOrderStatus([]);
    setSelectedDate([]);
    setFilter(prev => ({
      ...prev,
      status: undefined,
      startDate: undefined,
      endDate: undefined,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const closeExportCalendar = () => {
    setSelectedDate(null)
    datePickerRef?.current?.closeCalendar()
  }

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
        <FormGroup>
          <Label for="approved">Trạng thái</Label>
          <Row>
            <Col md={3}>
              <FormGroup check>
                <Label check for="waiting">
                  {uniformOrderStatusMapping(UNIFORM_ORDER_STATUS.WAITING)}
                </Label>
                <Input
                  id="waiting"
                  name="waiting"
                  type="checkbox"
                  value={UNIFORM_ORDER_STATUS.WAITING}
                  checked={orderStatus.includes(UNIFORM_ORDER_STATUS.WAITING)}
                  onChange={e => onChangeStatus(e, UNIFORM_ORDER_STATUS.WAITING)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="approved">
                  {uniformOrderStatusMapping(UNIFORM_ORDER_STATUS.APPROVED)}
                </Label>
                <Input
                  id="approved"
                  name="approved"
                  type="checkbox"
                  value={UNIFORM_ORDER_STATUS.APPROVED}
                  checked={orderStatus.includes(UNIFORM_ORDER_STATUS.APPROVED)}
                  onChange={e => onChangeStatus(e, UNIFORM_ORDER_STATUS.APPROVED)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="rejected">
                  {uniformOrderStatusMapping(UNIFORM_ORDER_STATUS.REJECTED)}
                </Label>
                <Input
                  id="rejected"
                  name="rejected"
                  type="checkbox"
                  value={UNIFORM_ORDER_STATUS.REJECTED}
                  checked={orderStatus.includes(UNIFORM_ORDER_STATUS.REJECTED)}
                  onChange={e => onChangeStatus(e, UNIFORM_ORDER_STATUS.REJECTED)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="cancelled">
                  {uniformOrderStatusMapping(UNIFORM_ORDER_STATUS.CANCELLED)}
                </Label>
                <Input
                  id="cancelled"
                  name="cancelled"
                  type="checkbox"
                  value={UNIFORM_ORDER_STATUS.CANCELLED}
                  checked={orderStatus.includes(UNIFORM_ORDER_STATUS.CANCELLED)}
                  onChange={e => onChangeStatus(e, UNIFORM_ORDER_STATUS.CANCELLED)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="processing">
                  {uniformOrderStatusMapping(UNIFORM_ORDER_STATUS.PROCESSING)}
                </Label>
                <Input
                  id="processing"
                  name="processing"
                  type="checkbox"
                  value={UNIFORM_ORDER_STATUS.PROCESSING}
                  checked={orderStatus.includes(UNIFORM_ORDER_STATUS.PROCESSING)}
                  onChange={e => onChangeStatus(e, UNIFORM_ORDER_STATUS.PROCESSING)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="stocked">
                  {uniformOrderStatusMapping(UNIFORM_ORDER_STATUS.STOCKED)}
                </Label>
                <Input
                  id="stocked"
                  name="stocked"
                  type="checkbox"
                  value={UNIFORM_ORDER_STATUS.STOCKED}
                  checked={orderStatus.includes(UNIFORM_ORDER_STATUS.STOCKED)}
                  onChange={e => onChangeStatus(e, UNIFORM_ORDER_STATUS.STOCKED)}
                />
              </FormGroup>
            </Col>
          </Row>
        </FormGroup>
        <Row>
          <FormGroup>
            <Label htmlFor="date">Ngày</Label>
            <DatePicker
              value={selectedDate}
              ref={datePickerRef}
              onChange={onChangeSelectedDate}
              id="date"
              name="date"
              arrow={false}
              range
              closeExportCalendar={closeExportCalendar}
            />
          </FormGroup>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default UniformOrdersFilterModals;
