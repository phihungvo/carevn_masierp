import React from 'react';

import Table from 'app/components/table/table';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import { generateColumnsAccount } from './generate-columns-account';

interface IAccountHeaderProps {
  data?: IEmployeeProfiles[];
}

const AccountTable = (props: IAccountHeaderProps) => {
  const { data } = props;

  const columns = generateColumnsAccount();

  return (
    <Table<IEmployeeProfiles>
      rowKey="id"
      dataSource={data}
      columns={columns}
    />
  );
};

export default AccountTable;
