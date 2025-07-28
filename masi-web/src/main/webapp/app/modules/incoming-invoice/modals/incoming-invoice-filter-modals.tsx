import DatePicker from 'app/components/date-picker/date-picker';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';
import { IIncomingInvoiceParams } from 'app/shared/model/incoming-invoice.model';
import React, { useCallback, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { Col, FormGroup, Label, Row } from 'reactstrap';

interface IIncomingInvoiceFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IIncomingInvoiceParams>>;
}

const IncomingInvoiceFilterModals = (props: IIncomingInvoiceFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;
  const datePickerRef = useRef<DatePickerRef | null>(null);

  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };
  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      startDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      endDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setSelectedDate([]);

    setFilter(prev => ({ ...prev, page: DEFAULT_PAGE, endDate: undefined, startDate: undefined }));
    toggle();
  };

  const closeExportCalendar = () => {
    setSelectedDate(null);
    datePickerRef?.current?.closeCalendar();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="invoice-filter-modals"
      style={{ width: '500px' }}
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
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
                range
                arrow={false}
                closeExportCalendar={closeExportCalendar}
                placeholder="Vui lòng chọn ngày"
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default IncomingInvoiceFilterModals;
