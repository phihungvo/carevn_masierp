import Table from 'app/components/table/table';
import { DATE_FORMAT, DEFAULT_PAGE, DEFAULT_PAGE_SIZE, TIME_SHEET_BULK_DECIMAL_REGEX } from 'app/constants/common';
import useTimeSheet from 'app/hooks/use-time-sheet';
import {
  IPatchTimeKeepingDto,
  IPostTimeKeepingDto,
  ITimeSheet,
  ITimeSheetGroupByEmployeeByObject,
  TimeSheetGroupByEmployeeConveted,
} from 'app/shared/model/time-sheet.model';
import { getDateRange } from 'app/shared/util/helper';
import dayjs from 'dayjs';
import React, { useMemo, useState } from 'react';
import { DateObject } from 'react-multi-date-picker';
import { generateBulkColumns } from './generate-bulk-columns';
import './time-sheet.scss';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';

const { usePatchTimeKeepingMutation, usePostTimeKeepingMutation, useTimeKeepingGroupByEmployee } = useTimeSheet;

const today = new DateObject(new Date()).format(DATE_FORMAT.YEAR_DATE);
interface ITimeSheetBulkTableProps {
  selectedDate: DateObject[];
  timeCheckType: TIME_SHEET_TYPE;
  workSpaceTypes: WORKSPACE_TYPE;
}

export const TimeSheetBulkTable = (props: ITimeSheetBulkTableProps) => {
  const { selectedDate, timeCheckType, workSpaceTypes } = props;
  const [pagination, setPagination] = useState({ page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE });
  const [hourWork, setHourWork] = useState<{
    [employeeId: string]: { [iso: string]: { timeSheetId: string; hour: string; locked: boolean } };
  }>({});

  const date = {
    start: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE) || today,
    end: selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) || today,
  };
  const [invalidateCount, setInvalidateCount] = useState(0);

  const { data, isLoading } = useTimeKeepingGroupByEmployee(date.start, date.end, pagination, timeCheckType, workSpaceTypes);

  const { mutate: pathTimeKeeping } = usePatchTimeKeepingMutation();
  const { mutate: createTimeKeeping } = usePostTimeKeepingMutation();
  const convertedData = useMemo(() => {
    if (!data) return undefined;

    if (!data.data.length) return { arrConvertedRecords: [], objectConvertedRecords: {} };

    let arr: TimeSheetGroupByEmployeeConveted[] = [];
    let hourWorks: { [employeeId: string]: { [iso: string]: { timeSheetId: string; hour: string; locked: boolean } } } = {};

    let converted = data.data.reduce((acum: ITimeSheetGroupByEmployeeByObject, record) => {
      let tmp = {};
      let times = record.timeKeepings
        ? record.timeKeepings.reduce((acum: { [iso: string]: ITimeSheet }, time) => {
          let iso = dayjs(time.date).add(7, 'hour').toISOString();
          tmp = {
            ...tmp,
            [iso]: {
              timeSheetId: time.id,
              hour: time.isDayOff ? 'OFF' : time?.note,
              locked: time.locked,
            },
          };
          return {
            ...acum,
            [iso]: time,
          };
        }, {})
        : {};
      hourWorks = {
        ...hourWorks,
        [record.id]: { ...tmp },
      };
      arr.push({ ...record, times: times });
      return {
        ...acum,
        [record.id]: {
          ...record,
          times,
        },
      };
    }, {});
    setHourWork(hourWorks);

    return {
      arrConvertedRecords: arr,
      objectConvertedRecords: converted,
    };
  }, [data, invalidateCount]);

  const dateRange = useMemo(() => {
    return getDateRange(dayjs(date.start).add(7, 'hour'), dayjs(date.end).add(7, 'hour'));
  }, [selectedDate]);

  const handleValidDecimal = (e: React.ChangeEvent<HTMLInputElement>, employeeId: string, iso: string) => {
    const decimalRegex = TIME_SHEET_BULK_DECIMAL_REGEX;
    let value = e.target.value;
    if ((value && decimalRegex.test(value)) || value === '') {
      if (workSpaceTypes === WORKSPACE_TYPE.OFFICE && timeCheckType === TIME_SHEET_TYPE.HOUR) {
        // thứ 7 chỉ được chấm công 4h và chủ nhật ko được chấm công
        // thứ 7 không được p (vì p là nghỉ phép full day)
        if (dayjs(iso).day() === 6) {
          if (Number(value) && Number(value) >= 4) {
            return;
          }
        }
      }
      if(Number(value) && Number(value) > 24 && timeCheckType === TIME_SHEET_TYPE.HOUR) {
        return;
      }
      setHourWork(pre => ({
        ...pre,
        [employeeId]: {
          ...pre[employeeId],
          [iso]: {
            ...pre[employeeId][iso],
            hour: value,
          },
        },
      }));
    }
  };

  const handleUpdateTimeKeeping = (body: { id: string; data: IPatchTimeKeepingDto }) => () => {
    pathTimeKeeping({ id: body.id, data: { ...body.data, type: timeCheckType } })
  };

  const handleCreateTimeKeeping = (data: IPostTimeKeepingDto) => () => {
    createTimeKeeping({
      ...data,
      type: timeCheckType,
    });
  };

  const columns = generateBulkColumns(
    handleValidDecimal,
    dateRange,
    workSpaceTypes,
    timeCheckType,
    () => setInvalidateCount(pre => pre + 1),
    hourWork,
    handleUpdateTimeKeeping,
    handleCreateTimeKeeping,
    pagination,
  );

  const totalCount = data?.totalRecord || 0;
  const { page, size } = pagination;

  return (
    <Table<TimeSheetGroupByEmployeeConveted>
      className="time-sheet-bulk"
      loading={isLoading}
      columns={columns}
      dataSource={convertedData?.arrConvertedRecords || []}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setPagination({ ...pagination, page, size }),
      }}
      stickyHeader={false}
      showIndex={false}
    />
  );
};
