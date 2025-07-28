import React from 'react';
import Input from '../input/input';
import { ColumnsTypes, PaginationType, RecordType } from './table.d';

interface ITableParentProps<T extends object = {}> {
  columns: ColumnsTypes<T>;
  data: T | RecordType<T>;
  rowKey?: string;
  rowSelection?: any;
  selectedRecords: any;
  handleSelectRow: (e: React.ChangeEvent<HTMLInputElement>, data: T, index: number) => void;
  index: number;
  rowClassName?: (record?: T, index?: number) => string;
  showIndex?: boolean;
  pagination?: PaginationType;
  checked_row_key?: string;
}

const TableParentRow = <T extends object>(props: ITableParentProps<T>) => {
  const {
    checked_row_key, columns, data, rowKey, rowSelection, selectedRecords,
    handleSelectRow, index, rowClassName, showIndex, pagination
  } = props;

  return (
    <tr key={`parent-row-${index}`} className={rowClassName ? rowClassName() : ''}>
      {/* Map through all columns to render all columns in a rows  */}
      {rowSelection?.type === 'checkbox' && (
        <td>
          <span>
            <Input
              type="checkbox"
              checked={checked_row_key ?
                data[checked_row_key] : selectedRecords.selectedRowKeys.includes(rowKey ? data[rowKey] : index)}
              value={rowKey ? data[rowKey] : index}
              onChange={e => handleSelectRow(e, data, index)}
              disabled={rowSelection?.disabledKeys?.includes(rowKey ? data[rowKey] : index)}
            />
          </span>
        </td>
      )}
      {showIndex && (
        <td>
          <span>
            {index + 1 + (pagination?.page ?? 0) * (pagination?.size ?? 0) >= 10 ? '' : '0'}
            {index + 1 + (pagination?.page ?? 0) * (pagination?.size ?? 0)}
          </span>
        </td>
      )}
      {columns.map(col => (
        <td key={`$parent-cel-${col.key}`} className={`${col.fixed ? `td-fixed-${col.fixed}` : ''}`.trim()}>
          <span style={{ justifyContent: col.align, textAlign: col.align ?? 'left' }}>
            {
              col?.render
                ? col.render(data?.[col?.dataIndex], data, index) // If a column has render method, use this for custom render cell by user
                : data?.[col?.dataIndex] // Or else only render the value of data in cell
            }
          </span>
        </td>
      ))}
    </tr>
  );
};

export default TableParentRow;
