import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import Select from 'app/components/select/select';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useWorkspace from 'app/hooks/use-workspace';
import { TIME_KEEPING_EXPLANATION_REASON } from 'app/shared/model/enumerations/time-keeping-explanation.model';
import { BaseOption } from 'app/shared/model/pagination.model';
import { ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';
import React, { useState } from 'react';
import { Col, FormGroup, Input, Label, Row } from 'reactstrap';

const { useGetWorkspacesQuery } = useWorkspace;
const { useGetEmployeesQuery } = useEmployee;

interface ITimeSheetViolationFilterModal {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ITimeKeepingViolationsParams>>;
}

export const ModalTimeSheetViolationFilter = (props: ITimeSheetViolationFilterModal) => {
  const { isOpen, toggle, setFilter } = props;
  const [selectedDate, setSelectedDate] = useState([]);
  const [selectedType, setSelectedType] = useState([]);
  const [employeeIds, setEmployeeIds] = useState<string[]>([]);
  const [workspaceIds, setWorkspaceIds] = useState<string[]>([]);

  // const datePickerRef = useRef<DatePickerRef | null>(null);

  // const onChangeSelectedDate = (value: DateObject[]) => {
  //   setSelectedDate(value);
  // };

  // const closeExportCalendar = () => {
  //   setSelectedDate([]);
  //   datePickerRef.current?.closeCalendar();
  // };

  const { data: workspaces, isLoading } = useGetWorkspacesQuery();
  const { data: employees, isLoading: loadingEmployees } = useGetEmployeesQuery();

  const onChangeType = (e: React.ChangeEvent<HTMLInputElement>, type: TIME_KEEPING_EXPLANATION_REASON) => {
    if (e.target.checked && e.target.value === type) {
      setSelectedType(prev => [...prev, type]);
    } else {
      setSelectedType(prev => prev.filter(item => item !== type));
    }
  };

  const handleFilter = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      fromDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      toDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      type: selectedType,
      employeeIds,
      workspaceIds,
    }));
    toggle();
  };

  const handleCancelFilter = () => {
    setSelectedDate([]);
    setSelectedType([]);
    setEmployeeIds([]);
    setWorkspaceIds([]);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      //fromDate: '',
      //toDate: '',
      type: [],
      employeeIds: [],
      workspaceIds: [],
    }));
    toggle();
  };

  //const handleDatePickerClose = useCallback(() => datePickerRef.current.closeCalendar(), [datePickerRef]);
  //useOnClickOutside(datePickerRef, handleDatePickerClose);
  //
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-timesheet-violation"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={handleFilter}
      onCancel={handleCancelFilter}
    >
      <Flex direction="column" gap={24}>
        { /*<DatePicker
          label="Ngày"
          weekDays={weekDays}
          value={selectedDate}
          ref={datePickerRef}
          onChange={onChangeSelectedDate}
          range
          arrow={false}
          closeExportCalendar={closeExportCalendar}
        />*/}
        <Row>
          <Label>Lý do vi phạm:</Label>
          <Col md={6}>
            <FormGroup check>
              <Label check for="status1">
                Quên chấm công
              </Label>
              <Input
                checked={selectedType.includes(TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT)}
                id="status1"
                name="status"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT}
                onChange={e => onChangeType(e, TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT)}
              />
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup check>
              <Label check for="status2">
                Chấm công không đúng giờ quy định
              </Label>
              <Input
                checked={selectedType.includes(TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME)}
                id="status2"
                name="status"
                type="checkbox"
                value={TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME}
                onChange={e => onChangeType(e, TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME)}
              />
            </FormGroup>
          </Col>
        </Row>

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
      </Flex>
    </Modal>
  );
};
