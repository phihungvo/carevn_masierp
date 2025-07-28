import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import { DATE_FORMAT } from 'app/constants/common';
import { IProductionProcess } from 'app/shared/model/production-process.model';
import dayjs from 'dayjs';
import React from 'react';
import ActionsDropdown from './actions-dropdown';
import productionProcessMapping from './production-process-mapping';

const { mapProductionProcessStatusColor, mapProductionProcessStatusText } = productionProcessMapping;

export const generateColumns = (
  toggleModalUpdate: () => void,
  toggleModalDelete: () => void,
  toggleModalStart: () => void,
  toggleModalStop: () => void,
  toggleModalComplete: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IProductionProcess> => {
  const columns: ColumnsTypes<IProductionProcess> = [
    {
      key: 'fromDate',
      title: 'Ngày sản xuất',
      dataIndex: 'fromDate',
      render: (value: string) => (value ? dayjs(value).format(DATE_FORMAT.DATE) : ''),
    },
    {
      key: 'status',
      title: 'Trạng thái',
      dataIndex: 'status',
      render: text => text && <Badge color={mapProductionProcessStatusColor(text)}>{mapProductionProcessStatusText(text)}</Badge>,
    },
    {
      key: 'actions',
      title: 'Thao tác',
      width: 106,
      render: (_, record) => (
        <ActionsDropdown
          record={record}
          id={record.id}
          status={record.status}
          workItemId={record.workItemId}
          toggleUpdateReq={toggleModalUpdate}
          toggleModalStart={toggleModalStart}
          toggleModalStop={toggleModalStop}
          toggleModalComplete={toggleModalComplete}
          toggleModalDelete={toggleModalDelete}
          setSelectedRecord={setSelectedRecord}
        />
      ),
    },
  ];

  return columns;
};
