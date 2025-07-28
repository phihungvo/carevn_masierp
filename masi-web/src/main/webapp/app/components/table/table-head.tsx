import React from 'react';
import Input from '../input/input';
import { ColumnsTypes, RowSelection } from './table.d';
import { isSubsetArrayString } from 'app/shared/util/is-subset-array-string';

interface ITableHeadProps<T extends object = {}> {
  rowSelection?: RowSelection<T>;
  columns: ColumnsTypes<T>;
  handleSelectAll: (e: React.ChangeEvent<HTMLInputElement>) => void;
  data: T[];
  extraHeaders?: React.ReactNode;
  stickyHeader?: boolean;
  showIndex?: boolean;
  rowKey?: string;
  checked_row_key?: string;
}

const TableHead = React.memo(<T extends object>(props: ITableHeadProps<T>) => {
  const { rowSelection, columns, handleSelectAll, data, extraHeaders, stickyHeader, showIndex, rowKey, checked_row_key } = props;
  return (
    <thead>
      {extraHeaders}
      <tr>
        {rowSelection?.type === 'checkbox' && (
          <th style={{ width: 64, minWidth: 64 }} className={`${stickyHeader ? 'sticky-header' : ''}`}>
            <span>
              <Input
                type="checkbox"
                onChange={handleSelectAll}
                checked={checked_row_key && data && data?.length > 0 ? data?.every(item => item?.[checked_row_key])
                  : data && data?.length > 0 && isSubsetArrayString(rowSelection?.selectedRowKeys, data?.map(item => item?.[rowKey]))}
                disabled={!!rowSelection.disabledKeys?.length}
              />
            </span>
          </th>
        )}
        {showIndex && (
          <th className={`${stickyHeader ? 'sticky-header' : ''}`.trim()} style={{ width: 80, minWidth: 76   }}>
            <span>STT</span>
          </th>
        )}
        {columns?.map((column, index) => (
          <th
            style={{ width: column.width, minWidth: column.width }}
            key={`table-head-${column.key ?? index}`}
            className={`${column.className ? column.className : ''} ${column.fixed ? `th-fixed-${column.fixed}` : ''} ${stickyHeader ? 'sticky-header' : ''
              }`.trim()}
          >
            <span style={{ justifyContent: column.align, textAlign: column.align ?? 'left' }}>{column.title}</span>
          </th>
        ))}
      </tr>
    </thead>
  );
});

TableHead.displayName = 'TableHead';

export default TableHead;
