import { ColumnsTypes } from 'app/components/table/table.d';
import { IEmployeeChangeLog } from 'app/shared/model/employee.model';
import React from 'react';
import { useMemo } from 'react';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import Tooltip from 'app/components/tooltip/tooltip';
import ButtonIcon from 'app/components/button-icon/button-icon';

export const generateColumnsChangelog = (
  toggleDetail: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IEmployeeChangeLog> => {
  const handleViewDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IEmployeeChangeLog> = useMemo(() => {
    return [
      {
        title: 'Ngày thay đổi',
        dataIndex: 'changeDate',
        key: 'changeDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <Tooltip label="Xem chi tiết" placement='top' target={`detail-${record.id}`}>
            <ButtonIcon
              id={`detail-${record.id}`}
              onClick={() => handleViewDetail(record.id)}
              icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}
            />
          </Tooltip>
        ),
      },
    ];
  }, []);

  return columns;
};
