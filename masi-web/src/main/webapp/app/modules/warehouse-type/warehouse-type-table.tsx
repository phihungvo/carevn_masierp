import React from 'react';
import { generateColumns } from './generate-columns';
import Table from 'app/components/table/table';
import useWarehouseType from 'app/hooks/use-warehouse-type';
import { IWarehouseType, IWarehouseTypeParams } from 'app/shared/model/warehouse-type.model';

const { useGetWarehouseTypes } = useWarehouseType;

interface IWarehouseTypeTableProps {
  filter: IWarehouseTypeParams;
  setFilter: React.Dispatch<React.SetStateAction<IWarehouseTypeParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
}

const WarehouseTypeTable = (props: IWarehouseTypeTableProps) => {
  const { filter, setFilter, toggleDelete, toggleUpdate, toggleDetail, setSelectedRecord } = props;

  const columns = generateColumns(toggleUpdate, toggleDelete, toggleDetail, setSelectedRecord);

  const { data, isLoading } = useGetWarehouseTypes(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IWarehouseType>
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

export default WarehouseTypeTable;
