import React from 'react';
import TableParentRow from './table-parent-row';
import { ColumnsTypes, PaginationType, RecordType } from './table.d';

interface ITableBodyProps<T extends object = {}> {
  columns: ColumnsTypes<T>;
  dataSource: T[] | RecordType<T>[];
  rowKey?: string;
  rowSelection?: any;
  selectedRecords: any;
  handleSelectRow: (e: React.ChangeEvent<HTMLInputElement>, data: T, index: number) => void;
  rowClassName?: (record: T, index: number) => string;
  extraRows?: React.ReactNode;
  showIndex?: boolean;
  pagination?: PaginationType;
  checked_row_key?: string;
}

const TableBody = React.memo(<T extends object>(props: ITableBodyProps<T>) => {
  const {
    columns, dataSource, rowKey, rowSelection, selectedRecords,
    handleSelectRow, rowClassName, extraRows, showIndex, pagination, checked_row_key
  } = props;

  return (
    <tbody>
      {/* Map though all items of datasource to render all rows in table  */}
      {dataSource.map((data: T | RecordType<T>, index: number) => (
        <React.Fragment key={rowKey ? data[rowKey] : index}>
          <TableParentRow
            columns={columns}
            data={data}
            rowKey={rowKey}
            rowSelection={rowSelection}
            selectedRecords={selectedRecords}
            handleSelectRow={handleSelectRow}
            index={index}
            {...(rowClassName && { rowClassName: () => rowClassName(data, index) })}
            showIndex={showIndex}
            pagination={pagination}
            checked_row_key={checked_row_key}
          />
        </React.Fragment>
      ))}
      {extraRows}
    </tbody>
  );
});

TableBody.displayName = 'TableBody';

export default TableBody;
