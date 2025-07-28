import './report-uniforms-reimbursement.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import ReportUniformsReimbursementHeader from './report-uniforms-reimbursement-header';
import ReportUniformsReimbursementTable from './report-uniforms-reimbursement-table';
import Flex from 'app/components/flex/flex';
import ReportUniformsReimbursementChart from './components/report-uniforms-reimbursement-chart';
import { IUniformSupportParams } from 'app/shared/model/report.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import useReports from 'app/hooks/use-reports';

const { useGetUniformSupportReport } = useReports;

const ReportUniformsReimbursement = () => {
  const [filter, setFilter] = useState<IUniformSupportParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const { data, isLoading } = useGetUniformSupportReport(filter);

  const totalCount = data?.totalRecord || 0;

  return (
    <Flex direction="column" gap={16}>
      <Typography level={3}>Báo cáo xuất ứng, hoàn ứng</Typography>
      <Card header={<ReportUniformsReimbursementHeader setFilter={setFilter} />}>
        <ReportUniformsReimbursementTable
          filter={filter}
          setFilter={setFilter}
          data={data?.data}
          isLoading={isLoading}
          totalCount={totalCount}
        />
      </Card>

      <ReportUniformsReimbursementChart data={data?.data} />
    </Flex>
  );
};

export default ReportUniformsReimbursement;
