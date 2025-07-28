import Table from 'app/components/table/table';
import useUom from 'app/hooks/use-uom';
import { IUom, IUomParams } from 'app/shared/model/uom.model';
import React from 'react';
import { generateColumns } from './generate-columns';

const { useGetUoms } = useUom;

interface IUomTableProps {
  filter: IUomParams;
  setFilter: React.Dispatch<React.SetStateAction<IUomParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
}

const UomTable = (props: IUomTableProps) => {
  const { filter, setFilter, toggleDelete, toggleUpdate, setSelectedRecord } = props;

  const columns = generateColumns(toggleUpdate, toggleDelete, setSelectedRecord);

  const { data, isLoading } = useGetUoms(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IUom>
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

export default UomTable;
