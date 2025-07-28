import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import ReportEmployeeExpiringContractTable from './report-employee-expiring-contract-table';
import ReportEmployeeExpiringContractHeader from './report-employee-expiring-contract-header';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IEmployeeExpiringContractParams } from 'app/shared/model/report.model';

const ReportEmployeeExpiringContract = () => {
  const [filter, setFilter] = useState<IEmployeeExpiringContractParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });
  return (
    <div className='page_container'>
      <Typography level={4}>Báo cáo theo dõi NV sắp hết hạn HĐ</Typography>

      <Card header={<ReportEmployeeExpiringContractHeader />}>
        <ReportEmployeeExpiringContractTable filter={filter} setFilter={setFilter} />
      </Card>
    </div>
  );
};

export default ReportEmployeeExpiringContract;
