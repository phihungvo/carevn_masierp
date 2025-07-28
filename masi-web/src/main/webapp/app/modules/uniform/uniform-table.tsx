import Table from 'app/components/table/table';
import React from 'react';
import { generateColumns } from './generate-columns';
import { IUniformStock, IUniformStockParams } from 'app/shared/model/uniform.model';
import useUniform from 'app/hooks/use-uniform';

const { useUniformStocks } = useUniform;

interface IUniformTableProps {
  filter: IUniformStockParams;
  setFilter: React.Dispatch<React.SetStateAction<IUniformStockParams>>;
}

const UniformTable = (props: IUniformTableProps) => {
  const { filter, setFilter } = props;

  const columns = generateColumns();

  const { data, isLoading } = useUniformStocks(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IUniformStock>
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

export default UniformTable;
