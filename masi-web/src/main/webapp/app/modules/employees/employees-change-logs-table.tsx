import Table from 'app/components/table/table';
import { IEmployeeChangeLog, IEmployeeChangeLogParams } from 'app/shared/model/employee.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import React from 'react';
import { generateColumnsChangelog } from './generate-columns-change-logs';

interface IEmployeeChangeLogTableProps {
  data: PaginationResponse<IEmployeeChangeLog>;
  isLoading: boolean;
  filter: IEmployeeChangeLogParams;
  setFilter: React.Dispatch<React.SetStateAction<IEmployeeChangeLog>>;
  setSelectedRecord: (id: string) => void;
  toggleDetail: () => void;
}

const EmployeesChangeLogTable = (props: IEmployeeChangeLogTableProps) => {
  const { data, isLoading, filter, setFilter, setSelectedRecord, toggleDetail } = props;

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  const columns = generateColumnsChangelog(toggleDetail, setSelectedRecord);

  return (
    <Table<IEmployeeChangeLog>
      rowKey="id"
      dataSource={data?.data}
      columns={columns}
      loading={isLoading}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};

export default EmployeesChangeLogTable;
