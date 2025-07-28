import './time-sheet.scss';
import ButtonIcon from 'app/components/button-icon/button-icon';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import Input from 'app/components/input/input';
import { useAppSelector } from 'app/config/store';
import { DATE_FORMAT, weekDays } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDownload } from 'app/hooks/use-download';
import useTimeSheet from 'app/hooks/use-time-sheet';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { getStartAndEndOfWeek } from 'app/shared/util/date-utils';
import dayjs from 'dayjs';
import 'dayjs/plugin/utc';
import React, { useEffect, useRef, useState } from 'react';
import DatePicker, { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { useLocation, useNavigate } from 'react-router';
import { CardSubtitle, DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';
import { RefreshIcon } from './icons';
import { TimeCheckType } from './time-sheet';
import timeSheetMapping from './time-sheet-mapping';
import FilterDateMulti from 'app/components/filter-date-multi/filter-date-multi';


const { timeKeepTypeMapping, workSpaceTypeMapping } = timeSheetMapping;

const {
  usePostTimeKeepingRecordMutation,
  useTimeKeepingExportByEmployeeWithDateLazyQuery,
  useTimeKeepingExportByEmployeeWithDateMonthLazyQuery,
  useTimeKeepingByEmployeeWithDateQuery,
  useRefreshTimeKeepingMachineData
} = useTimeSheet;

// TIME SHEET HEADER COMPONENTS
interface IHeader {
  toggleTimeSheetModal: () => void;
  selectedDate: DateObject[];
  setSelectedDate: (value: DateObject[]) => void;
  setTimeCheckType: (value: TIME_SHEET_TYPE) => void;
  timeCheckType: TIME_SHEET_TYPE;
  setInOrOut: (value: TimeCheckType) => void;
  workSpaceTypes: WORKSPACE_TYPE;
  setWorkSpaceTypes: (value: WORKSPACE_TYPE) => void;
}

export const TimeSheetHeader = (props: IHeader) => {
  const account = useAppSelector(state => state.authentication.account);

  const {
    toggleTimeSheetModal,
    setSelectedDate,
    setTimeCheckType,
    selectedDate,
    timeCheckType,
    setInOrOut,
    workSpaceTypes,
    setWorkSpaceTypes,
  } = props;

  const { pathname } = useLocation();
  const navigate = useNavigate();
  const { startOfWeek, endOfWeek } = getStartAndEndOfWeek(new Date());
  const isInTimeSheetPage = pathname === PATH.TIME_SHEET;
  const { mutate: refreshMachine } = useRefreshTimeKeepingMachineData();

  const [date, setDate] = useState<DateObject[]>([new DateObject(), new DateObject()]);
  const [exportDate, setExportDate] = useState<DateObject[]>([new DateObject(), new DateObject()]);

  const datePickerFilterRef = useRef<DatePickerRef | null>(null);
  const datePickerExportRef = useRef<DatePickerRef | null>(null);

  const disableCheckIN =
    timeCheckType === TIME_SHEET_TYPE.MIXING_FLOUR ||
    timeCheckType === TIME_SHEET_TYPE.LOADING_UNLOADING ||
    timeCheckType === TIME_SHEET_TYPE.OVERTIME;
  const disableCheckOut =
    timeCheckType === TIME_SHEET_TYPE.MIXING_FLOUR ||
    timeCheckType === TIME_SHEET_TYPE.LOADING_UNLOADING ||
    timeCheckType === TIME_SHEET_TYPE.OVERTIME;

  const startExportDate = exportDate[0]?.format(DATE_FORMAT.YEAR_DATE);
  const endExportDate = exportDate[1] ? exportDate[1]?.format(DATE_FORMAT.YEAR_DATE) : exportDate[0]?.format(DATE_FORMAT.YEAR_DATE);

  const { data: employeeTimeKeep } = useTimeKeepingByEmployeeWithDateQuery(account?.id, dayjs()?.format(DATE_FORMAT.YEAR_DATE));
  const { trigger, data } = useTimeKeepingExportByEmployeeWithDateLazyQuery(startExportDate, endExportDate);
  const { trigger: exportBulk, data: dataBulk } = useTimeKeepingExportByEmployeeWithDateMonthLazyQuery(startExportDate, endExportDate);
  const { mutate } = usePostTimeKeepingRecordMutation(toggleTimeSheetModal);

  const closeFilterCalendar = () => {
    setDate([]);
    setSelectedDate([]);
    datePickerFilterRef.current?.closeCalendar();
  };

  const filterTimeKeep = () => {
    setSelectedDate(date);
    datePickerFilterRef.current?.closeCalendar();
  };

  const closeExportCalendar = () => {
    datePickerExportRef.current?.closeCalendar();
  };

  const onChangeExportDate = (value: DateObject[]) => {
    setExportDate(value);
  };

  // Tạo Bản Ghi Chấm Công Chi Tiết
  const onPostTimeKeepingRecord = (inOrOut: TimeCheckType) => {
    setInOrOut(inOrOut);
    mutate({
      'check-in': new Date().toISOString(),
      employee: account?.id,
      type: timeCheckType,
      workSpaceTypes,
    });
  };

  // Xuất Chấm Công Theo Nhân Viên Và Ngày
  const onExportTimeKeepingData = () => {
    isInTimeSheetPage ? trigger() : exportBulk();
    closeExportCalendar();
  };

  useEffect(() => {
    const listNodes = document.querySelectorAll('.rmdp-day');

    if (date.length === 0) {
      listNodes.forEach(node => {
        node.classList.remove('rmdp-range');
      });
    }
  }, [date]);

  isInTimeSheetPage
    ? useDownload(
      data?.data,
      `time-keeping${timeCheckType && '-'}${timeCheckType ?? ''}-${startExportDate}${startExportDate && endExportDate && '-'
      }${endExportDate}`,
      'xlsx',
    )
    : useDownload(
      dataBulk?.data,
      `time-keeping${timeCheckType && '-'}${timeCheckType ?? ''}-${startExportDate}${startExportDate && endExportDate && '-'
      }${endExportDate}`,
      'xlsx',
    );

  return (
    <Flex flexWrap="wrap" justify="space-between" style={{ width: '100%' }}>
      <CardSubtitle className="card-header-subtitle">
        Tuần: {selectedDate?.[0]?.format(DATE_FORMAT.DATE) || startOfWeek} -{' '}
        {selectedDate?.[1]?.format(DATE_FORMAT.DATE) || endOfWeek}
      </CardSubtitle>
      <div className="card-header-extra timesheet-actions">
        <Input
          id="type"
          name="type"
          type="select"
          className="time-sheet-type"
          onChange={e => setTimeCheckType(e.target.value as TIME_SHEET_TYPE)}
        >
          <option value={TIME_SHEET_TYPE.HOUR}>
            {timeKeepTypeMapping(TIME_SHEET_TYPE.HOUR)}
          </option>
          <option value={TIME_SHEET_TYPE.OVERTIME}>
            {timeKeepTypeMapping(TIME_SHEET_TYPE.OVERTIME)}
          </option>
          <option value={TIME_SHEET_TYPE.LOADING_UNLOADING}>
            {timeKeepTypeMapping(TIME_SHEET_TYPE.LOADING_UNLOADING)}
          </option>
          <option value={TIME_SHEET_TYPE.MIXING_FLOUR}>
            {timeKeepTypeMapping(TIME_SHEET_TYPE.MIXING_FLOUR)}
          </option>
          <option value={TIME_SHEET_TYPE.DRIVER}>
            {timeKeepTypeMapping(TIME_SHEET_TYPE.DRIVER)}
          </option>
        </Input>

        <Input
          id="workspace"
          name="workspace"
          type="select"
          className="time-sheet-type"
          onChange={e => setWorkSpaceTypes(e.target.value as WORKSPACE_TYPE)}
        >
          <option selected value={WORKSPACE_TYPE.OFFICE}>
            {workSpaceTypeMapping(WORKSPACE_TYPE.OFFICE)}
          </option>
          <option value={WORKSPACE_TYPE.FACTORY}>
            {workSpaceTypeMapping(WORKSPACE_TYPE.FACTORY)}
          </option>
        </Input>

        <FilterDateMulti
          value={date}
          setSelectedDate={setDate}
          ref={datePickerFilterRef}
          onReset={closeFilterCalendar}
          onOk={filterTimeKeep}
        />
        <UncontrolledDropdown
          className="d-xl-none d-block"
          disabled={employeeTimeKeep?.data?.locked}
        >
          <DropdownToggle className="bg-primary">Chấm công</DropdownToggle>

          <DropdownMenu>
            <AuthGuard permissionKey="TIME_SHEET.CREATE">
              <DropdownItem
                onClick={() => onPostTimeKeepingRecord(TimeCheckType.CHECK_IN)}
                disabled={employeeTimeKeep?.data?.locked}
              >
                Chấm công vào
              </DropdownItem>

              <DropdownItem
                onClick={() => onPostTimeKeepingRecord(TimeCheckType.CHECK_OUT)}
                disabled={employeeTimeKeep?.data?.locked}
              >
                Chấm công ra
              </DropdownItem>
            </AuthGuard>

            {isInTimeSheetPage ? (
              <AuthGuard permissionKey="TIME_SHEET.CREATE">
                <DropdownItem onClick={() => navigate(PATH.TIME_SHEET_BULK)}>
                  Chấm công đồng loạt
                </DropdownItem>
              </AuthGuard>
            ) : (
              <DropdownItem onClick={() => navigate(PATH.TIME_SHEET)}>
                Bảng chấm công
              </DropdownItem>
            )}
          </DropdownMenu>
        </UncontrolledDropdown>

        <div className="d-xl-flex d-none gap-2">
          <AuthGuard permissionKey='TIME_SHEET.CREATE'>
            <Button
              onClick={() => onPostTimeKeepingRecord(TimeCheckType.CHECK_IN)}
              color="primary"
              disabled={disableCheckIN}
            >
              Chấm công vào
            </Button>
  
            <Button
              onClick={() => onPostTimeKeepingRecord(TimeCheckType.CHECK_OUT)}
              color="primary"
              disabled={disableCheckOut}
            >
              Chấm công ra
            </Button>
          </AuthGuard>
          {isInTimeSheetPage ? (
            <AuthGuard permissionKey='TIME_SHEET.CREATE'>
              <Button
                onClick={() => navigate(PATH.TIME_SHEET_BULK)}
                color="primary"
              >
                Chấm công đồng loạt
              </Button>
            </AuthGuard>
          ) : (
            <Button onClick={() => navigate(PATH.TIME_SHEET)} color="primary">
              Bảng chấm công
            </Button>
          )}
        </div>
        <div>
          <ButtonIcon onClick={_ => refreshMachine()} icon={<RefreshIcon />} />
        </div>
        <div className="export-timesheet">
          <ButtonIcon
            icon={
              <img
                className="document-download"
                src="content/images/vuesax/linear/document-download.svg"
                alt="download"
              />
            }
          />
          <DatePicker
            value={exportDate}
            weekDays={weekDays}
            ref={datePickerExportRef}
            onChange={onChangeExportDate}
            range
            arrow={false}
          >
            <Flex
              align="center"
              justify="center"
              gap={16}
              style={{ paddingTop: 36, paddingBottom: 16 }}
            >
              <Button outline type="button" onClick={closeExportCalendar}>
                Huỷ
              </Button>
              <Button
                color="primary"
                type="button"
                onClick={onExportTimeKeepingData}
              >
                Xuất BCC
              </Button>
            </Flex>
          </DatePicker>
        </div>
      </div>
    </Flex>
  );
};
