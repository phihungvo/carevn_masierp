import Table from 'app/components/table/table';
import { RowSelection } from 'app/components/table/table.d';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useAuthorities } from 'app/hooks/use-authority';
import { useDebounce } from 'app/hooks/use-debounce';
import { IAuthority, IAuthorityParams } from 'app/shared/model/authority.model';
import React, { useEffect, useState } from 'react';
import { generateColumns } from './generate-columns';

interface IAuthoritiesTableProps {
  rowSelection?: RowSelection<IAuthority>;
  searchText?: string;
}

const AuthoritiesTable = (props: IAuthoritiesTableProps) => {
  const { rowSelection, searchText } = props;

  const [filter, setFilter] = useState<IAuthorityParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: searchText,
  });

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search }));
  }, [search]);

  const { data, isLoading } = useAuthorities(filter);

  const columns = generateColumns();

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IAuthority>
      rowKey="id"
      columns={columns}
      dataSource={data?.data}
      loading={isLoading}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
      rowSelection={rowSelection}
    />
  );
};

export default AuthoritiesTable;
