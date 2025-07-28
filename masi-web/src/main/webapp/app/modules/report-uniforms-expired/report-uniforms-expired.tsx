import './report-uniforms-expired.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import ReportUniformsExpiredHeader from './report-uniforms-expired-header';
import { DateObject } from 'react-multi-date-picker';
import { IUniformExpiringParams } from 'app/shared/model/report.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import ReportUniformsExpiredTable from './report-uniforms-expired-table';
import ReportUniformsExpiredFilterModal from 'app/modules/report-uniforms-expired/modals/report-uniforms-expired-filter-modal';
import { useModalReportUniformsExpired } from 'app/hooks/use-modal-report-uniforms-expired';
import ReportUniformsExpiredHistoryModal from 'app/modules/report-uniforms-expired/modals/report-uniforms-expired-history-modal';
import { EUniformStatus } from 'app/shared/model/enumerations/report-uniforms-expired';

const ReportUniformsExpired = () => {
  const [{ openModalFilter, toggleModalFilter }, { openModalHistory, toggleModalHistory }] = useModalReportUniformsExpired();
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);
  const [filter, setFilter] = useState<IUniformExpiringParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    type: EUniformStatus.UNALLOCATED,
  });

  const [employeeIds, setEmployeeIds] = useState<string>('');

  return (
    <div className='page_container'>
      <Typography level={4}>Báo cáo NV sắp đến hạn cấp đồng phục</Typography>
      <Card
        header={
          <ReportUniformsExpiredHeader
            selectedDate={selectedDate}
            setSelectedDate={setSelectedDate}
            toggleModalFilter={toggleModalFilter}
          />
        }
      >
        <ReportUniformsExpiredTable
          filter={filter}
          setFilter={setFilter}
          toggleModalHistory={toggleModalHistory}
          setEmployeeIds={setEmployeeIds}
        />
      </Card>

      <ReportUniformsExpiredHistoryModal isOpen={openModalHistory} toggle={toggleModalHistory} employeeIds={employeeIds} />

      <ReportUniformsExpiredFilterModal isOpen={openModalFilter} toggle={toggleModalFilter} setFilter={setFilter} />
    </div>
  );
};

export default ReportUniformsExpired;
