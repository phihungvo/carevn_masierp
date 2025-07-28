import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import InputNumberDecimal from 'app/components/input/input-number-decimal';
import { ColumnType, ColumnsTypes } from 'app/components/table/table.d';
import Tooltip from 'app/components/tooltip/tooltip';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { PaginationParams } from 'app/shared/model/pagination.model';
import { IPatchTimeKeepingDto, IPostTimeKeepingDto, TimeSheetGroupByEmployeeConveted } from 'app/shared/model/time-sheet.model';
import dayjs from 'dayjs';
import React, { useMemo } from 'react';

export const generateBulkColumns = (
  handleValidDecimal: (e: React.ChangeEvent<HTMLInputElement>, employeeId: string, iso: string) => void,
  dateRange: {
    iso: string;
    shortName: string;
    dayInMonth: number;
  }[],
  workSpaceType: WORKSPACE_TYPE,
  timeKeepingType: TIME_SHEET_TYPE,
  refreshData: () => void,
  hourWork: { [employeeId: string]: { [iso: string]: { timeSheetId: string; hour: string; locked: boolean } } },
  handleUpdateTimeKeeping: (body: { id: string; data: IPatchTimeKeepingDto }) => () => void,
  handleCreateTimeKeeping: (data: IPostTimeKeepingDto) => () => void,
  pagination: PaginationParams,
): ColumnsTypes<TimeSheetGroupByEmployeeConveted> => {
  const handleBlur = (isCreate: boolean, iso: string, hour: string, createFn: Function, updateFn: Function) => () => {
    if (dayjs(iso).day() === 6 && workSpaceType === WORKSPACE_TYPE.OFFICE && timeKeepingType === TIME_SHEET_TYPE.HOUR) {
      if (hour?.toLocaleLowerCase() == 'p') {
        refreshData();
        return;
      }
    }
    if (isCreate) {
      createFn();
      return;
    }
    updateFn();
  };

  const isAbleBlur = (hour: any) => {
    if (
      hour &&
      (hour?.toString()?.toLowerCase() === 'off' ||
        hour?.toString()?.toLowerCase() === 'p' ||
        hour?.toString()?.toLowerCase() === 'po' ||
        hour?.toString()?.toLowerCase() === 'nb' ||
        hour?.toString()?.toLowerCase() === 'v' ||
        hour?.toString()?.toLowerCase() === 'kp' ||
        hour?.toString()?.toLowerCase() === 'wfh')
    ) {
      return true;
    }
    if (timeKeepingType === TIME_SHEET_TYPE.LOADING_UNLOADING) {
      return hour !== null && hour !== undefined && Number(hour) >= 0;
    }
    if (timeKeepingType === TIME_SHEET_TYPE.MIXING_FLOUR) {
      return hour !== null && hour !== undefined && Number(hour) >= 0 && Number(hour) <= 80;
    }

    return hour !== null && hour !== undefined && Number(hour) >= 0 && Number(hour) <= 16;
  };

  const isInValidHours = (hour: any) => {
    if (timeKeepingType === TIME_SHEET_TYPE.LOADING_UNLOADING) {
      return Number(hour) < 0;
    }
    if (timeKeepingType === TIME_SHEET_TYPE.MIXING_FLOUR) {
      return Number(hour) < 0 && Number(hour) > 80;
    }
    return Number(hour) > 16
  };

  const columns: ColumnsTypes<TimeSheetGroupByEmployeeConveted> = useMemo(() => {
    return [
      {
        key: 'code',
        title: 'STT',
        dataIndex: 'code',
        width: 80,
        render: (_, __, index) =>
          `${index + 1 + pagination.page * pagination.size >= 10 ? '' : '0'}${index + 1 + pagination.page * pagination.size}`,
      },
      {
        key: 'code',
        title: 'Mã nhân viên',
        dataIndex: 'code',
        width: 150,
      },
      {
        key: 'name',
        title: 'Tên nhân viên',
        dataIndex: 'name',
        width: 250,
        render: (text, record) => (
          <Tooltip placement='right' label={(record?.lastName || '') + ' ' + (record?.firstName || '')} target={`name-${record.id}`}>
            <EllipsisParagraph text={(record?.lastName || '') + ' ' + (record?.firstName || '')} id={`name-${record.id}`} />
          </Tooltip>
        )
      },
      ...dateRange.map(
        day =>
          ({
            key: day.dayInMonth?.toString(),
            title: (
              <div className="header-time-check-date">
                <p className="header-time-date">{day.shortName}</p>
                <span>{day.dayInMonth}</span>
              </div>
            ),
            align: 'center',
            className: 'time-check-date',
            width: 50,
            render: (_, record) => (
              <input
                disabled={hourWork?.[record.id]?.[day.iso]?.locked ||
                  (dayjs(day.iso).day() === 0 && workSpaceType === WORKSPACE_TYPE.OFFICE && timeKeepingType === TIME_SHEET_TYPE.HOUR)
                }
                name={`${record.id}${day.iso}`}
                className={`input-number ${isInValidHours(Number(hourWork?.[record.id]?.[day.iso]?.hour)) ? 'invalid' : ''} ${hourWork?.[record.id]?.[day.iso]?.hour?.toLocaleLowerCase() === 'off'
                  ? 'off'
                  : hourWork?.[record.id]?.[day.iso]?.hour?.toLocaleLowerCase() === 'p'
                    ? 'p'
                    : hourWork?.[record.id]?.[day.iso]?.hour?.toLocaleLowerCase() === 'po'
                      ? 'po'
                      : hourWork?.[record.id]?.[day.iso]?.hour?.toLocaleLowerCase() === 'nb'
                        ? 'nb'
                        : hourWork?.[record.id]?.[day.iso]?.hour?.toLocaleLowerCase() === 'v'
                          ? 'v'
                          : hourWork?.[record.id]?.[day.iso]?.hour?.toLocaleLowerCase() === 'kp'
                            ? 'kp'
                            : hourWork?.[record.id]?.[day.iso]?.hour?.toLocaleLowerCase() === 'wfh'
                              ? 'wfh'
                              : ''
                  }`}
                value={hourWork?.[record.id]?.[day.iso]?.hour || ''}
                onChange={e => handleValidDecimal(e, record.id, day.iso)}
                {...(isAbleBlur(hourWork?.[record.id]?.[day.iso]?.hour) && {
                  onBlur: handleBlur(
                    !!!hourWork?.[record.id]?.[day.iso]?.timeSheetId, day.iso, hourWork?.[record.id]?.[day.iso]?.hour,
                    handleCreateTimeKeeping({
                      date: day.iso,
                      employeeId: record.id,
                      hoursWorked:
                        hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'wfh'
                          ? 8
                          : hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'off' ||
                            hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'p' ||
                            hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'po' ||
                            hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'nb' ||
                            hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'v' ||
                            hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'kp'
                            ? 0
                            : Number(hourWork?.[record.id]?.[day.iso]?.hour),
                      isDayOff: !!(hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'off'),
                      ...((hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'p' ||
                        hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'po' ||
                        hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'nb' ||
                        hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'v' ||
                        hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'kp' ||
                        hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'wfh' ||
                        hourWork?.[record.id]?.[day.iso]?.hour === '') && {
                        character: hourWork?.[record.id]?.[day.iso]?.hour === '' ? 'NULL' : hourWork?.[record.id]?.[day.iso]?.hour,
                      }),
                    }),
                    handleUpdateTimeKeeping({
                      id: hourWork?.[record.id]?.[day.iso]?.timeSheetId,
                      data: {
                        id: hourWork?.[record.id]?.[day.iso]?.timeSheetId,
                        hoursWorked:
                          hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'wfh'
                            ? 8
                            : hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'off' ||
                              hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'p' ||
                              hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'po' ||
                              hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'nb' ||
                              hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'kp' ||
                              hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'v'
                              ? 0
                              : Number(hourWork?.[record.id]?.[day.iso]?.hour),
                        date: dayjs().toISOString(),
                        isDayOff: !!(hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'off'),
                        ...((hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'p' ||
                          hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'po' ||
                          hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'nb' ||
                          hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'v' ||
                          hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'kp' ||
                          hourWork?.[record.id]?.[day.iso]?.hour?.toString()?.toLowerCase() === 'wfh' ||
                          hourWork?.[record.id]?.[day.iso]?.hour === '') && {
                          character: hourWork?.[record.id]?.[day.iso]?.hour === '' ? 'NULL' : hourWork?.[record.id]?.[day.iso]?.hour,
                        }),
                      },
                    }),
                  ),
                })}
              />
            ),
          }) as ColumnType<TimeSheetGroupByEmployeeConveted>,
      ),
      {
        key: '',
        title: '',
        render: (_, record) => <InputNumberDecimal hidden />,
      },
    ];
  }, [handleValidDecimal, dateRange]);

  return columns;
};
