import { ColumnsTypes } from 'app/components/table/table.d';
import { IItem } from 'app/shared/model/item.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import React, { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  setSelectedRecord: (id: string) => void,
  toggleDetail: () => void,
  haveActions?: boolean,
): ColumnsTypes<IItem> => {
  const columns: ColumnsTypes<IItem> = useMemo(() => {
    const col: ColumnsTypes<IItem> = [
      {
        title: 'Mã vật phẩm',
        key: 'code',
        dataIndex: 'code',
        render: (text, record) => (
          <Tooltip label={text} target={`code-${record.id}`}>
            <EllipsisParagraph text={text} width={120} id={`code-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Tên vật phẩm',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <EllipsisParagraph text={text} width={120} id={`name-${record.id}`} />
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
        title: 'Loại vât phẩm',
        key: 'itemCategoryId',
        dataIndex: 'itemCategory',
        render: (text, record) => (
          <Tooltip label={text?.name} target={`itemCategory-${record.id}`}>
            <EllipsisParagraph text={text?.name} width={120} id={`itemCategory-${record.id}`} />
          </Tooltip>
        ),
      },
    ];

    if (haveActions) {
      col.push({
        title: 'Thao tác',
        key: 'action',
        width: 106,
        dataIndex: 'action',
        render: record => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      });
    }

    return col;
  }, [toggleUpdate, toggleDelete, setSelectedRecord]);

  return columns;
};
