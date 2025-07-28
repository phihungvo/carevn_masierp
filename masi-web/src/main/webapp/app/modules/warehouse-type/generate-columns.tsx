import { ColumnsTypes } from 'app/components/table/table.d';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import React from 'react';
import { IWarehouseType } from 'app/shared/model/warehouse-type.model';
import { isHasPermission } from 'app/constants/common';
import { useAppSelector } from 'app/config/store';

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleDetail: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IWarehouseType> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: ColumnsTypes<IWarehouseType> = useMemo(() => {
    return [
      {
        title: 'Tên loại kho',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <p className="attachment-link" onClick={() => {
            // if (!isHasPermission(authorities, 'WAREHOUSES_TYPES.VIEW')) return
            handleDetail(record?.id)
          }}>
            {text}
          </p>
        ),
      },
      {
        title: 'Mô tả',
        key: 'description',
        dataIndex: 'description',
      },
      // {
      //   title: 'Thao tác',
      //   key: 'action',
      //   dataIndex: 'action',
      //   render: (_, record) => (
      //     <ActionsDropdown
      //       toggleUpdate={toggleUpdate}
      //       toggleDelete={toggleDelete}
      //       toggleDetail={toggleDetail}
      //       record={record}
      //       setSelectedRecord={setSelectedRecord}
      //     />
      //   ),
      // },
    ];
  }, []);

  return columns;
};
