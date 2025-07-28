import React from 'react';
import Input from '../input/input';
import { ColumnsTypes, RecordType } from './table.d';

interface ITableChildProps<T extends object = {}> {
  columns: ColumnsTypes<T>;
  data: T | RecordType<T>;
  rowKey?: string;
  rowSelection?: any;
  selectedRecords: any;
  handleSelectRow: (e: React.ChangeEvent<HTMLInputElement>, data: T) => void;
  index: number;
  childIndex: number;
  child: T | RecordType<T>;
}

const TableChildRow = <T extends object>(props: ITableChildProps<T>) => {
  const { columns, data, rowKey, rowSelection, selectedRecords, handleSelectRow, index, childIndex, child } = props;

  return (
    <tr key={`child-row-${childIndex}`} className={`child-row child-row-${index}`}>
      {rowSelection?.type === 'checkbox' && (
        <td>
          <span>
            <Input
              type="checkbox"
              checked={selectedRecords.selectedRowKeys.includes(rowKey ? data[rowKey] : index)}
              value={rowKey ? data[rowKey] : index}
              onChange={e => handleSelectRow(e, data)}
            />
          </span>
        </td>
      )}
      {columns.map(col => (
        <td key={`child-col-${col.key}`}>
          <span style={{ justifyContent: col.align, textAlign: col.align ?? 'left' }}>
            {
              col?.render
                ? col.render(child?.[col.dataIndex], child, index) // If a column has render method, use this for custom render cell by user
                : child?.[col.dataIndex] // Or else only render the value of data in cell
            }
          </span>
        </td>
      ))}
    </tr>
  );
};

export default TableChildRow;
