import DatePicker from 'app/components/date-picker/date-picker';
import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { IUniformImportParams } from 'app/shared/model/report.model';
import React, { useCallback, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { REPORT_TYPE } from '../report-uniforms-exports';
import reportUniformsExportsMapping from '../report-uniforms-exports-mapping';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';

const { reportUniformsExportsTextMapping } = reportUniformsExportsMapping;

interface IReportUniformExportsFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IUniformImportParams>>;
  reportType: REPORT_TYPE;
  setSelectDate: React.Dispatch<React.SetStateAction<DateObject[]>>;
}

const ReportUniformExportsFilterModals = (props: IReportUniformExportsFilterModalsProps) => {
  const { isOpen, toggle, setFilter, reportType, setSelectDate } = props;

  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);
  const [type, setType] = useState<string>();

  const datePickerRef = useRef<DatePickerRef | null>(null);

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const closeExportCalendar = () => {
    setSelectedDate(null)
    datePickerRef?.current?.closeCalendar()
  }

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onOk = () => {
    setSelectDate(selectedDate);
    setFilter(prev => ({
      ...prev,
      fromDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      toDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      type,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setSelectDate([]);
    setSelectedDate([]);
    setType(undefined);
    setFilter(prev => ({
      ...prev,
      fromDate: undefined,
      toDate: undefined,
      type: undefined,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancelText="Đặt lại"
      okText="Áp dụng"
      className="modals-filter-reports-uniform-exports"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Row>
        <Col md={6}>
          <FormGroup>
            <Label htmlFor="date">Ngày</Label>
            <DatePicker
              value={selectedDate}
              ref={datePickerRef}
              onChange={onChangeSelectedDate}
              id="date"
              name="date"
              arrow={false}
              range
              closeExportCalendar={closeExportCalendar}
            />
          </FormGroup>
        </Col>

        {reportType === REPORT_TYPE.EXPORT && (
          <Col md={6}>
            <FormGroup>
              <Label htmlFor="exportType">Loại xuất</Label>
              <Input id="exportType" name="exportType" type="select" onChange={e => setType(e.target.value)} value={type}>
                <option selected disabled>
                  Chọn loại xuất
                </option>
                <option value="SALE">{reportUniformsExportsTextMapping('SALE')}</option>
                <option value="SUPPORT">{reportUniformsExportsTextMapping('SUPPORT')}</option>
                <option value="SENIORITY">{reportUniformsExportsTextMapping('SENIORITY')}</option>
                <option value="OTHER">{reportUniformsExportsTextMapping('OTHER')}</option>
              </Input>
            </FormGroup>
          </Col>
        )}
      </Row>
    </Modal>
  );
};

export default ReportUniformExportsFilterModals;
