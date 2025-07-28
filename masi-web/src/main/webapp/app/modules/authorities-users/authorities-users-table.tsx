import React from 'react';

import Table from 'app/components/table/table';
import useEmployee from 'app/hooks/use-employee';
import { useDebounce } from 'app/hooks/use-debounce';
import { generateColumns } from './generate-columns';
import { IGroupParams } from 'app/shared/model/group.model';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';

const { useGetEmployeeProfilesQuery } = useEmployee;

interface IAuthoritiesUsersTableProps {
    filter: IGroupParams;
    setFilter: React.Dispatch<React.SetStateAction<IGroupParams>>;
    searchText: string;
    setSelectedRecord: (id: string) => void;
    toggleUpdateUsers: () => void;
    toggleDetail: () => void;
    selectedRow: any
    setSelectedRow: React.Dispatch<any>
    toggleUpdateAccount: () => void
    toggleSettingCompanies: () => void
}

const AuthoritiesUsersTable = (props: IAuthoritiesUsersTableProps) => {
    const {
      filter,
      setFilter,
      searchText,
      setSelectedRecord,
      toggleUpdateUsers,
      toggleDetail,
      selectedRow,
      setSelectedRow,
      toggleUpdateAccount,
      toggleSettingCompanies
    } = props;

    const searchDebounce = useDebounce(searchText, 500);

    const { data, isLoading } = useGetEmployeeProfilesQuery({ ...filter, search: searchDebounce, hasAccount: true, isFilterCompany: false });

    const columns = generateColumns(
      setSelectedRecord,
      toggleUpdateUsers,
      toggleDetail,
      selectedRow,
      setSelectedRow,
      toggleUpdateAccount,
      toggleSettingCompanies,
    );

    const totalCount = data?.totalRecord || 0;
    const { page, size } = filter;

    return (
        <Table<IEmployeeProfiles>
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

export default AuthoritiesUsersTable;
