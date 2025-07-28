import dayjs from 'dayjs';
import React, { useEffect, useRef, useState } from 'react';

import './time-sheet-bulk-approval.scss';
import Input from 'app/components/input/input';
import Button from 'app/components/button/button';
import ButtonIcon from 'app/components/button-icon/button-icon';
import timeSheetMapping from '../time-sheet/time-sheet-mapping';
import useTimeKeepingMonthly from 'app/hooks/use-time-keeping-monthly';
import { DATE_FORMAT } from 'app/constants/common';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { ITimeKeepingMonthlyParams } from 'app/shared/model/time-keeping-monthly.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import FilterDate from 'app/components/filter-date/filter-date';

const { timeKeepTypeMapping, workSpaceTypeMapping } = timeSheetMapping;

// TIME SHEET BULK APPROVAL CARD HEADER
interface IHeader {
  toggleModalAcceptBulkTimeSheet: () => void;
  toggleModalRejectBulkTimeSheet: () => void;
  disableBtnReject: boolean;
  disabledApprove: boolean;
  filter: ITimeKeepingMonthlyParams;
  setFilter: React.Dispatch<React.SetStateAction<ITimeKeepingMonthlyParams>>;
}

const { useGetTimeKeepingMonthlyExport } = useTimeKeepingMonthly;

export const TimeSheetBulkApprovalHeader = (props: IHeader) => {
  const { toggleModalAcceptBulkTimeSheet, toggleModalRejectBulkTimeSheet, disableBtnReject, disabledApprove, filter, setFilter } = props;

  // const [isOpen, setIsOpen] = useState(false);

  const datePickerFilterRef = useRef<DatePickerRef | null>(null);

  const [date, setDate] = useState<DateObject>();

  const { trigger, data } = useGetTimeKeepingMonthlyExport({
    ...filter,
  });

  const onExportTimeKeepingData = () => {
    trigger();
  };

  // const dropdownItems = Array(12)
  //   .fill('')
  //   .map((_, index) => ({
  //     label: `Tháng ${index + 1}`,
  //     onClick: () =>
  //       setFilter(prev => ({ ...prev, month: `${new Date().getFullYear()}-${index < 9 ? 0 : ''}${index + 1}-01`, page: DEFAULT_PAGE })),
  //   }));

  // const toggle = () => setIsOpen(!isOpen);

  useDownloadXlsx(data?.data, `time-sheet-bulk-approval-month-${dayjs(filter?.month).month() + 1}`, 'xlsx');

  useEffect(() => {
    if (date && date.isValid) {
      setFilter(prev => ({ ...prev, month: date?.format(DATE_FORMAT.YEAR_DATE) }));
    }
  }, [date]);

  const closeFilterCalendar = () => {
    setDate(null);
    setFilter(prev => ({
      ...prev,
      month: undefined,
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

  const filterReportDates = () => {
    setFilter(prev => ({
      ...prev,
      month: date?.format(DATE_FORMAT.YEAR_DATE),
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

  return (
    <div className="card-header-container">
      <div className="card-header-extra" />
      <div className="card-header-extra timesheet-actions">
        <Input
          id="workspaceType"
          name="workspaceType"
          type="select"
          className="time-sheet-type"
          onChange={e => setFilter(prev => ({ ...prev, workspaceType: e.target.value as WORKSPACE_TYPE }))}
        >
          <option value={WORKSPACE_TYPE.OFFICE}>{workSpaceTypeMapping(WORKSPACE_TYPE.OFFICE)}</option>
          <option value={WORKSPACE_TYPE.FACTORY}>{workSpaceTypeMapping(WORKSPACE_TYPE.FACTORY)}</option>
        </Input>
        <Input
          id="type"
          name="type"
          type="select"
          className="time-sheet-type"
          onChange={e => setFilter(prev => ({ ...prev, type: e.target.value as TIME_SHEET_TYPE }))}
        >
          <option value={TIME_SHEET_TYPE.HOUR}>{timeKeepTypeMapping(TIME_SHEET_TYPE.HOUR)}</option>
          <option value={TIME_SHEET_TYPE.OVERTIME}>{timeKeepTypeMapping(TIME_SHEET_TYPE.OVERTIME)}</option>
          <option value={TIME_SHEET_TYPE.LOADING_UNLOADING}>{timeKeepTypeMapping(TIME_SHEET_TYPE.LOADING_UNLOADING)}</option>
          <option value={TIME_SHEET_TYPE.MIXING_FLOUR}>{timeKeepTypeMapping(TIME_SHEET_TYPE.MIXING_FLOUR)}</option>
          <option value={TIME_SHEET_TYPE.DRIVER}>{timeKeepTypeMapping(TIME_SHEET_TYPE.DRIVER)}</option>
        </Input>
        {/* <Dropdown
          outline
          label={
            <>
              Tháng {filter.month ? dayjs(filter.month).month() + 1 : new Date().getMonth() + 1}{' '}
              <img src="content/images/vuesax/linear/sort.svg" alt="sort-month" />
            </>
          }
          items={dropdownItems}
          isOpen={isOpen}
          toggle={toggle}
        /> */}

        <FilterDate
          value={date}
          setSelectedDate={setDate}
          ref={datePickerFilterRef}
          onReset={closeFilterCalendar}
          onOk={filterReportDates}
          onlyMonthPicker
        />

          <AuthGuard permissionKey='TIME_SHEET_BULK_APPROVAL.EDIT'>
            <Button disabled={disabledApprove} color="primary" onClick={toggleModalAcceptBulkTimeSheet}>
              Xác nhận
            </Button>
            <Button disabled={disableBtnReject} color="primary" onClick={toggleModalRejectBulkTimeSheet}>
              Từ chối
            </Button>
          </AuthGuard>
          <AuthGuard permissionKey='TIME_SHEET_BULK_APPROVAL.EXPORT'>
            <ButtonIcon
              onClick={onExportTimeKeepingData}
              icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
            />
          </AuthGuard>
      </div>
    </div>
  );
};
