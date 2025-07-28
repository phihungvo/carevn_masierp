
import FilterDateMulti from 'app/components/filter-date-multi/filter-date-multi';
import { DATE_FORMAT } from 'app/constants/common';
import { IHrChangeReportParams } from 'app/shared/model/report.model';
import dayjs from 'dayjs';
import React, { useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { CardSubtitle } from 'reactstrap';

interface IHrChangeHeaderProps {
  setFilter: React.Dispatch<React.SetStateAction<IHrChangeReportParams>>;
}

const ReportHrChangeHeader = (props: IHrChangeHeaderProps) => {
  const { setFilter } = props;

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
      fromDate: dayjs(date[0]?.toDate()).startOf('month').format(DATE_FORMAT.YEAR_DATE),
      toDate: dayjs(date[1] ? date[1]?.toDate() : date[0].toDate() ).endOf('month').format(DATE_FORMAT.YEAR_DATE),
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

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
          onlyMonthPicker
        />
      </div>
    </>
  );
};

export default ReportHrChangeHeader;
