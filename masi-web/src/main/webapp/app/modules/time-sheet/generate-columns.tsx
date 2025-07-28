import ButtonIcon from 'app/components/button-icon/button-icon';
import { ColumnsTypes } from 'app/components/table/table.d';
import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import React, { useMemo } from 'react';
import timeSheetMapping from './time-sheet-mapping';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import Badge from 'app/components/badge/badge';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { Typography } from 'app/components/typography/typography';
import { IConfig } from 'app/shared/model/config.model';
import ActionsDropdown from './actions-dropdown';

const { timeKeepStatusMapping, hoursWorkedMapping } = timeSheetMapping;

export const generateColumns = (handleSelectUpdateRecord: (record: string) => void, config: IConfig): ColumnsTypes<ITimeSheet> => {
  const columns: ColumnsTypes<ITimeSheet> = useMemo(() => {
    return [
      { key: 'code', title: 'Mã nhân viên', width: 150, render: (_, record) => record?.employee?.employeeProfile?.employeeCode },
      {
        key: 'name',
        title: 'Tên nhân viên',
        width: 250,
        render: (text, record) => (
          <Tooltip placement='right' label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`name-${record.id}`}>
            <EllipsisParagraph text={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} id={`name-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'totalCompletionPercent',
        title: 'Thực tế công',
        dataIndex: 'totalCompletionPercent',
      },
      {
        title: 'Tổng thời gian chấm công',
        key: 'hoursWorked',
        dataIndex: 'hoursWorked',
        render: (_, record) => {
          // lấy tổng theo format giờ.
          const hour = dayjs.duration(dayjs(record?.lastCheckIn).diff(dayjs(record?.firstCheckIn))).format('HH');
          // lấy tổng theo format phút.
          const sencond = dayjs.duration(dayjs(record?.lastCheckIn).diff(dayjs(record?.firstCheckIn))).format('mm');
          //                                                         Đổi phút sang giờ     : Lấy 2 số sau dot.  
          const { label, color } = hoursWorkedMapping(Number(hour) + Number(parseFloat(String(Number(sencond) / 60)).toFixed(1)), config)
          return <p>{record?.lastCheckIn && record?.firstCheckIn &&
            <Typography level={'text'} color={color} style={{ fontWeight: '600' }}>
              {label}
            </Typography>}</p>
        },
      },
      {
        key: 'checkinDate',
        title: 'Giờ vào',
        render: (_, record) => (record.firstCheckIn ? dayjs(record.firstCheckIn).format(DATE_FORMAT.TIME_ONLY) : ''),
      },
      {
        key: 'checkoutDate',
        title: 'Giờ ra',
        render: (_, record) => (record.lastCheckIn ? dayjs(record.lastCheckIn).format(DATE_FORMAT.TIME_ONLY) : ''),
      },
      { key: 'date', title: 'Ngày', dataIndex: 'date', render: (_, record) => dayjs(record.date).format(DATE_FORMAT.DATE) },
      {
        key: 'status',
        title: 'Trạng thái',
        dataIndex: 'status',
        render: (_, record) => {
          const { label, color } = timeKeepStatusMapping(record?.locked);
          if ((record?.hoursWorked || record?.firstCheckIn) && !record?.locked) {
            return <Badge color="success">Đã chấm công</Badge>;
          }

          return label && <Badge color={color}>{label}</Badge>;
        },
      },
      {
        key: 'actions',
        title: 'Thao tác',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => {
          if (!record?.locked) {
            return (
              <>
                <ActionsDropdown
                  handleShowModalHistory={handleSelectUpdateRecord}
                  record={record}
                />
              </>
            );
          }

          return <ButtonIcon disabled icon={<img className="pointer" src="content/images/vuesax/linear/edit.svg" alt="edit" />} />;
        },
      },
    ];
  }, [timeKeepStatusMapping, config]);

  return columns;
};
