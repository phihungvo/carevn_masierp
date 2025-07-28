import React from 'react';
import { generateColumns } from './generate-columns';
import Table from 'app/components/table/table';
import { IUniformSupport, IUniformSupportParams } from 'app/shared/model/report.model';

interface IReportUniformsReimbursementTableProps {
  filter: IUniformSupportParams;
  setFilter: React.Dispatch<React.SetStateAction<IUniformSupportParams>>;
  data: IUniformSupport[];
  isLoading?: boolean;
  totalCount?: number;
}

const ReportUniformsReimbursementTable = (props: IReportUniformsReimbursementTableProps) => {
  const { filter, setFilter, isLoading, totalCount, data } = props;

  const columns = generateColumns();

  const { page, size } = filter;

  return (
    <Table
      rowKey="id"
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

export default ReportUniformsReimbursementTable;
