import { FormGroup, Label } from 'reactstrap';
import React, { useCallback, useEffect, useRef } from 'react';
import { Control, Controller, FormState, Path, UseFormSetValue } from 'react-hook-form';
import { DateObject, DatePickerProps, DatePickerRef } from 'react-multi-date-picker';

import FormError from './form-error';
import DatePicker from '../date-picker/date-picker';
import useOnClickOutside from 'app/hooks/use-click-outside';
import { DATE_FORMAT } from 'app/constants/common';
import { ColorType } from 'app/shared/model/enumerations/color.model';

interface IDatePicker<T> extends DatePickerProps {
  color?: ColorType;
  label?: string;
  control: Control<T>;
  name: Path<T>;
  className?: string;
  disabled?: boolean;
  range?: boolean;
  format?: string;
  onlyYearPicker?: boolean;
  setSelectedDate?: React.Dispatch<React.SetStateAction<any[]>>;
  setValue?: UseFormSetValue<T>;
  onChange?: (date: DateObject) => void;
  portal?: boolean;
  formState?: FormState<T>;
}

const FormDatePicker = <T extends object>(props: IDatePicker<T>) => {
  const {
    label,
    name,
    control,
    className,
    disabled,
    range,
    format = DATE_FORMAT.DATE,
    onlyYearPicker,
    setSelectedDate,
    setValue,
    portal,
    formState,
    placeholder,
    ...rest
  } = props;

  const dateRef = useRef<DatePickerRef>(null);

  const handleDatePickerClose = useCallback(() => dateRef.current.closeCalendar(), [dateRef]);
  !portal && useOnClickOutside(dateRef, handleDatePickerClose);

  const closeExportCalendar = () => {
    setValue(name, null);
    dateRef.current?.closeCalendar();
  };

  return (
    <>
      <Controller
        control={control}
        name={props.name}
        render={({ field: { onChange, value, ...restField }, fieldState: { error } }) => (
          <FormGroup className={className}>
            {label && <Label for={name}>{label}</Label>}
            <DatePicker
              {...restField}
              ref={dateRef}
              format={format}
              disabled={disabled}
              value={value || {}}
              onChange={(date: DateObject) => {
                onChange(date);
                props.onChange?.(date);
              }}
              className={error ? 'invalid' : ''}
              range={range}
              onlyYearPicker={onlyYearPicker}
              closeExportCalendar={closeExportCalendar}
              portal={portal}
              formState={formState}
              placeholder={placeholder}
            />
            {error && <FormError message={error.message} />}
          </FormGroup>
        )}
      />
    </>
  );
};

export default FormDatePicker;
