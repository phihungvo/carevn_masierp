import React, { useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';

import ButtonIcon from 'app/components/button-icon/button-icon';
import useReports from 'app/hooks/use-reports';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { DATE_FORMAT } from 'app/constants/common';
import FilterDateMulti from 'app/components/filter-date-multi/filter-date-multi';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetRecruitmentReportsExcel } = useReports;

interface IReportRecruitmentHeaderProps {
  setSelectedDate: (value: DateObject[]) => void;
  selectedDate?: DateObject[];
}

const ReportRecruitmentHeader = ({ setSelectedDate, selectedDate }: IReportRecruitmentHeaderProps) => {
  const [date, setDate] = useState<DateObject[]>([new DateObject(), new DateObject()]);

  const datePickerFilterRef = useRef<DatePickerRef | null>(null);

  const fromDate = selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE);
  const toDate = selectedDate?.[1] ? selectedDate?.[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE);
  const { trigger, data } = useGetRecruitmentReportsExcel(fromDate, toDate);

  const closeFilterCalendar = () => {
    setDate([]);
    setSelectedDate([]);
    datePickerFilterRef.current?.closeCalendar();
  };

  const filterTimeKeep = () => {
    setSelectedDate(date);
    datePickerFilterRef.current?.closeCalendar();
  };

  const handleDownload = () => {
    trigger();
  };

  useDownloadXlsx(data?.data, `recruitment-request-reports${fromDate && `-${fromDate}`}${toDate && `-${toDate}`}`, 'xlsx');

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
        <FilterDateMulti value={date} setSelectedDate={setDate} ref={datePickerFilterRef} onReset={closeFilterCalendar} onOk={filterTimeKeep} />
        <AuthGuard permissionKey='REPORT_RECRUITMENT.EXPORT'>
          <ButtonIcon
            onClick={() => handleDownload()}
            icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
          />
        </AuthGuard>
      </div>
    </div>
  );
};

export default ReportRecruitmentHeader;
