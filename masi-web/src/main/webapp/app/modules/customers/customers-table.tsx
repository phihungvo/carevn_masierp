import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { ICustomer, ICustomerParams } from 'app/shared/model/customer.model';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import { generateColumns } from './generate-columns';
import useCustomers from 'app/hooks/use-customers';
import { DebouncedFunc, debounce, keyBy } from 'lodash';

const { useGetEnabledCustomers } = useCustomers;

interface ICustomersTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDispose: () => void;
  toggleTransfer: () => void;
  searchText: string;
  filter: ICustomerParams;
  setFilter: React.Dispatch<React.SetStateAction<ICustomerParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: ICustomer[];
  setSelectedRows: React.Dispatch<React.SetStateAction<ICustomer[]>>;
}

const CustomersTable = (props: ICustomersTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDispose,
    toggleTransfer,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    selectedRowKeys,
    setSelectedRowKeys,
    selectedRows,
    setSelectedRows,
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data, isLoading, isRefetching } = useGetEnabledCustomers(filter);

  const columns = generateColumns(toggleDetail, toggleUpdate, toggleDispose, toggleTransfer, setSelectedRecord);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  useEffect(() => {
    var statusDebounce: DebouncedFunc<() => void>;
    if (data) {
      statusDebounce = debounce(() => {
        var obj = keyBy(data.data, 'id');
        setSelectedRows(pre => [...pre.map(e => obj[e.id])]);
      }, 500);

      statusDebounce();
    }

    return () => statusDebounce && statusDebounce.cancel();
  }, [isRefetching]);

  return (
    <Table<ICustomer>
      rowKey="id"
      loading={isLoading}
      dataSource={data?.data}
      columns={columns}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
        showTotal: true,
      }}
    />
  );
};

export default CustomersTable;
