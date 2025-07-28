import React, { useState } from 'react';
import Table from 'app/components/table/table';
import useUniform from "app/hooks/use-uniform";
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from "app/constants/common";
import { IUniformRelease, IUniformReleaseParams } from "app/shared/model/uniform.model";
import { generateColumnsHistory } from "app/modules/report-uniforms-expired/generate-columns-history";

interface IReportUniformsExpiredTableProps {
  employeeIds: string
}

const { useUniformReleases } = useUniform;

const ReportUniformsExpiredHistoryTable = (props: IReportUniformsExpiredTableProps) => {
  const { employeeIds } = props;

  const [ filter ,setFilter] = useState<IUniformReleaseParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    employeeIds: [employeeIds]
  });

  const columns = generateColumnsHistory();

  const { data, isLoading } = useUniformReleases(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IUniformRelease>
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

export default ReportUniformsExpiredHistoryTable;
