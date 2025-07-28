import Table from 'app/components/table/table';
import useGroup from 'app/hooks/use-group';
import { IGroup, IGroupParams } from 'app/shared/model/group.model';
import React from 'react';
import { generateColumns } from './generate-columns';

const { useGroups } = useGroup;

interface IAuthoritiesGroupTableProps {
  filter: IGroupParams;
  setFilter: React.Dispatch<React.SetStateAction<IGroupParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
}

const AuthoritiesGroupTable = (props: IAuthoritiesGroupTableProps) => {
  const { filter, setFilter, toggleDelete, toggleUpdate, toggleDetail, setSelectedRecord } = props;

  const { data, isLoading } = useGroups(filter);

  const columns = generateColumns(toggleUpdate, toggleDelete, toggleDetail, setSelectedRecord);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IGroup>
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

export default AuthoritiesGroupTable;
