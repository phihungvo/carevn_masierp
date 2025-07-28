import { ColumnsTypes } from 'app/components/table/table.d';
import { IUom } from 'app/shared/model/uom.model';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import React from 'react';
import uomMapping from './uom-mapping';

const { companyTextMapping } = uomMapping;

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IUom> => {
  const columns: ColumnsTypes<IUom> = useMemo(() => {
    return [
      {
        title: 'Tên đơn vị',
        key: 'name',
        dataIndex: 'name',
        render: text => text,
      },
      {
        title: 'Thao tác',
        key: 'action',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown toggleUpdate={toggleUpdate} toggleDelete={toggleDelete} record={record} setSelectedRecord={setSelectedRecord} />
        ),
      },
    ];
  }, []);

  return columns;
};
