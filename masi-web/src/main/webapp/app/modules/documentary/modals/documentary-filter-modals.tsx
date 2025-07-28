import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React, { useCallback, useRef, useState } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import useOnClickOutside from 'app/hooks/use-click-outside';
import DatePicker from 'app/components/date-picker/date-picker';
import { DOCUMENTARY_GROUP, DOCUMENTARY_TYPE } from 'app/shared/model/enumerations/documentary';
import { IDocumentaryParams } from 'app/shared/model/documentary.model';
import documentaryMapping from '../documentary-mapping';

const { documentaryGroupMapping, documentaryTypeMapping } = documentaryMapping;

interface IDocumentaryFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IDocumentaryParams>>;
}

const DocumentaryFilterModals = (props: IDocumentaryFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const datePickerRef = useRef<DatePickerRef | null>(null);

  const [type, setType] = useState<DOCUMENTARY_TYPE>();
  const [group, setGroup] = useState<DOCUMENTARY_GROUP>();
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      documentDateFrom: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      documentDateTo: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      documentaryType: type,
      documentaryGroup: group,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setSelectedDate([]);
    setType(undefined);
    setGroup(undefined);
    setFilter(prev => ({
      ...prev,
      documentaryGroup: undefined,
      documentaryType: undefined,
      documentDateFrom: undefined,
      documentDateTo: undefined,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const closeExportCalendar = () => {
    setSelectedDate(null)
    datePickerRef?.current?.closeCalendar()
  }

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="orders-filter-modals"
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
              />
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup>
              <Label for="type">Loại</Label>
              <Input name="type" id="type" type="select" value={type} onChange={e => setType(e.target.value as DOCUMENTARY_TYPE)}>
                <option selected disabled>
                  Chọn loại
                </option>
                <option value={DOCUMENTARY_TYPE.ANNOUNCEMENT}>{documentaryTypeMapping(DOCUMENTARY_TYPE.ANNOUNCEMENT)}</option>
                <option value={DOCUMENTARY_TYPE.DOCUMENTARY}>{documentaryTypeMapping(DOCUMENTARY_TYPE.DOCUMENTARY)}</option>
                <option value={DOCUMENTARY_TYPE.RESPONSE}>{documentaryTypeMapping(DOCUMENTARY_TYPE.RESPONSE)}</option>
              </Input>
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup>
              <Label for="group">Nhóm</Label>
              <Input name="group" id="group" type="select" value={group} onChange={e => setGroup(e.target.value as DOCUMENTARY_GROUP)}>
                <option selected disabled>
                  Chọn nhóm
                </option>
                <option value={DOCUMENTARY_GROUP.INTERNAL}>{documentaryGroupMapping(DOCUMENTARY_GROUP.INTERNAL)}</option>
                <option value={DOCUMENTARY_GROUP.OUTGOING}>{documentaryGroupMapping(DOCUMENTARY_GROUP.OUTGOING)}</option>
                <option value={DOCUMENTARY_GROUP.INCOMING}>{documentaryGroupMapping(DOCUMENTARY_GROUP.INCOMING)}</option>
              </Input>
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default DocumentaryFilterModals;
