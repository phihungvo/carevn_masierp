import DateObject from 'react-date-object';
import DatePicker from 'react-multi-date-picker';
import { Button } from 'reactstrap';
import React, { MutableRefObject, forwardRef } from 'react';

import './filter-date.scss';
import Flex from '../flex/flex';
import { monthNames, weekDays } from 'app/constants/common';
interface IFilterDate {
  setSelectedDate: (value: DateObject) => void;
  okText?: string;
  resetText?: string;
  onOk?: () => void;
  onReset?: () => void;
  value: DateObject;
  onlyMonthPicker?: boolean;
  display?: string;
  onlyYearPicker?: boolean;
}

const FilterDate = forwardRef<any, IFilterDate>((props, ref) => {
  const { setSelectedDate, okText = 'Áp dụng', resetText = 'Đặt lại', onOk, onReset, value, onlyMonthPicker, display = 'Lọc', onlyYearPicker } = props;

  const onChangeSelectedDate = (value: DateObject) => setSelectedDate(value);

  return (
    <div className="filter">
      <Button className="btn-filter">
        {display} <img src="content/images/vuesax/linear/sort.svg" alt="filter-icon" />
      </Button>
      <DatePicker
        value={value}
        weekDays={weekDays}
        months={monthNames}
        ref={ref as MutableRefObject<any>}
        onChange={onChangeSelectedDate}
        arrow={false}
        onlyMonthPicker={onlyMonthPicker}
        onlyYearPicker={onlyYearPicker}
      >
        <Flex align="center" justify="center" gap={16} style={{ paddingTop: 36, paddingBottom: 16 }}>
          <Button outline={true} type="button" onClick={onReset}>
            {resetText}
          </Button>
          <Button color="primary" type="button" onClick={onOk}>
            {okText}
          </Button>
        </Flex>
      </DatePicker>
    </div>
  );
});

export default FilterDate;
