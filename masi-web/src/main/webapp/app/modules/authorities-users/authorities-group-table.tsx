import React from 'react';

import useGroup from 'app/hooks/use-group';
import Table from 'app/components/table/table';
import { IGroup, IGroupParams } from 'app/shared/model/group.model';
import { generateColumnsAuthoritiesGroup } from './generate-columns-authorities-group';

const { useGroups } = useGroup;

interface IAuthoritiesGroupTableProps {
  filter: IGroupParams;
  setFilter: React.Dispatch<React.SetStateAction<IGroupParams>>;
  selectedRowKeys: string[];
  setSelectedRowKeys: React.Dispatch<React.SetStateAction<string[]>>;
}

const AuthoritiesGroupTable = (props: IAuthoritiesGroupTableProps) => {
  const { filter, setFilter, selectedRowKeys, setSelectedRowKeys } = props;

  const { data, isLoading } = useGroups({ ...filter });

  const columns = generateColumnsAuthoritiesGroup();

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IGroup>
      rowKey="id"
      loading={isLoading}
      columns={columns}
      dataSource={data?.data}
      rowSelection={{
        type: 'checkbox',
        onChange: (selectedRowKeys: string[], selectedRows) => {
          setSelectedRowKeys(selectedRowKeys);
          // setSelectedRows(selectedRows);
        },
        selectedRowKeys,
      }}
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
