import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { IOrderParams } from 'app/shared/model/order.model';
import { generateColumns } from './generate-columns';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import useUniform from 'app/hooks/use-uniform';
import { IUniformOrder, IUniformOrderParams } from 'app/shared/model/uniform.model';

const { useUniformOrders } = useUniform;

interface IUniformOrdersTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleApprove: () => void;
  toggleCancel: () => void;
  toggleStock: () => void;
  searchText: string;
  filter: IOrderParams;
  setFilter: React.Dispatch<React.SetStateAction<IUniformOrderParams>>;
  setSelectedRecord: (id: string) => void;
}

const UniformOrdersTable = (props: IUniformOrdersTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    toggleApprove,
    toggleCancel,
    toggleStock,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
  } = props;

  const columns = generateColumns(toggleDetail, toggleUpdate, toggleDelete, toggleApprove, toggleCancel, toggleStock, setSelectedRecord);

  const name = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, name, page: DEFAULT_PAGE }));
  }, [name]);

  const { data, isLoading } = useUniformOrders(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IUniformOrder>
      rowKey="id"
      loading={isLoading}
      dataSource={data?.data}
      columns={columns}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};

export default UniformOrdersTable;
