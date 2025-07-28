import Tabs from '@uiw/react-tabs';
import React, { useState } from 'react';

import Flex from 'app/components/flex/flex';
import Card from 'app/components/card/card';
import useReports from 'app/hooks/use-reports';
import ReportLeaveRegimeTable from 'app/modules/report-leave-regime/report-leave-regime-table';
import ReportLeaveRegimeHeader from 'app/modules/report-leave-regime/report-leave-regime-header';
import ReportLeaveRegimeChart from 'app/modules/report-leave-regime/components/report-leave-regime-chart';
import { Typography } from 'app/components/typography/typography';
import { ILeaveRegimeReportParams } from 'app/shared/model/report.model';
import { EREPORTS, EREPORTS_TEXT } from 'app/shared/model/enumerations/reports';

const { useGetLeaveRegimeReports } = useReports;

const ReportLeaveRegime = () => {
  const [filter, setFilter] = useState<ILeaveRegimeReportParams>();

  const { data, isLoading } = useGetLeaveRegimeReports(filter);

  return (
    <Flex className='page_container' direction="column" gap={16}>
      <Typography level={4}>Báo cáo số lượng NV đăng ký nghỉ chế độ theo khung thời gian</Typography>

      <Tabs type='card' activeKey={EREPORTS.DATATABLE}>
        <Tabs.Pane label={EREPORTS_TEXT.DATATABLE} key={EREPORTS.DATATABLE}>
          <Card header={<ReportLeaveRegimeHeader setFilter={setFilter} />}>
            <ReportLeaveRegimeTable data={data} isLoading={isLoading} />
          </Card>
        </Tabs.Pane>

        <Tabs.Pane label={EREPORTS_TEXT.CHART} key={EREPORTS.CHART} >
          <ReportLeaveRegimeChart data={data} setFilter={setFilter} />
        </Tabs.Pane>
      </Tabs>
    </Flex>
  );
};

export default ReportLeaveRegime;
