import useUom from 'app/hooks/use-uom';
import { IUomGroup, IUomGroupParams } from 'app/shared/model/uom.model';
import React from 'react';
import { generateColumns } from './generate-columns';
import Table from 'app/components/table/table';

const { useGetUomGroups } = useUom;

interface IUomTableProps {
  filter: IUomGroupParams;
  setFilter: React.Dispatch<React.SetStateAction<IUomGroupParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
}

const UomGroupTable = (props: IUomTableProps) => {
  const { filter, setFilter, toggleDelete, toggleUpdate, toggleDetail, setSelectedRecord } = props;

  const columns = generateColumns(toggleUpdate, toggleDelete, toggleDetail, setSelectedRecord);

  const { data, isLoading } = useGetUomGroups(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IUomGroup>
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

export default UomGroupTable;
