import dayjs from 'dayjs';
import React, { useState } from 'react';

import './time-sheet.scss';
import Table from 'app/components/table/table';
import useTimeSheet from 'app/hooks/use-time-sheet';
import { DateObject } from 'react-multi-date-picker';
import { generateColumns } from './generate-columns';
import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { DATE_FORMAT, DEFAULT_MAX_HOUR_WORK, DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import useConfig from 'app/hooks/use-config'

const { useTimeKeepingByRangeDatePeriodQuery } = useTimeSheet;
const { useGetConfig } = useConfig;

// TIME CHECK COMPONENTS
interface ITimeCheckTable {
  toggleUpdateTimeCheck: () => void;
  selectedDate: DateObject[];
  setSelectedRecord: (record: string) => void;
  timeCheckType: TIME_SHEET_TYPE;
  workSpaceTypes: WORKSPACE_TYPE;
}

export const TimeSheetTable = (props: ITimeCheckTable) => {
  const { toggleUpdateTimeCheck, selectedDate, setSelectedRecord, timeCheckType, workSpaceTypes } = props;

  const [pagination, setPagination] = useState({ page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE });

  const startDate = selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE) || dayjs().format(DATE_FORMAT.YEAR_DATE);
  const endDate = selectedDate?.[1]?.format(DATE_FORMAT.YEAR_DATE) || dayjs().format(DATE_FORMAT.YEAR_DATE);
  const { data: config } = useGetConfig()

  // Lấy Bản Ghi Chấm Công Theo Trong Giai Đoạn Ngày
  const { data, isLoading } = useTimeKeepingByRangeDatePeriodQuery(startDate, endDate, pagination, timeCheckType, workSpaceTypes);
  // const { data } = useTimeKeepingByEmployeeWithPeriodQuery(
  //   '84b36e89-155e-4c4c-9b61-85cbc8f73589',
  //   selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE) || dayjs().format(DATE_FORMAT.YEAR_DATE),
  //   selectedDate?.[1]?.format(DATE_FORMAT.YEAR_DATE) || dayjs().format(DATE_FORMAT.YEAR_DATE),
  // );

  const handleSelectUpdateRecord = (record: string) => {
    setSelectedRecord(record);
    toggleUpdateTimeCheck();
  };

  const columns = generateColumns(handleSelectUpdateRecord, config);

  const dataSource = data?.data?.length ? data?.data : [];
  const totalCount = data?.totalRecord || 0;
  const { page, size } = pagination;

  return (
    <Table<ITimeSheet>
      responsive
      loading={isLoading}
      columns={columns}
      dataSource={dataSource}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setPagination({ ...pagination, page, size }),
      }}
    />
  );
};
