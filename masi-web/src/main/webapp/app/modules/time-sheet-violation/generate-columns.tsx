import { ColumnsTypes } from 'app/components/table/table.d';
import { DATE_FORMAT } from 'app/constants/common';
import { ITimeKeepingViolation } from 'app/shared/model/time-keeping-violation.model';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import timeSheetViolationMapping from './time-sheet-violation-mapping';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import React from 'react';
import Badge from 'app/components/badge/badge';

const { mapTimeKeepingViolationTypeToText } = timeSheetViolationMapping;

export const generateColumns = (): ColumnsTypes<ITimeKeepingViolation> => {
  const columns: ColumnsTypes<ITimeKeepingViolation> = useMemo(() => {
    return [
      {
        key: 'employee',
        title: 'Tên NV',
        dataIndex: 'employee',
        render: (text, record) => (
          <Tooltip label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`name-${record.id}`}>
            <EllipsisParagraph text={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} width={180} id={`name-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'date',
        title: 'Ngày',
        dataIndex: 'date',
        render: (_, record) => (record.timeKeeping?.date ? dayjs(record.timeKeeping?.date).format(DATE_FORMAT.DATE) : ''),
      },
      {
        key: 'checkin',
        title: 'Chấm công vào',
        dataIndex: 'checkin',
        render: (_, record) =>
          record.timeKeeping?.firstCheckIn ? dayjs(record.timeKeeping.firstCheckIn).format(DATE_FORMAT.TIME_ONLY) : '',
      },
      {
        key: 'checkout',
        title: 'Chấm công ra',
        dataIndex: 'checkout',
        render: (_, record) => (record.timeKeeping?.lastCheckIn ? dayjs(record.timeKeeping.lastCheckIn).format(DATE_FORMAT.TIME_ONLY) : ''),
      },
      {
        key: 'reason',
        title: 'Lý do vi phạm',
        dataIndex: 'reason',
        render: (text, record) => (
          <Tooltip label={mapTimeKeepingViolationTypeToText(record.type)} target={`reason-${record.id}`}>
            <EllipsisParagraph text={mapTimeKeepingViolationTypeToText(record.type)} width={150} id={`reason-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Giải trình',
        key: 'explanationId',
        dataIndex: 'explanationId',
        render: (id, record) => {
          return id ? <Badge color={'success'}>Đã giải trình</Badge> : <Badge color='error'>Chưa giải trình</Badge>;
        }
      }
    ];
  }, []);

  return columns;
};
