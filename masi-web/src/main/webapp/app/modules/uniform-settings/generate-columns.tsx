import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IUniform } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import React, { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleActive: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IUniform> => {
  const columns: ColumnsTypes<IUniform> = useMemo(() => {
    return [
      {
        title: 'Mã đồng phục',
        key: 'code',
        dataIndex: 'code',
      },
      {
        title: 'Tên đồng phục',
        key: 'name',
        dataIndex: 'name',
      },
      {
        title: 'Đơn giá',
        key: 'basePrice',
        dataIndex: 'basePrice',
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Đơn vị',
        key: 'uom',
        dataIndex: 'uom',
        render: (_, record) => record?.uomDTO?.name,
      },
      {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status',
        render: (_, record) =>
          record?.status === 'ENABLE' ? <Badge color={'success'}>Hoạt động</Badge> : <Badge color={'error'}>Không sử dụng</Badge>,
      },
      {
        title: 'Thao tác',
        key: 'action',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown toggleUpdate={toggleUpdate} toggleDelete={toggleDelete} toggleActive={toggleActive} record={record} setSelectedRecord={setSelectedRecord} />
        ),
      },
    ];
  }, []);

  return columns;
};
