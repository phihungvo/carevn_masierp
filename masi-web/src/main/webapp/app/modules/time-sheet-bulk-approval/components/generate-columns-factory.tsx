import Badge from 'app/components/badge/badge';
import FormInput from 'app/components/form/form-input';
import { ColumnType, ColumnsTypes } from 'app/components/table/table.d';
import { ITimeKeepingMonthly } from 'app/shared/model/time-keeping-monthly.model';
import React from 'react';
import { useMemo } from 'react';
import { Control, FieldValues } from 'react-hook-form';
import timeSheetBulkApprovalMapping from '../time-sheet-bulk-approval-mapping';
import { roundedHours } from '../utilts';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { mapTimeSheetMonthlyStatusColor, mapTimeSheetMonthlyStatusText } = timeSheetBulkApprovalMapping;

export const generateColumnsFactory = (
  allDaysInMonth: { dayOfWeek: string; dateNumber: number; fullDate: string }[],
  control: Control<FieldValues, any>,
  setSelectedRecord: (id: string) => void,
  toggleDetail: () => void,
): ColumnsTypes<ITimeKeepingMonthly> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<ITimeKeepingMonthly> = useMemo(() => {
    return [
      {
        key: 'code',
        title: 'Mã NV',
        dataIndex: 'code',
        width: 150,
        render: (_, record) => record?.employee?.employeeProfile?.employeeCode,
      },
      {
        key: 'name',
        title: 'Tên nhân viên',
        dataIndex: 'name',
        width: 250,
        render: (_, record) =>
          record?.review?.id ? (
            <Tooltip label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`name-${record.id}`}>
              <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
                <EllipsisParagraph text={
                  (record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')
                } id={`name-${record.id}`} />
              </p>
            </Tooltip>
          ) : (
            <Tooltip label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`name-${record.id}`}>
              <EllipsisParagraph text={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} id={`name-${record.id}`} />
            </Tooltip>
          ),
      },
      {
        key: 'title',
        title: 'Vị trí làm việc',
        dataIndex: 'title',
        width: 200,
        render: (_, record) => record?.employee?.employeeProfile?.role,
      },
      ...allDaysInMonth.map(
        (day, index) =>
          ({
            key: `T${index + 1}`,
            align: 'center',
            title: (
              <div>
                <p className="header-time-date">{day.dayOfWeek}</p>
                <span>{day.dateNumber}</span>
              </div>
            ),
            render: (_, record) => {
              const findTimeKeeping = record?.timeKeepings?.find(item => item?.date === day.fullDate);
              return (
                <FormInput
                  control={control}
                  disabled
                  className="input-time-check-bulk"
                  min={0}
                  name={`${day.fullDate.toString()}_${record?.id}` + (findTimeKeeping?.id ? `_${findTimeKeeping?.id}` : '')}
                />
              );
            },
          }) as ColumnType<ITimeKeepingMonthly>,
      ),
      {
        key: 'shiftHours',
        width: 100,
        dataIndex: 'shiftHours',
        title: (
          <div className="shift-hours-headers">
            <p className="header-time-date">Giờ ca</p>
            <span>Ngày</span>
          </div>
        ),
        render: text => roundedHours(text, 1),
      },
      {
        key: 'holiday300',
        title: <div className="shift-hours-headers">Lễ 300%</div>,
        dataIndex: 'holiday300',
        width: 120,
      },
      {
        key: 'offDay',
        title: <div className="shift-hours-headers">Ngày off hưởng nguyên lương</div>,
        dataIndex: 'offDay',
        width: 140,
      },
      {
        key: 'annualLeave',
        title: <div className="shift-hours-headers">Phép năm</div>,
        dataIndex: 'annualLeave',
      },
      {
        key: 'totalHoursAtFactory',
        title: <div className="shift-hours-headers">Tổng giờ tại NM</div>,
        dataIndex: 'totalHoursAtFactory',
        width: 140,
        render: text => roundedHours(text, 1),
      },
      {
        key: 'totalWorkAtFactory',
        title: <div className="shift-hours-headers">Tổng công tại NM</div>,
        dataIndex: 'totalWorkAtFactory',
        width: 140,
        render: text => roundedHours(text, 2),
      },
      {
        key: 'totalWork',
        title: <div className="shift-hours-headers">Tổng công</div>,
        dataIndex: 'totalWork',
        render: text => roundedHours(text, 2),
      },
      {
        key: 'offDayInMonth',
        title: <div className="shift-hours-headers">Ngày off trong tháng</div>,
        dataIndex: 'offDayInMonth',
        width: 120,
      },
      {
        key: 'reason',
        title: <div className="shift-hours-headers">Lý do từ chối</div>,
        width: 120,
        render: (_, record) =>
          <Tooltip label={record?.review?.note} target={`reason-${record.id}`}>
            <EllipsisParagraph text={record?.review?.note} width={150} id={`reason-${record.id}`} />
          </Tooltip>
      },
      {
        key: 'status',
        title: <div className="shift-hours-headers">Trạng thái</div>,
        dataIndex: 'status',
        width: 120,
        render: text => <Badge color={mapTimeSheetMonthlyStatusColor(text)}>{mapTimeSheetMonthlyStatusText(text)}</Badge>,
      },
    ];
  }, [allDaysInMonth]);

  return columns;
};
