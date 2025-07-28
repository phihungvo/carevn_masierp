import { ColumnsTypes } from 'app/components/table/table.d';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import React from 'react';
import { IWarehouse } from 'app/shared/model/warehouse.model';

import warehouseMapping from "app/modules/warehouse/warehouse-mapping";

const { warehouseTypeMapName } = warehouseMapping;

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleDetail: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IWarehouse> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  console.log("warehouseTypePage", warehouseTypeMapName);

  const columns: ColumnsTypes<IWarehouse> = useMemo(() => {
    return [
      {
        title: 'Mã kho',
        key: 'code',
        dataIndex: 'code',
        render: (text, record) => (
          <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
            {text}
          </p>
        ),
      },
      {
        title: 'Tên kho',
        key: 'name',
        dataIndex: 'name',
      },
      {
        title: 'Loại kho',
        key: 'warehouseTypePage',
        dataIndex: 'warehouseTypePage',
        render: (text, record) => ( warehouseTypeMapName[record.warehouseTypePage] || '' ),
      },
      {
        title: 'Địa chỉ',
        key: 'address',
        dataIndex: 'address',
      },
      {
        title: 'Thao tác',
        key: 'action',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            toggleDetail={toggleDetail}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
