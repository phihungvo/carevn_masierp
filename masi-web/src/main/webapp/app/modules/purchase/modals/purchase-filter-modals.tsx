import DatePicker from 'app/components/date-picker/date-picker';
import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';
import { PURCHASE_STATUS } from 'app/shared/model/enumerations/purchase.model';
import { IPurchaseParams } from 'app/shared/model/purchase.model';
import React, { useCallback, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { FormGroup, Label } from 'reactstrap';
import purchaseMapping from '../purchase-mapping';

const { purchaseStatusTextMapping } = purchaseMapping;

interface IPurchaseFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IPurchaseParams>>;
}

const PurchaseFilterModals = (props: IPurchaseFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);
  const [statuses, setStatuses] = useState<PURCHASE_STATUS[]>([]);

  const datePickerRef = useRef<DatePickerRef | null>(null);

  // const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: PURCHASE_STATUS) => {
  //   if (e.target.checked && e.target.value === value) {
  //     setStatuses(prev => [...prev, value]);
  //   } else {
  //     setStatuses(prev => prev.filter(item => item !== value));
  //   }
  // };

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const closeExportCalendar = () => {
    setSelectedDate(null);
    datePickerRef.current?.closeCalendar();
  };

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      statuses,
      createdFrom: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      createdTo: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setStatuses([]);
    setSelectedDate([]);
    setFilter(prev => ({
      ...prev,
      statuses: [],
      createdFrom: '',
      createdTo: '',
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
      className="modals-filter-purchases"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Typography level={5}>Bộ lọc</Typography>

      <FormGroup>
        <Label for="status">Trạng thái:</Label>
        <Input id="status" name="status" type="select" onChange={e => setStatuses([e.target.value as PURCHASE_STATUS])} value={statuses[0]}>
          <option selected disabled>
            Chọn trạng thái
          </option>
          <option value={PURCHASE_STATUS.NEW}>{purchaseStatusTextMapping(PURCHASE_STATUS.NEW)}</option>
          <option value={PURCHASE_STATUS.WAITING_APPROVAL}>{purchaseStatusTextMapping(PURCHASE_STATUS.WAITING_APPROVAL)}</option>
          <option value={PURCHASE_STATUS.APPROVED}>{purchaseStatusTextMapping(PURCHASE_STATUS.APPROVED)}</option>
          <option value={PURCHASE_STATUS.REJECTED}>{purchaseStatusTextMapping(PURCHASE_STATUS.REJECTED)}</option>
          <option value={PURCHASE_STATUS.DELIVERING}>{purchaseStatusTextMapping(PURCHASE_STATUS.DELIVERING)}</option>
          <option value={PURCHASE_STATUS.FINISHED}>{purchaseStatusTextMapping(PURCHASE_STATUS.FINISHED)}</option>
        </Input>
        {/* <Row>
          <Col md={4}>
            <FormGroup check>
              <Label check for="new">
                {purchaseStatusTextMapping(PURCHASE_STATUS.NEW)}
              </Label>
              <Input
                id="new"
                name="new"
                type="checkbox"
                value={PURCHASE_STATUS.NEW}
                checked={statuses.includes(PURCHASE_STATUS.NEW)}
                onChange={e => onChangeStatus(e, PURCHASE_STATUS.NEW)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="pending">
                {purchaseStatusTextMapping(PURCHASE_STATUS.WAITING_APPROVAL)}
              </Label>
              <Input
                id="pending"
                name="pending"
                type="checkbox"
                value={PURCHASE_STATUS.WAITING_APPROVAL}
                checked={statuses.includes(PURCHASE_STATUS.WAITING_APPROVAL)}
                onChange={e => onChangeStatus(e, PURCHASE_STATUS.WAITING_APPROVAL)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="approved">
                {purchaseStatusTextMapping(PURCHASE_STATUS.APPROVED)}
              </Label>
              <Input
                id="approved"
                name="approved"
                type="checkbox"
                value={PURCHASE_STATUS.APPROVED}
                checked={statuses.includes(PURCHASE_STATUS.APPROVED)}
                onChange={e => onChangeStatus(e, PURCHASE_STATUS.APPROVED)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="delivery">
                {purchaseStatusTextMapping(PURCHASE_STATUS.DELIVERING)}
              </Label>
              <Input
                id="delivery"
                name="delivery"
                type="checkbox"
                value={PURCHASE_STATUS.DELIVERING}
                checked={statuses.includes(PURCHASE_STATUS.DELIVERING)}
                onChange={e => onChangeStatus(e, PURCHASE_STATUS.DELIVERING)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="completed">
                {purchaseStatusTextMapping(PURCHASE_STATUS.FINISHED)}
              </Label>
              <Input
                id="completed"
                name="completed"
                type="checkbox"
                value={PURCHASE_STATUS.FINISHED}
                checked={statuses.includes(PURCHASE_STATUS.FINISHED)}
                onChange={e => onChangeStatus(e, PURCHASE_STATUS.FINISHED)}
              />
            </FormGroup>
          </Col>

          <Col md={4}>
            <FormGroup check>
              <Label check for="rejected">
                {purchaseStatusTextMapping(PURCHASE_STATUS.REJECTED)}
              </Label>
              <Input
                id="rejected"
                name="rejected"
                type="checkbox"
                value={PURCHASE_STATUS.REJECTED}
                checked={statuses.includes(PURCHASE_STATUS.REJECTED)}
                onChange={e => onChangeStatus(e, PURCHASE_STATUS.REJECTED)}
              />
            </FormGroup>
          </Col>
        </Row> */}
      </FormGroup>

      <FormGroup>
        <Label>Ngày tạo</Label>
        <DatePicker
          value={selectedDate}
          ref={datePickerRef}
          onChange={onChangeSelectedDate}
          name="date"
          range
          arrow={false}
          closeExportCalendar={closeExportCalendar}
        />
      </FormGroup>
    </Modal>
  );
};

export default PurchaseFilterModals;
