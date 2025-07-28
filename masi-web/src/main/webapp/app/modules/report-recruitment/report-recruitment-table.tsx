import React, { Dispatch, SetStateAction } from 'react';

import Table from 'app/components/table/table';
import useReports from 'app/hooks/use-reports';
import { generateColumns } from './generate-columns';
import { IRecruitmentReportParams, IReportRecruitment } from 'app/shared/model/report.model';

const { useGetRecruitmentReports } = useReports;

interface IReportRecruitmentTableProps {
  filter: IRecruitmentReportParams;
  setFilter: Dispatch<SetStateAction<IRecruitmentReportParams>>;
}

const ReportRecruitmentTable = ({ filter, setFilter }: IReportRecruitmentTableProps) => {
  const columns = generateColumns();

  const { data, isLoading } = useGetRecruitmentReports(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IReportRecruitment>
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

export default ReportRecruitmentTable;
