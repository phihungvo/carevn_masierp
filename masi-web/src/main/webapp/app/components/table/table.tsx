import React from 'react';
import { TableProps, Table as TableStrap } from 'reactstrap';
import './table.scss';
import { useFixedColumns } from 'app/hooks/use-fixed-columns';
import { useSelectCheckboxTable } from 'app/hooks/use-select-checkbox-table';
import TableHead from './table-head';
import TableBody from './table-body';
import TableLoading from './table-loading';
import TableEmpty from './table-empty';
import { ColumnsTypes, PaginationType, RecordType, RowSelection } from './table.d';
import Pagination from '../pagination/pagination';

interface ITable<T extends object = {}> extends TableProps {
  columns: ColumnsTypes<T>;
  dataSource: T[] | RecordType<T>[];
  pagination?: PaginationType;
  expandableChildren?: React.ReactNode;
  loading?: boolean;
  rowKey?: keyof T; // Key of row
  rowSelection?: RowSelection<T>;
  rowClassName?: (record: T, index: number) => string;
  setSelected?: React.Dispatch<
    React.SetStateAction<{
      [id: string]: T;
    }>
  >;
  extraHeaders?: React.ReactNode;
  extraRows?: React.ReactNode;
  stickyHeader?: boolean;
  showIndex?: boolean;
  checked_row_key?: string;
}

const Table = <T extends object>(props: ITable<T>) => {
  const {
    columns,
    dataSource,
    pagination,
    expandableChildren,
    loading,
    rowKey,
    rowSelection,
    rowClassName,
    setSelected,
    extraRows,
    extraHeaders,
    stickyHeader = true,
    responsive = true,
    showIndex = true,
    className,
    checked_row_key,
    ...rest
  } = props;

  useFixedColumns();
  const { selectedRecords, handleSelectRow, handleSelectAll } = useSelectCheckboxTable(
    dataSource,
    rowSelection,
    rowKey as string,
    setSelected,
  );

  return (
    <>
      <TableStrap
        {...rest}
        bordered
        responsive={stickyHeader ? responsive : false}
        className={`table-v2 table-v2--fixed ${className ? className : ''}`}
        data-border={true}
      >
        {columns?.length && (
          <TableHead
            rowSelection={rowSelection}
            columns={columns}
            handleSelectAll={handleSelectAll}
            data={dataSource}
            extraHeaders={extraHeaders}
            stickyHeader={stickyHeader}
            showIndex={showIndex}
            rowKey={rowKey as string}
            checked_row_key={checked_row_key}
          />
        )}
        {loading ? (
          <TableLoading />
        ) : dataSource?.length ? (
          <TableBody
            columns={columns}
            dataSource={dataSource}
            rowKey={rowKey as string}
            rowSelection={rowSelection}
            selectedRecords={selectedRecords}
            handleSelectRow={handleSelectRow}
            rowClassName={rowClassName}
            extraRows={extraRows}
            showIndex={showIndex}
            pagination={pagination}
            checked_row_key={checked_row_key}
          />
        ) : null}
      </TableStrap>
      {!dataSource?.length && !loading && <TableEmpty />}
      {pagination ? (
        <Pagination
          className="pagination-bar"
          showJumper={pagination?.showJumper ?? true}
          showTotal={pagination?.showTotal ?? false}
          totalLabel={pagination?.totalLabel}
          page={pagination?.page}
          totalCount={pagination?.totalCount}
          size={pagination?.size}
          onPageChange={pagination?.onPageChange}
        />
      ) : null}
    </>
  );
};

export default Table;
