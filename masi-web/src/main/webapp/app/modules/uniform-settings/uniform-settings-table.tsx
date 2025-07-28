import { IUniform, IUniformParams } from 'app/shared/model/uniform.model';
import React from 'react';
import { generateColumns } from './generate-columns';
import useUniform from 'app/hooks/use-uniform';
import Table from 'app/components/table/table';

const { useUniforms } = useUniform;

interface IUniformSettingsTableProps {
  filter: IUniformParams;
  setFilter: React.Dispatch<React.SetStateAction<IUniformParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleActive: () => void;
}

const UniformSettingsTable = (props: IUniformSettingsTableProps) => {
  const { filter, setFilter, toggleDelete, toggleUpdate, toggleActive, setSelectedRecord } = props;

  const columns = generateColumns(toggleUpdate, toggleDelete, toggleActive, setSelectedRecord);

  const { data, isLoading } = useUniforms(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IUniform>
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

export default UniformSettingsTable;
