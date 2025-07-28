import React, { useEffect, useState } from 'react';
import { DateObject } from 'react-multi-date-picker';

import Card from 'app/components/card/card';
import ReportRecruitmentTable from 'app/modules/report-recruitment/report-recruitment-table';
import ReportRecruitmentHeader from 'app/modules/report-recruitment/report-recruitment-header';
import { Typography } from 'app/components/typography/typography';
import { IRecruitmentReportParams } from 'app/shared/model/report.model';
import { DATE_FORMAT, DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';

const ReportRecruitment = () => {
  const [filter, setFilter] = useState<IRecruitmentReportParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);

  useEffect(() => {
    selectedDate &&
      setFilter(prev => ({
        ...prev,
        fromDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
        toDate: selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE),
      }));
  }, [selectedDate]);

  return (
    <div className='page_container'>
      <Typography level={4}>Báo cáo so sánh yêu cầu tuyển dụng và trạng thái tuyển dụng</Typography>

      <Card header={<ReportRecruitmentHeader setSelectedDate={setSelectedDate} selectedDate={selectedDate} />}>
        <ReportRecruitmentTable filter={filter} setFilter={setFilter} />
      </Card>
    </div>
  );
};

export default ReportRecruitment;
