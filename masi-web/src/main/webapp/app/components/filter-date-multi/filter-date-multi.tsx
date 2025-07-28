import './filter-date-multi.scss';
import { monthNames, weekDays } from 'app/constants/common';
import React, { MutableRefObject, forwardRef } from 'react';
import DatePicker, { DateObject } from 'react-multi-date-picker';
import { Button } from 'reactstrap';
import Flex from '../flex/flex';

interface IFilterDateMulti {
  setSelectedDate: (value: DateObject[]) => void;
  okText?: string;
  resetText?: string;
  onOk?: () => void;
  onReset?: () => void;
  value: DateObject[];
  onlyMonthPicker?: boolean;
  onlyYearPicker?: boolean;
}

const FilterDateMulti = forwardRef<any, IFilterDateMulti>((props, ref) => {
  const { setSelectedDate, okText = 'Áp dụng', resetText = 'Đặt lại', onOk, onReset, value, onlyMonthPicker, onlyYearPicker } = props;

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  return (
    <div className="filter">
      <Button className="btn-filter">
        Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter-icon" />
      </Button>
      <DatePicker
        value={value}
        weekDays={weekDays}
        months={monthNames}
        ref={ref as MutableRefObject<any>}
        onChange={onChangeSelectedDate}
        range
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

export default FilterDateMulti;
