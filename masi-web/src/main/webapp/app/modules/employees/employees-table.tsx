import Table from 'app/components/table/table';
import { DEFAULT_PAGE } from 'app/constants/common';
import useAccount from 'app/hooks/use-account';
import { useDebounce } from 'app/hooks/use-debounce';
import useEmployee from 'app/hooks/use-employee';
import { IEmployee, IEmployeeParams, IEmployeeProfiles } from 'app/shared/model/employee.model';
import { debounce, DebouncedFunc, keyBy } from 'lodash';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';

const { useGetEmployeeProfilesQuery } = useEmployee;
const { useGetAccountsStatuses } = useAccount;

interface IEmployeeTableProps {
  toggleDetail: () => void;
  toggleDeactivate: () => void;
  toggleActivate: () => void;
  toggleUpload: () => void;
  toggleConfirmLeave: () => void;
  searchText: string;
  filter: IEmployeeParams;
  setFilter: React.Dispatch<React.SetStateAction<IEmployeeParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRows: IEmployee[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IEmployee[]>>;
  toggleProvideAccount: () => void;
  toggleActivateTimkeepingDevicer: () => void;
}

const EmployeesTable = (props: IEmployeeTableProps) => {
  const {
    toggleDetail,
    toggleDeactivate,
    toggleActivate,
    toggleUpload,
    toggleConfirmLeave,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    setSelectedRows,
    toggleProvideAccount,
    toggleActivateTimkeepingDevicer
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);
  const { data, isLoading, isRefetching } = useGetEmployeeProfilesQuery(filter);
  const { data: accountsStatuses } = useGetAccountsStatuses(data?.data.map(e => e.id));
  const columns = generateColumns(toggleDetail, toggleDeactivate, toggleActivate, toggleUpload, toggleConfirmLeave, setSelectedRecord, filter, toggleProvideAccount, toggleActivateTimkeepingDevicer);
  const employees = React.useMemo(() => {
    if (!data) return [];
    if (!accountsStatuses) return data.data;
    return data.data.map(e => ({ ...e, account: accountsStatuses.find(a => a.id === e.id) }));
  }, [data?.data, accountsStatuses]);
  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  useEffect(() => {
    let statusDebounce: DebouncedFunc<() => void>;
    if (data) {
      statusDebounce = debounce(() => {
        const obj = keyBy(data.data, 'id');
        setSelectedRows(pre => [...pre.map(e => obj[e.id])]);
      }, 500);

      statusDebounce();
    }

    return () => statusDebounce && statusDebounce.cancel();
  }, [isRefetching]);

  return (
    <Table<IEmployeeProfiles>
      rowKey="id"
      loading={isLoading}
      dataSource={employees}
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

export default EmployeesTable;
