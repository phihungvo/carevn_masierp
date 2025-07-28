import useReports from 'app/hooks/use-reports';
import React, { SetStateAction } from 'react';
import { generateColumns } from './generate-columns';
import { IEmployeeExpiringContract, IEmployeeExpiringContractParams } from 'app/shared/model/report.model';
import Table from 'app/components/table/table';

const { useGetEmployeeExpiringContractReports } = useReports;

interface IReportEmployeeExpiringContractTableProps {
  filter?: IEmployeeExpiringContractParams;
  setFilter?: React.Dispatch<SetStateAction<IEmployeeExpiringContractParams>>;
}

const ReportEmployeeExpiringContractTable = (props: IReportEmployeeExpiringContractTableProps) => {
  const { filter, setFilter } = props;

  const columns = generateColumns();

  const { data, isLoading } = useGetEmployeeExpiringContractReports(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IEmployeeExpiringContract>
      rowKey="id"
      loading={isLoading}
      dataSource={data?.data}
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

export default ReportEmployeeExpiringContractTable;
