import dayjs from 'dayjs';
import Tabs from '@uiw/react-tabs';
import React, { useState } from 'react';

import Card from 'app/components/card/card';
import Flex from 'app/components/flex/flex';
import useReports from 'app/hooks/use-reports';
import ReportHrChangeTable from 'app/modules/report-hr-change/report-hr-change-table';
import ReportHrChangeHeader from 'app/modules/report-hr-change/report-hr-change-header';
import ReportHrChangeChart from 'app/modules/report-hr-change/components/report-hr-change-chart';
import { Typography } from 'app/components/typography/typography';
import { IHrChangeReportParams } from 'app/shared/model/report.model';
import { EREPORTS, EREPORTS_TEXT } from 'app/shared/model/enumerations/reports';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { DateObject } from 'react-multi-date-picker';

const { useGetHrChangeReports } = useReports;

const ReportHrChange = () => {
  const [filter, setFilter] = useState<IHrChangeReportParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const { data, isLoading } = useGetHrChangeReports(filter);
  const [year, setYear] = useState<number>(new DateObject().year);
  const { data: dataAll } = useGetHrChangeReports({ page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE_NAX, fromDate: year && dayjs(`${year}-01-01`).format('YYYY-MM-DD'), toDate: year && dayjs(`${year}-12-31`).format('YYYY-MM-DD') });

  return (
    <Flex className='page_container' direction="column" gap={16}>
      <Typography level={4}>Báo cáo biến động nhân sự</Typography>

      <Tabs type='card' activeKey={EREPORTS.DATATABLE}>
        <Tabs.Pane label={EREPORTS_TEXT.DATATABLE} key={EREPORTS.DATATABLE}>
          <Card header={<ReportHrChangeHeader setFilter={setFilter} />}>
            <ReportHrChangeTable
              data={data?.data}
              isLoading={isLoading}
              totalCount={data?.totalRecord || 0}
              filter={filter}
              setFilter={setFilter}
            />
          </Card>
        </Tabs.Pane>
        <Tabs.Pane label={EREPORTS_TEXT.CHART} key={EREPORTS.CHART}>
          <ReportHrChangeChart data={dataAll?.data} setYear={setYear} year={year} />:
        </Tabs.Pane>
      </Tabs>
    </Flex>
  );
};

export default ReportHrChange;
