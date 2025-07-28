import DatePicker from 'app/components/date-picker/date-picker';
import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import Select from 'app/components/select/select';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';
import useEmployee from 'app/hooks/use-employee';
import { ICustomerParams } from 'app/shared/model/customer.model';
import { BaseOption } from 'app/shared/model/pagination.model';
import React, { useCallback, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { FormGroup, Label } from 'reactstrap';
import { generateBirthdayDate } from '../check-highlight-birthday';

const { useGetEmployeesQuery } = useEmployee;

interface ICustomersFilterModals {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ICustomerParams>>;
}

const CustomersFilterModals = (props: ICustomersFilterModals) => {
  const { isOpen, toggle, setFilter } = props;

  const [listEmployeeOwner, setListEmployeeOwner] = useState<string[]>([]);
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([]);
  const [month, setMonth] = useState<string>();

  const datePickerRef = useRef<DatePickerRef | null>(null);

  const { data: employeeData, isLoading } = useGetEmployeesQuery();

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      contractFrom: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      contractTo: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      birthdayFrom: month ? generateBirthdayDate(Number(month)).birthdayFrom : undefined,
      birthdayTo: month ? generateBirthdayDate(Number(month)).birthdayTo : undefined,
      listEmployeeOwner,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setSelectedDate([]);
    setListEmployeeOwner([]);
    setMonth(undefined);
    setFilter(prev => ({
      ...prev,
      contractFrom: '',
      contractTo: '',
      listEmployeeOwner: undefined,
      birthdayFrom: undefined,
      birthdayTo: undefined,
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
      cancelText="Đặt lại"
      okText="Áp dụng"
      className="modals-filter-customers"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Label htmlFor="contractSigned">Ngày tạo</Label>
        <DatePicker
          value={selectedDate}
          ref={datePickerRef}
          onChange={onChangeSelectedDate}
          id="contractSigned"
          name="contractSigned"
          range
          arrow={false}
          closeExportCalendar={closeExportCalendar}
        />
      </FormGroup>

      <FormGroup>
        <Label htmlFor="customerOwner">Người phụ trách</Label>
        <Select<BaseOption, true>
          id="customerOwner"
          name="customerOwner"
          placeholder="Chọn người phụ trách"
          onChange={value => setListEmployeeOwner(value?.map(item => item?.value as string))}
          value={listEmployeeOwner.map(item => {
            const findEmp = employeeData?.data?.find(e => e?.id === item);
            return { label: (findEmp?.lastName || '') + ' ' + (findEmp?.firstName || ''), value: item };
          })}
          options={employeeData?.data?.map(e => ({
            label: `${e?.lastName || ''} ${e?.firstName || ''}`,
            value: e?.id,
          }))}
          isMulti
          isLoading={isLoading}
        />
      </FormGroup>

      <FormGroup>
        <Label htmlFor="birthday">Tháng sinh nhật</Label>
        <Input value={month} onChange={e => setMonth(e.target.value)} name="birthday" id="birthday" type="select">
          <option selected disabled>
            Chọn tháng sinh nhật
          </option>
          <option value="1">Tháng 1</option>
          <option value="2">Tháng 2</option>
          <option value="3">Tháng 3</option>
          <option value="4">Tháng 4</option>
          <option value="5">Tháng 5</option>
          <option value="6">Tháng 6</option>
          <option value="7">Tháng 7</option>
          <option value="8">Tháng 8</option>
          <option value="9">Tháng 9</option>
          <option value="10">Tháng 10</option>
          <option value="11">Tháng 11</option>
          <option value="12">Tháng 12</option>
        </Input>
      </FormGroup>
    </Modal>
  );
};

export default CustomersFilterModals;
