import Table from 'app/components/table/table';
import { IHrChangeReport, IHrChangeReportParams } from 'app/shared/model/report.model';
import React, { SetStateAction } from 'react';
import { generateColumns } from './generate-columns';

interface IReportHrChangeTableProps {
  data: IHrChangeReport[];
  isLoading: boolean;
  totalCount: number;
  filter: IHrChangeReportParams;
  setFilter: React.Dispatch<SetStateAction<IHrChangeReportParams>>;
}

const ReportHrChangeTable = (props: IReportHrChangeTableProps) => {
  const { data, isLoading, totalCount, filter, setFilter } = props;

  const columns = generateColumns();
  const { page, size } = filter;

  return (
    <Table<IHrChangeReport>
      loading={isLoading}
      dataSource={data}
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

export default ReportHrChangeTable;
