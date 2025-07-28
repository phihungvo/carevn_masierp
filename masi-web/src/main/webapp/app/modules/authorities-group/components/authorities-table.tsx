import React from 'react';

import Table from 'app/components/table/table';
import { generateColumns } from 'app/modules/authorities/generate-columns';
import { IAuthority } from 'app/shared/model/authority.model';

interface IAuthoritiesTableProps {
  data?: IAuthority[]
}

const AuthoritiesTable = (props: IAuthoritiesTableProps) => {
  const { data } = props;

  const columns = generateColumns();

  return (
    <Table<IAuthority>
      rowKey="id"
      columns={columns}
      dataSource={data}
    />
  );
};

export default AuthoritiesTable;
