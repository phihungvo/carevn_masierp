import DatePicker from 'app/components/date-picker/date-picker';
import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import useOnClickOutside from 'app/hooks/use-click-outside';
import leaveRequestMapping from 'app/modules/leave-request/leave-request-mapping';
import React, { useCallback, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import leaveRegisterMapping from '../leave-register-mapping';
import { ILeaveRegimeParams } from 'app/shared/model/leave-regime.model';
import { LEAVE_REGIME_STATUS } from 'app/shared/model/enumerations/leave-regime.model';
import { LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';

const { mapLeaveRequestType } = leaveRequestMapping;
const { leaveRegimeStatusTextMapping } = leaveRegisterMapping;

interface ILeaveRegisterFilterModalProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ILeaveRegimeParams>>;
}

const LeaveRegisterFilterModal = (props: ILeaveRegisterFilterModalProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);
  const [status, setStatus] = useState<LEAVE_REGIME_STATUS[]>([]);
  const [leaveType, setLeaveType] = useState<LEAVE_REQUEST_TYPE[]>([]);
  const datePickerRef = useRef<DatePickerRef | null>(null);

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: LEAVE_REGIME_STATUS) => {
    if (e.target.checked && e.target.value === value) {
      setStatus(prev => [...prev, value]);
    } else {
      setStatus(prev => prev.filter(item => item !== value));
    }
  };

  const onChangeLeaveType = (e: React.ChangeEvent<HTMLInputElement>, value: LEAVE_REQUEST_TYPE) => {
    if (e.target.checked && e.target.value === value) {
      setLeaveType(prev => [...prev, value]);
    } else {
      setLeaveType(prev => prev.filter(item => item !== value));
    }
  };

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      status,
      leaveType,
      startDate: selectedDate[0]?.format('YYYY-MM-DD'),
      endDate: selectedDate[1]?.format('YYYY-MM-DD'),
    }));
    toggle();
  };

  const onCancel = () => {
    setStatus([]);
    setLeaveType([]);
    setSelectedDate([]);
    setFilter(prev => ({
      ...prev,
      status: [],
      leaveType: [],
      startDate: undefined,
      endDate: undefined,
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
      cancelText="Đặt lại"
      okText="Áp dụng"
      className="modals-filter-leave-register"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Label for="status">Trạng thái:</Label>
        <Row>
          <Col md={3}>
            <FormGroup check>
              <Label check for="new">
                {leaveRegimeStatusTextMapping(LEAVE_REGIME_STATUS.NEW)}
              </Label>
              <Input
                id="new"
                name="new"
                type="checkbox"
                value={LEAVE_REGIME_STATUS.NEW}
                checked={status.includes(LEAVE_REGIME_STATUS.NEW)}
                onChange={e => onChangeStatus(e, LEAVE_REGIME_STATUS.NEW)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="pending">
                {leaveRegimeStatusTextMapping(LEAVE_REGIME_STATUS.WAITING_APPROVAL)}
              </Label>
              <Input
                id="pending"
                name="pending"
                type="checkbox"
                value={LEAVE_REGIME_STATUS.WAITING_APPROVAL}
                checked={status.includes(LEAVE_REGIME_STATUS.WAITING_APPROVAL)}
                onChange={e => onChangeStatus(e, LEAVE_REGIME_STATUS.WAITING_APPROVAL)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="approved">
                {leaveRegimeStatusTextMapping(LEAVE_REGIME_STATUS.APPROVED)}
              </Label>
              <Input
                id="approved"
                name="approved"
                type="checkbox"
                value={LEAVE_REGIME_STATUS.APPROVED}
                checked={status.includes(LEAVE_REGIME_STATUS.APPROVED)}
                onChange={e => onChangeStatus(e, LEAVE_REGIME_STATUS.APPROVED)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="rejected">
                {leaveRegimeStatusTextMapping(LEAVE_REGIME_STATUS.REJECTED)}
              </Label>
              <Input
                id="rejected"
                name="rejected"
                type="checkbox"
                value={LEAVE_REGIME_STATUS.REJECTED}
                checked={status.includes(LEAVE_REGIME_STATUS.REJECTED)}
                onChange={e => onChangeStatus(e, LEAVE_REGIME_STATUS.REJECTED)}
              />
            </FormGroup>
          </Col>

          <Col md={3}>
            <FormGroup check>
              <Label check for="cancelled">
                {leaveRegimeStatusTextMapping(LEAVE_REGIME_STATUS.CANCEL)}
              </Label>
              <Input
                id="cancelled"
                name="cancelled"
                type="checkbox"
                value={LEAVE_REGIME_STATUS.CANCEL}
                checked={status.includes(LEAVE_REGIME_STATUS.CANCEL)}
                onChange={e => onChangeStatus(e, LEAVE_REGIME_STATUS.CANCEL)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>

      <FormGroup>
        <Label for="status">Loại:</Label>
        <Row>
          <Col md={4}>
            <FormGroup check>
              <Label check for="annual_leave">
                {mapLeaveRequestType(LEAVE_REQUEST_TYPE.ANNUAL_LEAVE)}
              </Label>
              <Input
                id="annual_leave"
                name="annual_leave"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.ANNUAL_LEAVE}
                checked={leaveType.includes(LEAVE_REQUEST_TYPE.ANNUAL_LEAVE)}
                onChange={e => onChangeLeaveType(e, LEAVE_REQUEST_TYPE.ANNUAL_LEAVE)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="noSalary">
                {mapLeaveRequestType(LEAVE_REQUEST_TYPE.UNPAID_LEAVE)}
              </Label>
              <Input
                id="noSalary"
                name="noSalary"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.UNPAID_LEAVE}
                checked={leaveType.includes(LEAVE_REQUEST_TYPE.UNPAID_LEAVE)}
                onChange={e => onChangeLeaveType(e, LEAVE_REQUEST_TYPE.UNPAID_LEAVE)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="maternity">
                {mapLeaveRequestType(LEAVE_REQUEST_TYPE.MATERNITY_LEAVE)}
              </Label>
              <Input
                id="maternity"
                name="maternity"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.MATERNITY_LEAVE}
                checked={leaveType.includes(LEAVE_REQUEST_TYPE.MATERNITY_LEAVE)}
                onChange={e => onChangeLeaveType(e, LEAVE_REQUEST_TYPE.MATERNITY_LEAVE)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="wedding">
                {mapLeaveRequestType(LEAVE_REQUEST_TYPE.WEDDING_LEAVE)}
              </Label>
              <Input
                id="wedding"
                name="wedding"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.WEDDING_LEAVE}
                checked={leaveType.includes(LEAVE_REQUEST_TYPE.WEDDING_LEAVE)}
                onChange={e => onChangeLeaveType(e, LEAVE_REQUEST_TYPE.WEDDING_LEAVE)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="funeral">
                {mapLeaveRequestType(LEAVE_REQUEST_TYPE.FUNERAL_LEAVE)}
              </Label>
              <Input
                id="funeral"
                name="funeral"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.FUNERAL_LEAVE}
                checked={leaveType.includes(LEAVE_REQUEST_TYPE.FUNERAL_LEAVE)}
                onChange={e => onChangeLeaveType(e, LEAVE_REQUEST_TYPE.FUNERAL_LEAVE)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="sick">
                {mapLeaveRequestType(LEAVE_REQUEST_TYPE.SICK_LEAVE)}
              </Label>
              <Input
                id="sick"
                name="sick"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.SICK_LEAVE}
                checked={leaveType.includes(LEAVE_REQUEST_TYPE.SICK_LEAVE)}
                onChange={e => onChangeLeaveType(e, LEAVE_REQUEST_TYPE.SICK_LEAVE)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="compensation_leave">
                {mapLeaveRequestType(LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE)}
              </Label>
              <Input
                id="compensation_leave"
                name="compensation_leave"
                type="checkbox"
                value={LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE}
                checked={leaveType.includes(LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE)}
                onChange={e => onChangeLeaveType(e, LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>

      <FormGroup>
        <Label>Thời gian</Label>
        <DatePicker value={selectedDate} ref={datePickerRef} onChange={onChangeSelectedDate} name="date" range arrow={false} closeExportCalendar={closeExportCalendar} />
      </FormGroup>
    </Modal>
  );
};

export default LeaveRegisterFilterModal;
