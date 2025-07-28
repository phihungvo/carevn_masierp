import React, { useCallback, useRef, useState } from 'react';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import Input from 'app/components/input/input';
import { ITimeKeepingExplanationParams } from 'app/shared/model/time-keeping-explanation.model';
import {
  TIME_KEEPING_EXPLANATION_REASON,
  TIME_KEEPING_EXPLANATION_STATUS,
} from 'app/shared/model/enumerations/time-keeping-explanation.model';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import Select from 'app/components/select/select';
import { BaseOption } from 'app/shared/model/pagination.model';
import useWorkspace from 'app/hooks/use-workspace';
import useEmployee from 'app/hooks/use-employee';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import DatePicker from 'app/components/date-picker/date-picker';
import useOnClickOutside from 'app/hooks/use-click-outside';

const { useGetWorkspacesQuery } = useWorkspace;
const { useGetEmployeesQuery } = useEmployee;

interface IModalFilterTimeSheetExplanation {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ITimeKeepingExplanationParams>>;
}

export const TimeSheetExplanationFilterModal = (props: IModalFilterTimeSheetExplanation) => {
  const { isOpen, toggle, setFilter } = props;

  const datePickerRef = useRef<DatePickerRef | null>(null);
  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);
  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const [statuses, setStatuses] = useState([]);
  const [types, setTypes] = useState([]);
  const [workspaceIds, setWorkspaceIds] = useState<string[]>([]);
  const [employeeIds, setEmployeeIds] = useState<string[]>([]);
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const { data: workspaces, isLoading } = useGetWorkspacesQuery();
  const { data: employees, isLoading: loadingEmployees } = useGetEmployeesQuery();

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: string) => {
    if (e.target.checked && e.target.value === value) {
      setStatuses(prev => [...prev, value]);
    } else {
      setStatuses(prev => prev.filter(item => item !== value));
    }
  };

  const onChangeType = (e: React.ChangeEvent<HTMLInputElement>, value: string) => {
    if (e.target.checked && e.target.value === value) {
      setTypes(prev => [...prev, value]);
    } else {
      setTypes(prev => prev.filter(item => item !== value));
    }
  };

  const handleFilter = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      statuses,
      types,
      startFrom: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      startTo: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      employeeIds,
      workspaceIds,
    }));
    toggle();
  };

  const handlCancelFilter = () => {
    setStatuses([]);
    setTypes([]);
    setSelectedDate([]);
    setEmployeeIds([]);
    setWorkspaceIds([]);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      statuses: [],
      types: [],
      startFrom: undefined,
      startTo: undefined,
      employeeIds: [],
      workspaceIds: [],
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
      className="modal-filter-request"
      cancelText="Đặt lại"
      okText="Áp dụng"
      onOk={handleFilter}
      onCancel={handlCancelFilter}
    >
      <FormGroup>
        <Label>Trạng thái</Label>
        <Row>
          <Col md={3}>
            <FormGroup check>
              <Label check for="accepted">
                Đã duyệt
              </Label>
              <Input
                checked={statuses.includes(TIME_KEEPING_EXPLANATION_STATUS.APPROVED)}
                id="accepted"
                name="accepted"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_STATUS.APPROVED}
                onChange={e => onChangeStatus(e, TIME_KEEPING_EXPLANATION_STATUS.APPROVED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="pending">
                Đợi duyệt
              </Label>
              <Input
                checked={statuses.includes(TIME_KEEPING_EXPLANATION_STATUS.PENDING)}
                id="pending"
                name="pending"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_STATUS.PENDING}
                onChange={e => onChangeStatus(e, TIME_KEEPING_EXPLANATION_STATUS.PENDING)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="rejected">
                Từ chối
              </Label>
              <Input
                checked={statuses.includes(TIME_KEEPING_EXPLANATION_STATUS.REJECTED)}
                id="rejected"
                name="rejected"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_STATUS.REJECTED}
                onChange={e => onChangeStatus(e, TIME_KEEPING_EXPLANATION_STATUS.REJECTED)}
              />
            </FormGroup>
          </Col>
          <Col md={3}>
            <FormGroup check>
              <Label check for="cancelled">
                Đã huỷ
              </Label>
              <Input
                checked={statuses.includes(TIME_KEEPING_EXPLANATION_STATUS.CANCELLED)}
                id="cancelled"
                name="cancelled"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_STATUS.CANCELLED}
                onChange={e => onChangeStatus(e, TIME_KEEPING_EXPLANATION_STATUS.CANCELLED)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
      <FormGroup>
        <Label for="reason">Lý do vi phạm</Label>
        <Row>
          <Col md={5}>
            <FormGroup check>
              <Label check for="missingCheck">
                Quên chấm công
              </Label>
              <Input
                checked={types.includes(TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT)}
                id="missingCheck"
                name="reason"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT}
                onChange={e => onChangeType(e, TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT)}
              />
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup check>
              <Label check for="insufficient">
                Chấm công không đúng giờ quy định
              </Label>

              <Input
                checked={types.includes(TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME)}
                id="insufficient"
                name="reason"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME}
                onChange={e => onChangeType(e, TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME)}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>

      <FormGroup>
        <Label htmlFor="workspaceIsd">Phòng ban</Label>
        <Select<BaseOption, true>
          id="workspaceId"
          name="workspaceId"
          placeholder="Chọn phòng ban"
          onChange={value => setWorkspaceIds(value?.map(item => item?.value as string))}
          value={workspaceIds?.map(item => {
            const findWsp = workspaces?.data?.find(e => e?.id === item);
            return { label: findWsp?.name, value: item };
          })}
          options={workspaces?.data?.map(wsp => ({
            label: wsp?.name,
            value: wsp?.id,
          }))}
          isMulti
          isLoading={isLoading}
        />
      </FormGroup>

      <FormGroup>
        <Label htmlFor="employeeIds">Nhân viên</Label>
        <Select<BaseOption, true>
          id="employeeIds"
          name="employeeIds"
          placeholder="Chọn nhân viên"
          onChange={value => setEmployeeIds(value?.map(item => item?.value as string))}
          value={employeeIds?.map(item => {
            const findEmp = employees?.data?.find(e => e?.id === item);
            return { label: (findEmp?.lastName || '') + ' ' + (findEmp?.firstName || ''), value: item };
          })}
          options={employees?.data?.map(e => ({
            label: (e?.lastName || '') + ' ' + (e?.firstName || ''),
            value: e?.id,
          }))}
          isMulti
          isLoading={loadingEmployees}
        />
      </FormGroup>

      <FormGroup>
        <Label htmlFor="date">Ngày</Label>
        <DatePicker
          value={selectedDate}
          ref={datePickerRef}
          onChange={onChangeSelectedDate}
          id="date"
          name="date"
          range
          arrow={false}
          closeExportCalendar={closeExportCalendar}
        />
      </FormGroup>
    </Modal>
  );
};
