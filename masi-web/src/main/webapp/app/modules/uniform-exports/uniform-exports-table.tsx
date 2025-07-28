import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { IOrderParams } from 'app/shared/model/order.model';
import { generateColumns } from './generate-columns';
import useUniform from 'app/hooks/use-uniform';
import { IUniformRelease, IUniformReleaseParams } from 'app/shared/model/uniform.model';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';

const { useUniformReleases } = useUniform;

interface IUniformExportsTableProps {
  searchText: string;
  filter: IOrderParams;
  setFilter: React.Dispatch<React.SetStateAction<IUniformReleaseParams>>;
  setSelectedRecord: React.Dispatch<React.SetStateAction<string>>;
  toggleDetail: () => void;
}

const UniformExportsTable = (props: IUniformExportsTableProps) => {
  const { searchText, filter, setFilter, setSelectedRecord, toggleDetail } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const columns = generateColumns(setSelectedRecord, toggleDetail);

  const { data, isLoading } = useUniformReleases(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IUniformRelease>
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

export default UniformExportsTable;
