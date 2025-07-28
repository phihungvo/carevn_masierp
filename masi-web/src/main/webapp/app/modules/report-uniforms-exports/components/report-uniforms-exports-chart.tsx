import dayjs from 'dayjs';
import { Button } from 'reactstrap';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import DatePicker, { DateObject, DatePickerRef } from 'react-multi-date-picker';

import Flex from 'app/components/flex/flex';
import useReports from 'app/hooks/use-reports';
import BarChart from 'app/components/chart/bar-chart';
import ButtonIcon from 'app/components/button-icon/button-icon';
import FilterDateMulti from 'app/components/filter-date-multi/filter-date-multi';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { DATE_FORMAT, weekDays } from 'app/constants/common';
import { IUniformImportParams } from 'app/shared/model/report.model';
import { UNIFORM_REPORT_TYPE } from 'app/shared/model/enumerations/uniform.model';

const { useGetUniformAllReports } = useReports;
const { useGetUniformExportReportExcel } = useReports

interface IReportUniformsExportsChartProps {
  filter: IUniformImportParams;
  setFilter: React.Dispatch<React.SetStateAction<IUniformImportParams>>;
}

const ReportUniformsExportChart = (props: IReportUniformsExportsChartProps) => {
  const { filter, setFilter } = props;

  const [date, setDate] = useState<DateObject[]>();
  const [exportDate, setExportDate] = useState<DateObject[]>([new DateObject(), new DateObject()]);

  const datePickerFilterRef = useRef<DatePickerRef | null>(null);
  const datePickerExportRef = useRef<DatePickerRef | null>(null);

  const startExportDate = exportDate?.[0]?.format(DATE_FORMAT.YEAR_DATE);
  const endExportDate = exportDate?.[1] ? exportDate?.[1]?.format(DATE_FORMAT.YEAR_DATE) : exportDate?.[0]?.format(DATE_FORMAT.YEAR_DATE);

  const { data } = useGetUniformAllReports({ fromDate: filter?.fromDate, toDate: filter?.toDate });

  const dataMap = useMemo(() => {
    const map = new Map<string, Record<UNIFORM_REPORT_TYPE, number>>();
    // This code is using the `useMemo` hook from React to create a memoized version of a data map. The `useMemo` hook is used to optimize performance by only re-computing the data map when the `data` prop changes.

    // First, a new Map object is created. The keys of this map are strings, and the values are Records with keys of type `UNIFORM_REPORT_TYPE` and values of type `number`.
    //
    // Then, for each item in the `data` array, the code does the following:
    //
    // 1. It gets the `name` property of the item and uses it as a key.
    // 2. It tries to get the value from the map using the key. If the key does not exist in the map, `map.get(key)` will return `undefined`.
    // 3. It checks the `type` property of the item and based on its value, it assigns the `total` property of the item to one of `releaseTotal`, `stockedTotal`, or `stockTotal`. If the `type` does not match any of the `UNIFORM_REPORT_TYPE` values, it assigns `0`.
    //
    // Next, it uses `map.set` to add a new entry to the map. If a value for the key already exists in the map, it creates a new object with the existing values plus the new totals. If a value for the key does not exist, it creates a new object with the new totals.
    //
    // Finally, it transforms the map into an array of objects with properties `name`, `RELEASE`, `STOCKED`, and `STOCK`. This is done using `Array.from(map.entries())` to convert the map into an array of `[key, value]` pairs, and then `map()` to transform each pair into an object.
    //
    // The resulting `dataMap` is a memoized value, meaning it will only be re-computed when the `data` prop changes. This can be a significant performance optimization if computing the data map is a computationally expensive operation.

    data?.data?.forEach(item => {
      const key = item?.name;
      const value = map.get(key);
      const releaseTotal = item?.type === UNIFORM_REPORT_TYPE.RELEASE ? item?.total : 0;
      const stockedTotal = item?.type === UNIFORM_REPORT_TYPE.STOCKED ? item?.total : 0;
      const stockTotal = item?.type === UNIFORM_REPORT_TYPE.STOCK ? item?.total : 0;

      map.set(
        key,
        value
          ? {
            [UNIFORM_REPORT_TYPE.RELEASE]: value?.RELEASE + releaseTotal,
            [UNIFORM_REPORT_TYPE.STOCKED]: value?.STOCKED + stockedTotal,
            [UNIFORM_REPORT_TYPE.STOCK]: value?.STOCK + stockTotal,
          }
          : {
            [UNIFORM_REPORT_TYPE.RELEASE]: releaseTotal,
            [UNIFORM_REPORT_TYPE.STOCKED]: stockedTotal,
            [UNIFORM_REPORT_TYPE.STOCK]: stockTotal,
          },
      );
    });
    return Array.from(map.entries()).map(([key, value]) => ({
      name: key,
      RELEASE: value?.RELEASE,
      STOCKED: value?.STOCKED,
      STOCK: value?.STOCK,
    }));
  }, [data]);

  const chartData = {
    labels: dataMap?.map(item => item?.name) || [],
    datasets: [
      {
        label: 'Xuất kho',
        data: dataMap?.map(item => item?.RELEASE) || [],
        parsing: {
          yAxisKey: UNIFORM_REPORT_TYPE.RELEASE,
        },
        backgroundColor: '#ffb3c2',
        maxBarThickness: 48,
        borderWidth: 1,
        borderRadius: 4,
      },
      {
        label: 'Nhập kho',
        data: dataMap?.map(item => item?.STOCKED) || [],
        parsing: {
          yAxisKey: UNIFORM_REPORT_TYPE.STOCKED,
        },
        backgroundColor: '#a0d0f6',
        maxBarThickness: 48,
        borderWidth: 1,
        borderRadius: 4,
      },
      {
        label: 'Tồn kho',
        data: dataMap?.map(item => item?.STOCK) || [],
        parsing: {
          yAxisKey: UNIFORM_REPORT_TYPE.STOCK,
        },
        backgroundColor: '#ffe6ad',
        maxBarThickness: 48,
        borderWidth: 1,
        borderRadius: 4,
      },
    ],
  };

  const closeFilterCalendar = () => {
    setDate(null);
    setFilter(null)
    datePickerFilterRef.current?.closeCalendar();
  };

  const filterReportDates = () => {
    setFilter(prev => ({
      ...prev,
      fromDate: dayjs(date[0]?.toDate()).startOf('month').format(DATE_FORMAT.YEAR_DATE),
      toDate: dayjs(date[1] ? date[1]?.toDate() : date[0].toDate()).endOf('month').format(DATE_FORMAT.YEAR_DATE),
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

  // call data file
  const { trigger, data: dataFile } = useGetUniformExportReportExcel(startExportDate, endExportDate)

  const closeExportCalendar = () => datePickerFilterRef.current?.closeCalendar();

  const onExportTimeKeepingData = () => {
    trigger()
    closeExportCalendar();
  };

  const onChangeExportDate = (value: DateObject[]) => setExportDate(value);

  // hook download xlsx
  useDownloadXlsx(dataFile?.data,
    `${startExportDate ? dayjs(startExportDate).format(DATE_FORMAT.DATE) : ''}${endExportDate && endExportDate ?
      '-' : ''}${endExportDate ? dayjs(endExportDate).format(DATE_FORMAT.DATE) : ''}`, 'xlsx')

  return (
    <div style={{ background: 'white', padding: 8 }}>
      <div className="card-header-container">
        <div className="card-header-extra" />
        <div className="card-header-extra">
          <FilterDateMulti
            value={date || undefined}
            setSelectedDate={setDate}
            ref={datePickerFilterRef}
            onReset={closeFilterCalendar}
            onOk={filterReportDates}
            onlyMonthPicker
          />
          <div className="export-timesheet">
            <ButtonIcon
              icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
            />
            <DatePicker value={exportDate} weekDays={weekDays} ref={datePickerExportRef} onChange={onChangeExportDate} range arrow={false}>
              <Flex align="center" justify="center" gap={16} style={{ paddingTop: 36, paddingBottom: 16 }}>
                <Button outline type="button" onClick={closeExportCalendar}>
                  Huỷ
                </Button>
                <Button color="primary" type="button" onClick={onExportTimeKeepingData}>
                  Xuất excel
                </Button>
              </Flex>
            </DatePicker>
          </div>
        </div>
      </div>
      <BarChart chartData={chartData} title="Biểu đồ xuất nhập tồn đồng phục" />
    </div>
  );
};

export default ReportUniformsExportChart;
