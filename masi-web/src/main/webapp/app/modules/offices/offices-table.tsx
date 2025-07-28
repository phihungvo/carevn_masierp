import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';
import { IWorkspace, IWorkspaceParams } from 'app/shared/model/workspace.model';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import useWorkspace from 'app/hooks/use-workspace';

const { useGetWorkspacesQuery } = useWorkspace;

interface IOfficeTableProps {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  searchText: string;
  filter: IWorkspaceParams;
  setFilter: React.Dispatch<React.SetStateAction<IWorkspaceParams>>;
  setSelectedRecord: (id: string) => void;
}

const OfficesTable = (props: IOfficeTableProps) => {
  const { toggleUpdate, toggleDelete, searchText, filter, setFilter, setSelectedRecord } = props;

  const columns = generateColumns(toggleUpdate, toggleDelete, setSelectedRecord);

  const searchString = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, searchString, page: DEFAULT_PAGE }));
  }, [searchString]);

  const { data, isLoading } = useGetWorkspacesQuery(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IWorkspace>
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

export default OfficesTable;
