import { ColumnsTypes } from 'app/components/table/table.d';
import { IUomGroup } from 'app/shared/model/uom.model';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import React from 'react';

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleDetail: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IUomGroup> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IUomGroup> = useMemo(() => {
    return [
      {
        title: 'Tên nhóm đơn vị',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
            {text}
          </p>
        ),
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
