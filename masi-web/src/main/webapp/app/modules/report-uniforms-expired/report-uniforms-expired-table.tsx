import Table from 'app/components/table/table';
import useReports from 'app/hooks/use-reports';
import { IUniformExpiring, IUniformExpiringParams } from 'app/shared/model/report.model';
import React, { SetStateAction } from 'react';
import { generateColumns } from './generate-columns';

const { useGetUniformExpiringReports } = useReports;

interface IReportUniformsExpiredTableProps {
  filter?: IUniformExpiringParams;
  setFilter?: React.Dispatch<SetStateAction<IUniformExpiringParams>>;
  toggleModalHistory: () => void;
  setEmployeeIds:(id: string) => void
}

const ReportUniformsExpiredTable = (props: IReportUniformsExpiredTableProps) => {
  const { filter, setFilter, toggleModalHistory ,setEmployeeIds} = props;

  const columns = generateColumns(toggleModalHistory, setEmployeeIds);

  const { data, isLoading } = useGetUniformExpiringReports(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  console.log('data', data);

  return (
    <Table<IUniformExpiring>
      rowKey="employeeCode"
      loading={isLoading}
      dataSource={data?.data || []}
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

export default ReportUniformsExpiredTable;
