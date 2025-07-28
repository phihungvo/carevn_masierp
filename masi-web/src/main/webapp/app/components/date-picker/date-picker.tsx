import { Button, FormGroup, InputProps, Label } from 'reactstrap';
import React, { MutableRefObject, forwardRef } from 'react';
import DatePickerLib, {
  CalendarProps,
  DatePickerProps,
} from 'react-multi-date-picker';
import InputMask from 'react-input-mask';

import './date-picker.scss';
import { DATE_FORMAT, weekDays } from 'app/constants/common';
import Flex from '../flex/flex';

type IDatePicker = CalendarProps &
  DatePickerProps &
  InputProps & {
    label?: string;
    icon?: boolean;
    children?: React.ReactNode;
    value?: Date[] | Date;
    closeExportCalendar: () => void;
    formState?: any;
    range?: boolean;
  };

const DatePicker = forwardRef<any, IDatePicker>((props: IDatePicker, ref) => {
  const {
    arrow = false,
    label,
    id,
    name,
    placeholder,
    icon = true,
    className,
    disabled,
    children,
    onBlur,
    value,
    format = DATE_FORMAT.DATE,
    closeExportCalendar,
    formState,
    range,
    ...rest
  } = props;

  const mergedClassName = `date-picker ${className}`.trim();

  return (
    <>
      <FormGroup className={mergedClassName} style={{ height: '40px' }}>
        {label && <Label for={id}>{label}</Label>}
        <div className="date-picker-input">
          <DatePickerLib
            {...rest}
            // portal={portal}
            value={value}
            disabled={disabled}
            ref={ref as MutableRefObject<any>}
            weekDays={weekDays}
            format={format}
            name={name}
            range={range}
            key={name}
            className="date-picker-calendar"
            arrow={arrow}
            placeholder={placeholder}
            inputMode="select"
            render={(inner_value, onFocus, onDateChange) => {
              return (
                <InputMask
                  placeholder={placeholder}
                  className={`input-mask ${
                    formState?.errors[name] && 'invalid'
                  }`}
                  onFocus={onFocus}
                  mask={
                    format === DATE_FORMAT.YEAR
                      ? '9999'
                      : range
                        ? '99/99/9999 ~ 99/99/9999'
                        : '99/99/9999'
                  }
                  disabled={disabled}
                  value={inner_value}
                  onChange={onDateChange}
                />
              );
            }}
          >
            {children}
            <Flex
              align="center"
              justify="center"
              gap={16}
              style={{ paddingTop: 10, paddingBottom: 16 }}
            >
              <Button outline onClick={closeExportCalendar}>
                Đặt lại
              </Button>
            </Flex>
          </DatePickerLib>
          {icon && (
            <img
              className="calendar-icon"
              src="content/images/vuesax/linear/calendar-add.svg"
              alt="calendar-icon"
            />
          )}
        </div>
      </FormGroup>
    </>
  );
});

export default DatePicker;
