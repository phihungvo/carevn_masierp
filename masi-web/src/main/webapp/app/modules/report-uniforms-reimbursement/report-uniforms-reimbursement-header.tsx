
import FilterDate from 'app/components/filter-date-multi/filter-date-multi';
import { DATE_FORMAT } from 'app/constants/common';
import { IUniformSupportParams } from 'app/shared/model/report.model';
import { getStartAndEndOfWeek } from 'app/shared/util/date-utils';
import React, { useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { CardSubtitle } from 'reactstrap';

interface IReportUniformsReimbursementHeader {
  setFilter: React.Dispatch<React.SetStateAction<IUniformSupportParams>>;
}

const ReportUniformsReimbursementHeader = (props: IReportUniformsReimbursementHeader) => {
  const { setFilter } = props;
  const { startOfWeek, endOfWeek } = getStartAndEndOfWeek(new Date());

  const datePickerFilterRef = useRef<DatePickerRef | null>(null);

  const [date, setDate] = useState<DateObject[]>([]);

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

  return (
    <>
      <CardSubtitle className="card-header-subtitle">
        {/* Tuần: {date?.[0]?.format(DATE_FORMAT.DATE) || startOfWeek} - {date?.[1]?.format(DATE_FORMAT.DATE) || endOfWeek} */}
      </CardSubtitle>
      <div className="card-header-extra">
        <FilterDate
          value={date}
          setSelectedDate={setDate}
          ref={datePickerFilterRef}
          onReset={closeFilterCalendar}
          onOk={filterReportDates}
        />
      </div>
    </>
  );
};

export default ReportUniformsReimbursementHeader;
