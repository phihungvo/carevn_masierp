import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import React, { useEffect, useMemo, useRef, useState } from 'react';

import BarChart from 'app/components/chart/bar-chart';
import FilterDate from 'app/components/filter-date/filter-date';
import { DATE_FORMAT } from 'app/constants/common';
import { IHrChangeReport } from 'app/shared/model/report.model';

interface IReportHrChangeChartProps {
  data: IHrChangeReport[];
  year: number;
  setYear: React.Dispatch<React.SetStateAction<number>>;
}

const ReportHrChangeChart = (props: IReportHrChangeChartProps) => {
  const { data, setYear } = props;

  const dataMap = useMemo(() => {
    const defaultMonths = Array.from({ length: 12 }, (_, i) => ({
      monthNumber: i + 1, // tháng từ 1 đến 12
      total: { leave: 0, join: 0 }, // mặc định là 0 cho cả leave và join
    }));

    data?.forEach(item => {
      const month = item?.month?.split('-')[1];
      if (!month) return;

      const monthNumber = parseInt(month);
      const currentMonthData = defaultMonths[monthNumber - 1]?.total || { leave: 0, join: 0 };
      currentMonthData.leave += item?.totalLeave || 0;
      currentMonthData.join += item?.totalJoin || 0;
    });

    return defaultMonths;
  }, [data]);

  // const randomColor = dataMap?.map(() => generateRandomHexColorWithAlpha());

  const chartData = {
    labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
    datasets: [
      {
        label: 'Đã nghỉ',
        data: dataMap?.map(data => data?.total?.leave) || [],
        backgroundColor: '#ffb3c2',
        maxBarThickness: 20,
        borderWidth: 1,
        borderColor: '#ffb3c2',
        borderRadius: 4,
      },
      {
        label: 'Mới',
        data: dataMap?.map(data => data?.total?.join) || [],
        backgroundColor: '#a0d0f6',
        maxBarThickness: 20,
        borderWidth: 1,
        borderColor: '#a0d0f6',
        borderRadius: 4,
      },
    ],
  };


  const datePickerFilterRef = useRef<DatePickerRef | null>(null);

  const [date, setDate] = useState<DateObject>();

  useEffect(() => {
    if (date && date.isValid) datePickerFilterRef.current?.openCalendar();
  }, [date])

  const closeFilterCalendar = () => {
    setDate(null);
    setYear(null);
    datePickerFilterRef.current?.closeCalendar();
  };

  const filterReportDates = () => {
    setYear(Number(date?.format(DATE_FORMAT.YEAR_DATE)?.split('-')[0]));
    datePickerFilterRef.current?.closeCalendar();
  };

  return (
    <div style={{ background: 'white', padding: 8 }}>
      <div className="card-header-container">
        <div className="card-header-extra" />
        <div className="card-header-extra">
          <FilterDate
            value={date || undefined}
            setSelectedDate={setDate}
            ref={datePickerFilterRef}
            onReset={closeFilterCalendar}
            onOk={filterReportDates}
            onlyYearPicker
          />
        </div>
      </div>
      <BarChart chartData={chartData} />
    </div>
  );
};

export default ReportHrChangeChart;
