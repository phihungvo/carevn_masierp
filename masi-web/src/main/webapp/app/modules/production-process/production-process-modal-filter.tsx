import Button from 'app/components/button/button';
import DatePicker from 'app/components/date-picker/date-picker';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';
import { PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';
import { IProductionCommandWithProcessParams } from 'app/shared/model/production-command.model';
import React, { Dispatch, SetStateAction, useCallback, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import productionProcessMapping from 'app/modules/production-process/production-process-mapping'

const { mapProductionProcessStatusText } = productionProcessMapping

interface IProductionProcessModalFilter {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionCommandWithProcessParams>>;
  selectedDate: DateObject[]
  setSelectedDate?: Dispatch<SetStateAction<DateObject[]>>
  status: PRODUCTION_PROCESS_STATUS[]
  setStatus: Dispatch<SetStateAction<PRODUCTION_PROCESS_STATUS[]>>
}

const ProductionProcessModalFilter = (props: IProductionProcessModalFilter) => {
  const { isOpen, toggle, setFilter, selectedDate, setSelectedDate, status, setStatus } = props;


  const datePickerRef = useRef<DatePickerRef | null>(null);

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const closeExportCalendar = () => {
    setSelectedDate([]);
    datePickerRef.current?.closeCalendar();
  };

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: string) => {
    if (e.target.checked && e.target.value === value) {
      setStatus(prev => [...prev, value as PRODUCTION_PROCESS_STATUS]);
    } else {
      setStatus(prev => prev.filter(item => item !== value));
    }
  };

  const handleFilter = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      fromDate: selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE),
      toDate: selectedDate?.[1] ? selectedDate?.[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE),
      statuses: status,
    }));
    toggle();
  };

  const handleCancelFilter = () => {
    setSelectedDate(null);
    setStatus([]);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      fromDate: '',
      toDate: '',
      statuses: [],
    }));
    toggle();
  };

  const handleDatePickerClose = useCallback(() => datePickerRef.current.closeCalendar(), [datePickerRef]);
  useOnClickOutside(datePickerRef, handleDatePickerClose);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="production-process-filter-modal"
      cancelText="Đặt lại"
      okText="Áp dụng"
      onOk={handleFilter}
      onCancel={handleCancelFilter}
    >
      <Typography level={5}>Bộ lọc</Typography>
      <Form>
        <FormGroup>
          <Label>Ngày</Label>
          <DatePicker
            value={selectedDate}
            ref={datePickerRef}
            onChange={onChangeSelectedDate}
            name="date"
            range
            arrow={false}
            closeExportCalendar={closeExportCalendar}
          />
        </FormGroup>
        <FormGroup>
          <Label for="status">Trạng thái</Label>
          <Row>
            <Col md={2}>
              <FormGroup check>
                <Label check for="new">
                  {mapProductionProcessStatusText(PRODUCTION_PROCESS_STATUS.NEW)}
                </Label>
                <Input
                  checked={status.includes(PRODUCTION_PROCESS_STATUS.NEW)}
                  id="new"
                  name="new"
                  type="checkbox"
                  value={PRODUCTION_PROCESS_STATUS.NEW}
                  onChange={e => onChangeStatus(e, PRODUCTION_PROCESS_STATUS.NEW)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="completed">
                  {mapProductionProcessStatusText(PRODUCTION_PROCESS_STATUS.COMPLETED)}

                </Label>
                <Input
                  checked={status.includes(PRODUCTION_PROCESS_STATUS.COMPLETED)}
                  id="completed"
                  name="completed"
                  type="checkbox"
                  value={PRODUCTION_PROCESS_STATUS.COMPLETED}
                  onChange={e => onChangeStatus(e, PRODUCTION_PROCESS_STATUS.COMPLETED)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="running">
                  {mapProductionProcessStatusText(PRODUCTION_PROCESS_STATUS.RUNNING)}
                </Label>
                <Input
                  checked={status.includes(PRODUCTION_PROCESS_STATUS.RUNNING)}
                  id="running"
                  name="running"
                  type="checkbox"
                  value={PRODUCTION_PROCESS_STATUS.RUNNING}
                  onChange={e => onChangeStatus(e, PRODUCTION_PROCESS_STATUS.RUNNING)}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup check>
                <Label check for="stop">
                  {mapProductionProcessStatusText(PRODUCTION_PROCESS_STATUS.STOP)}
                </Label>
                <Input
                  checked={status.includes(PRODUCTION_PROCESS_STATUS.STOP)}
                  id="stop"
                  name="stop"
                  type="checkbox"
                  value={PRODUCTION_PROCESS_STATUS.STOP}
                  onChange={e => onChangeStatus(e, PRODUCTION_PROCESS_STATUS.STOP)}
                />
              </FormGroup>
            </Col>
          </Row>
        </FormGroup>
      </Form>
    </Modal>
  );
};

export default ProductionProcessModalFilter;
