import React from 'react';
import { PaginationType, RecordType, RowSelection } from './table.d';
import Table, { TableProps } from '@uiw/react-table';
import Pagination from '../pagination/pagination';
import TableEmpty from './table-empty';
import './table.scss';

interface ITable<T extends object = {}> extends TableProps {
  dataSource: T[] | RecordType<T>[];
  pagination?: PaginationType;
  expandableChildren?: React.ReactNode;
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
}

const TableUIW = <T extends object>(props: ITable<T>) => {
  const {
    dataSource,
    pagination,
    expandableChildren,
    rowSelection,
    rowClassName,
    setSelected,
    extraRows,
    extraHeaders,
    stickyHeader = true,
    showIndex = true,
    className,
    ...rest
  } = props;
  return (
    <Table
      className={`custom-table-UIW ${className}`}
      {...rest}
      data={dataSource}
      empty={<TableEmpty />}
      footer={
        pagination ? (
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
        ) : null
      }
    />
  );
};

export default TableUIW;
