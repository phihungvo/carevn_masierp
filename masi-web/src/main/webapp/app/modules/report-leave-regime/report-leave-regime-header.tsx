import ButtonIcon from 'app/components/button-icon/button-icon';
import FilterDateMulti from 'app/components/filter-date-multi/filter-date-multi';
import AuthGuard from 'app/components/guards/auth-guard';
import { DATE_FORMAT } from 'app/constants/common';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useReports from 'app/hooks/use-reports';
import { ILeaveRegimeReportParams } from 'app/shared/model/report.model';
import React, { useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { CardSubtitle } from 'reactstrap';

const { useGetLeaveRegimeReportsExcel } = useReports;

interface IReportLeaveRegimeHeader {
  setFilter: React.Dispatch<React.SetStateAction<ILeaveRegimeReportParams>>;
}

const ReportLeaveRegimeHeader = (props: IReportLeaveRegimeHeader) => {
  const { setFilter } = props;

  const datePickerFilterRef = useRef<DatePickerRef | null>(null);

  const [date, setDate] = useState<DateObject[]>([]);

  const fromDate = date?.[0]?.format(DATE_FORMAT.YEAR_DATE);
  const toDate = date?.[1] ? date?.[1]?.format(DATE_FORMAT.YEAR_DATE) : date?.[0]?.format(DATE_FORMAT.YEAR_DATE);
  const { trigger, data } = useGetLeaveRegimeReportsExcel(fromDate, toDate);

  const closeFilterCalendar = () => {
    setDate([]);
    setFilter(prev => ({
      ...prev,
      fromDate: undefined,
      toDate: undefined,
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

  const filterReportDates = () => {
    setFilter(prev => ({
      ...prev,
      fromDate: date[0]?.format(DATE_FORMAT.YEAR_DATE),
      toDate: date[1] ? date[1]?.format(DATE_FORMAT.YEAR_DATE) : date[0]?.format(DATE_FORMAT.YEAR_DATE),
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

  const handleDownload = () => {
    trigger();
  };

  useDownloadXlsx(data?.data, `leave-regime-reports${fromDate && `-${fromDate}`}${toDate && `-${toDate}`}`, 'xlsx');

  return (
    <>
      <CardSubtitle className="card-header-subtitle"></CardSubtitle>
      <div className="card-header-extra">
        <FilterDateMulti
          value={date}
          setSelectedDate={setDate}
          ref={datePickerFilterRef}
          onReset={closeFilterCalendar}
          onOk={filterReportDates}
        />
       <AuthGuard permissionKey='REPORT_LEAVE_REGIME.EXPORT'>
          <ButtonIcon
            onClick={() => handleDownload()}
            icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
          />
       </AuthGuard>
      </div>
    </>
  );
};

export default ReportLeaveRegimeHeader;
