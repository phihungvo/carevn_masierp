import React, { useState } from 'react';
import { DateObject } from 'react-multi-date-picker';

import './report-uniforms-exports.scss';
import Flex from 'app/components/flex/flex';
import useReports from 'app/hooks/use-reports';
import ReportUniformsExportsBody from './report-uniforms-exports-body';
import ReportUniformDetailModals from './modals/report-uniform-detail-modals';
import ReportUniformExportsFilterModals from './modals/report-uniforms-exports-filter-modals';
import { Typography } from 'app/components/typography/typography';
import { IUniformImportParams } from 'app/shared/model/report.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsReportUniformExports } from 'app/hooks/use-modals-report';
import { UNIFORM_REPORT_TYPE } from 'app/shared/model/enumerations/uniform.model';

const { useGetUniformAllReports } = useReports;

export enum REPORT_TYPE {
  IMPORT = 'IMPORT',
  EXPORT = 'EXPORT',
  INVENTORY = 'INVENTORY',
}

const ReportUniformsExports = () => {
  const [{ openFilter, toggleFilter }, { openDetail, toggleDetail }] = useModalsReportUniformExports();

  const [selectedRecord, setSelectedRecord] = useState<string>();
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);
  const [reportType, setReportType] = useState<REPORT_TYPE>(REPORT_TYPE.IMPORT);
  const [type, setType] = useState<UNIFORM_REPORT_TYPE>();
  const [filter, setFilter] = useState<IUniformImportParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const { data, isLoading } = useGetUniformAllReports(filter);

  const totalCount = data?.totalRecord || 0;

  return (
    <Flex className='page_container' direction="column" gap={16}>
      <Typography level={4}>Báo cáo nhập xuất tồn đồng phục</Typography>

      <ReportUniformsExportsBody
        toggleFilter={toggleFilter}
        selectedDate={selectedDate}
        setSelectedDate={setSelectedDate}
        reportType={reportType}
        setReportType={setReportType}
        filter={filter}
        setFilter={setFilter}
        type={reportType}
        isLoading={isLoading}
        totalCount={totalCount}
        setSelectedRecord={setSelectedRecord}
        toggleDetail={toggleDetail}
        setType={setType}
        data={data?.data}
      />

      <ReportUniformExportsFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
        setSelectDate={setSelectedDate}
        reportType={reportType}
      />

      <ReportUniformDetailModals
        isOpen={openDetail}
        toggle={toggleDetail}
        selectedRecord={selectedRecord}
        selectedDate={selectedDate}
        type={type}
      />
    </Flex>
  );
};

export default ReportUniformsExports;
