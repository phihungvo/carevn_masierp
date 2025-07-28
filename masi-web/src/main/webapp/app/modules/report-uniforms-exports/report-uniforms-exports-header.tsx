import dayjs from 'dayjs';
import React, { useRef, useState } from 'react';
import DatePicker, { DateObject, DatePickerRef } from 'react-multi-date-picker';

import Flex from 'app/components/flex/flex';
import useReports from 'app/hooks/use-reports'
import Button from 'app/components/button/button';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { REPORT_TYPE } from './report-uniforms-exports';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { DATE_FORMAT, weekDays } from 'app/constants/common';
import AuthGuard from 'app/components/guards/auth-guard';

interface IReportUniformsExportsHeader {
  toggleFilter: () => void;
  selectedDate: DateObject[];
  setSelectedDate: (value: DateObject[]) => void;
  reportType: REPORT_TYPE;
  setReportType: (value: REPORT_TYPE) => void;
}

const { useGetUniformExportReportExcel } = useReports

const ReportUniformsExportsHeader = (props: IReportUniformsExportsHeader) => {
  const { toggleFilter, selectedDate, setSelectedDate, reportType, setReportType } = props;

  const [exportDate, setExportDate] = useState<DateObject[]>([new DateObject(), new DateObject()]);

  const datePickerExportRef = useRef<DatePickerRef | null>(null);

  const startExportDate = exportDate[0]?.format(DATE_FORMAT.YEAR_DATE);
  const endExportDate = exportDate[1] ? exportDate[1]?.format(DATE_FORMAT.YEAR_DATE) : exportDate[0]?.format(DATE_FORMAT.YEAR_DATE);

  const { trigger, data } = useGetUniformExportReportExcel(startExportDate, endExportDate)

  const onChangeExportDate = (value: DateObject[]) => setExportDate(value);

  const closeExportCalendar = () => {
    datePickerExportRef.current?.closeCalendar();
  };

  const onExportTimeKeepingData = () => {
    trigger()
    closeExportCalendar();
  };

  useDownloadXlsx(data?.data,
    `${startExportDate ? dayjs(startExportDate).format(DATE_FORMAT.DATE) : ''}${endExportDate && endExportDate ? '-' : ''}${endExportDate ? dayjs(endExportDate).format(DATE_FORMAT.DATE) : ''}`, 'xlsx')

  return (

    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
        <div className="export-timesheet">
          <ButtonIcon
            icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
          />
          <DatePicker value={exportDate} weekDays={weekDays} ref={datePickerExportRef} onChange={onChangeExportDate} range arrow={false}>
            <Flex align="center" justify="center" gap={16} style={{ paddingTop: 36, paddingBottom: 16 }}>
              <Button outline type="button" onClick={closeExportCalendar}>
                Huỷ
              </Button>
              <AuthGuard permissionKey='REPORT_UNIFORMS_EXPORTS.EXPORT'>
                <Button color="primary" type="button" onClick={onExportTimeKeepingData}>
                  Xuất excel
                </Button>
              </AuthGuard>
            </Flex>
          </DatePicker>
        </div>
      </div>
    </div>
  );
};

export default ReportUniformsExportsHeader;
