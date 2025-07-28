import useWarehouse from 'app/hooks/use-warehouse';
import { IWarehouse, IWarehouseParams } from 'app/shared/model/warehouse.model';
import React from 'react';
import { generateColumns } from './generate-columns';
import Table from 'app/components/table/table';

const { useGetWarehouses } = useWarehouse;

interface IWarehouseTableProps {
  filter: IWarehouseParams;
  setFilter: React.Dispatch<React.SetStateAction<IWarehouseParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
}

const WarehouseTable = (props: IWarehouseTableProps) => {
  const { filter, setFilter, toggleDelete, toggleUpdate, toggleDetail, setSelectedRecord } = props;

  const columns = generateColumns(toggleUpdate, toggleDelete, toggleDetail, setSelectedRecord);

  const { data, isLoading } = useGetWarehouses(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IWarehouse>
      rowKey="id"
      loading={isLoading}
      columns={columns}
      dataSource={data?.data}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};

export default WarehouseTable;
