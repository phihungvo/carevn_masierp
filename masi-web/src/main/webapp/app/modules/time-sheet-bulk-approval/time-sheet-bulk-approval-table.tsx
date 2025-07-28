import './time-sheet-bulk-approval.scss';
import Table from 'app/components/table/table';
import { getAllDatesInMonth } from 'app/shared/util/date-utils';
import dayjs from 'dayjs';
import React, { Dispatch, SetStateAction, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { ITimeKeepingMonthly, ITimeKeepingMonthlyParams } from 'app/shared/model/time-keeping-monthly.model';
import ExtraRows from './extra-rows';
import { calculateTotalHoursByType, calculateTotals, calculateTotalWorkByType, renderTimeSheetByType } from './utilts';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { useAppSelector } from 'app/config/store';
import { checkPermission } from 'app/shared/util/check-permission';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';

interface ITimeSheetBulkApprovalTable {
  setSelectedRecord: Dispatch<SetStateAction<string>>;
  setSelectedRows: Dispatch<SetStateAction<ITimeKeepingMonthly[]>>;
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
  selectedRowKeys: string[];
  filter: ITimeKeepingMonthlyParams;
  setFilter: React.Dispatch<React.SetStateAction<ITimeKeepingMonthlyParams>>;
  data: PaginationResponse<ITimeKeepingMonthly>;
  toggleDetail: () => void;
}

// TIME SHEET BULK APPROVAL TABLE
export const TimeSheetBulkApprovalTable = (props: ITimeSheetBulkApprovalTable) => {
  const { setSelectedRecord, setSelectedRows, setSelectedRowKeys, selectedRowKeys, filter, setFilter, data, toggleDetail } = props;

  const authorities = useAppSelector(state => state.authentication.account.authorities);

  const selectedDateMonth = filter?.month || new Date();

  const allDaysInMonth = getAllDatesInMonth(dayjs(selectedDateMonth).year(), dayjs(selectedDateMonth).month() + 1);

  const { control, setValue } = useForm();

  useEffect(() => {
    if (data) {
      data.data.forEach(item => {
        allDaysInMonth.forEach(day => {
          const findTimeKeeping = item?.timeKeepings?.find(timeKeeping => timeKeeping.date === day.fullDate);

          setValue(`${day.fullDate}_${item?.id}_${findTimeKeeping?.id}`, findTimeKeeping?.note);
        });
      });
    }
  }, [data]);

  const columns = renderTimeSheetByType(filter?.type, filter?.workspaceType, allDaysInMonth, control, setSelectedRecord, toggleDetail);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  const dataSource = data?.data?.reduce((acc, item, index) => {
    if (item?.employee?.firstName !== 'Tổng giờ' && item?.employee?.firstName !== 'Tổng công') {
      acc.push(item);
    }
    return acc;
  }, []);

  const dataTotalHours = calculateTotals(data, dataSource?.length, allDaysInMonth);
  const dataTotalWorks = calculateTotals(data, dataSource?.length + 1, allDaysInMonth);
  const dataTotalHoursRenderByType = calculateTotalHoursByType(
    data,
    dataTotalHours,
    filter?.type,
    filter?.workspaceType,
    dataSource?.length,
  );
  const dataTotalWorksRenderByType = calculateTotalWorkByType(
    data,
    dataTotalWorks,
    filter?.type,
    filter?.workspaceType,
    dataSource?.length + 1,
  );

  return (
    <Table<ITimeKeepingMonthly>
      rowKey="id"
      {...(checkPermission(authorities, PermissionResource.PERSONAL_MONTHLY_TIMESHEET, Action.REVIEW) && {
        rowSelection: {
          type: 'checkbox',
          onChange(selectedRowKeys, selectedRows) {
            setSelectedRowKeys(selectedRowKeys);
            setSelectedRows(selectedRows);
          },
          selectedRowKeys,
        },
      })}
      className="time-sheet-bulk-approval"
      columns={columns}
      dataSource={dataSource}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
      stickyHeader={false}
      extraRows={
        <>
          <ExtraRows columns={columns} data={['', <b>Tổng giờ</b>, '', '', ...dataTotalHoursRenderByType]} />
          <ExtraRows columns={columns} data={['', <b> Tổng công</b>, '', '', ...dataTotalWorksRenderByType]} />
        </>
      }
    />
  );
};
