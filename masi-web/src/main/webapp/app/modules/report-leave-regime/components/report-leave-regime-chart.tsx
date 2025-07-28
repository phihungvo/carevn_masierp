import React, { useEffect, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';

import useReports from 'app/hooks/use-reports';
import PieChart from 'app/components/chart/pie-chart';
import ButtonIcon from 'app/components/button-icon/button-icon';
import FilterDateMulti from 'app/components/filter-date-multi/filter-date-multi';
import { DATE_FORMAT } from 'app/constants/common';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { generateRandomHexColorWithAlpha } from 'app/shared/util/generate-random-hex';
import { ILeaveRegimeReport, ILeaveRegimeReportParams } from 'app/shared/model/report.model';

const { useGetLeaveRegimeReportsExcel } = useReports;

interface IReportLeaveRegimeChartProps {
  data: ILeaveRegimeReport[];
  setFilter: React.Dispatch<React.SetStateAction<ILeaveRegimeReportParams>>;
}

const ReportLeaveRegimeChart = (props: IReportLeaveRegimeChartProps) => {
  const { data, setFilter } = props;

  const datasets = data?.map(data => data.count);

  const randomColor = datasets?.map(() => generateRandomHexColorWithAlpha());

  const [date, setDate] = useState<DateObject[]>([]);
  const [isCheck, setIsCheck] = useState<boolean>(false);

  const datePickerFilterRef = useRef<DatePickerRef | null>(null);
  const fromDate = date?.[0]?.format(DATE_FORMAT.YEAR_DATE);
  const toDate = date?.[1] ? date?.[1]?.format(DATE_FORMAT.YEAR_DATE) : date?.[0]?.format(DATE_FORMAT.YEAR_DATE);

  const { trigger, data: dataFile } = useGetLeaveRegimeReportsExcel(fromDate, toDate);

  useEffect(() => {
    if (!isCheck) setIsCheck(true);
  }, [isCheck])

  const chartData = {
    labels: ['Nghỉ tang', 'Nghỉ bù', 'Nghỉ ốm', 'Nghỉ thai sản', 'Nghỉ cưới', 'Nghỉ không lương', 'Nghỉ phép năm'].sort(),
    datasets: [
      {
        label: 'Loại nghỉ phép',
        data: datasets,
        backgroundColor: !isCheck && randomColor?.map(c => c.backgroundColor),
        borderColor: !isCheck && randomColor?.map(c => c.borderColor),
        borderWidth: 1,
      },
    ],
  };

  const handleDownload = () => trigger();

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

  useDownloadXlsx(dataFile?.data, `leave-regime-reports${fromDate && `-${fromDate}`}${toDate && `-${toDate}`}`, 'xlsx');

  return (
    <div style={{ background: 'white', padding: 8 }}>
      <div className="card-header-container">
        <div className="card-header-extra" />
        <div className="card-header-extra">
          <FilterDateMulti
            value={date}
            setSelectedDate={setDate}
            ref={datePickerFilterRef}
            onReset={closeFilterCalendar}
            onOk={filterReportDates}
          />
          <ButtonIcon
            onClick={() => handleDownload()}
            icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
          />
        </div>
      </div>
      <div>
        <PieChart chartData={chartData} text="Loại nghỉ phép" width={500} height={500} />
      </div>
    </div>
  );
};

export default ReportLeaveRegimeChart;
