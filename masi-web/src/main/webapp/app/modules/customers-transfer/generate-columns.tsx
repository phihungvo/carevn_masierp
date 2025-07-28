import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import { ICustomer } from 'app/shared/model/customer.model';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import customerMapping from '../customers/customer-mapping';

const { mapCustomerStatusText } = customerMapping;

export const generateColumns = (toggleTransfer: () => void, setSelectedRecord: (id: string) => void): ColumnsTypes<ICustomer> => {
  const columns: ColumnsTypes<ICustomer> = useMemo(() => {
    return [
      {
        key: 'customerCode',
        title: 'Mã KH',
        dataIndex: 'customerCode',
      },
      {
        key: 'companyName',
        title: 'Tên Cty',
        dataIndex: 'companyName',
      },
      {
        key: 'lastName',
        title: 'Họ',
        dataIndex: 'lastName',
      },
      {
        key: 'firstName',
        title: 'Tên',
        dataIndex: 'firstName',
      },
      {
        key: 'email',
        title: 'Email',
        dataIndex: 'email',
      },
      {
        key: 'customerStatus',
        title: 'Trạng thái',
        dataIndex: 'customerStatus',
        render: text => <Badge color={'success'}>{mapCustomerStatusText(text)}</Badge>,
      },
      {
        key: 'action',
        title: 'Thao tác',
        width: 106,
        render: (_, record) => <ActionsDropdown toggleTransfer={toggleTransfer} record={record} setSelectedRecord={setSelectedRecord} />,
      },
    ];
  }, []);

  return columns;
};
