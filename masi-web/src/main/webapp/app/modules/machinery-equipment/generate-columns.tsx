import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { ColumnsTypes } from 'app/components/table/table.d';
import Tooltip from 'app/components/tooltip/tooltip';
import { IItem } from 'app/shared/model/item.model';
import React, { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import machineryEquipmentMapping from './machinery-equipment-mapping';

const { machineryEquipmentStatusBadgeMapping } = machineryEquipmentMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  togglePropose: () => void,
  toggleApprove: () => void,
  setSelectedRecord: (id: string) => void,
  toggleDispose: () => void,
  toggleActivate: () => void,
): ColumnsTypes<IItem> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IItem> = useMemo(() => {
    const col: ColumnsTypes<IItem> = [
      {
        title: 'Mã VT - CCDC',
        key: 'code',
        dataIndex: 'code',
        render: (text, record) => (
          <p className="attachment-link" onClick={() => handleDetail(record?.id)}>
            {text}
          </p>
        ),
      },
      {
        title: 'Tên VT - CCDC',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <EllipsisParagraph text={text} width={300} id={`name-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Đơn vị',
        key: 'name',
        dataIndex: 'uom',
        render: text => {
          return text?.name;
        },
      },
      {
        title: 'Xuất xứ',
        key: 'name',
        dataIndex: 'attribute',
        render: text => {
          return text?.origin;
        },
      },
      {
        title: 'Loại',
        key: 'itemCategoryId',
        dataIndex: 'itemCategory',
        render: (text, record) => (
          <Tooltip label={text?.name} target={`itemCategory-${record.id}`}>
            <EllipsisParagraph text={text?.name} width={120} id={`itemCategory-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Đơn giá',
        key: 'unitPrice',
        dataIndex: 'unitPrice',
      },
      {
        title: 'Trạng thái',
        key: 'isActive',
        dataIndex: 'isActive',
        render: text => machineryEquipmentStatusBadgeMapping(text),
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        dataIndex: 'action',
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            togglePropose={togglePropose}
            toggleApprove={toggleApprove}
            toggleDispose={toggleDispose}
            toggleActivate={toggleActivate}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];

    return col;
  }, [toggleUpdate, toggleDelete, toggleDispose, toggleActivate, setSelectedRecord]);

  return columns;
};
