import { DatePicker as DateTimePicker } from 'antd';
import DatePicker from 'app/components/date-picker/date-picker';
import FormError from 'app/components/form/form-error';
import { DATE_FORMAT } from 'app/constants/common';
import useOnClickOutside from 'app/hooks/use-click-outside';
import { ColorType } from 'app/shared/model/enumerations/color.model';
import {
  convertToDate,
  convertToIsoDate,
  handleMergeTime,
} from 'app/shared/util/date-utils';
import React, { useCallback, useRef } from 'react';
import {
  Control,
  Controller,
  FormState,
  Path,
  UseFormSetValue,
} from 'react-hook-form';
import {
  DateObject,
  DatePickerProps,
  DatePickerRef,
} from 'react-multi-date-picker';
import { FormGroup, Label } from 'reactstrap';
import './form-date-picker.scss';
import dayjs from 'dayjs';

interface IDatePicker<T> extends DatePickerProps {
  color?: ColorType;
  label?: string;
  control?: Control<T>;
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
  onCloseCalendar?: () => void;
  isWithoutForm?: boolean;
  value?: DateObject;
  isConvertToIsoDate?: boolean;
  isConvertToDate?: boolean;
  convertDate?: (date: DateObject) => any;
  includeTimePicker?: boolean;
}

const FormDatePickerV2 = <T extends object>(props: IDatePicker<T>) => {
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
    portal = true,
    formState,
    placeholder,
    onCloseCalendar,
    isWithoutForm,
    isConvertToIsoDate,
    isConvertToDate,
    convertDate,
    includeTimePicker = false,
    ...rest
  } = props;

  const dateRef = useRef<DatePickerRef>(null);

  const handleDatePickerClose = useCallback(() => {
    dateRef.current.closeCalendar();
  }, [dateRef]);

  !portal && useOnClickOutside(dateRef, handleDatePickerClose);

  const closeExportCalendar = () => {
    setValue && setValue(name, null);
    props.onChange?.(null);
    dateRef.current?.closeCalendar();
  };

  const { RangePicker } = DateTimePicker;

  const mergedClassName = `date-picker ${className}`.trim();
  return (
    <>
      {!isWithoutForm ? (
        <Controller
          control={control}
          name={props.name}
          render={({ field: { onChange, value }, fieldState: { error } }) => {
            return (
              <FormGroup className={`form-group-v2 ${mergedClassName}`}>
                {label && (
                  <Label className="form-label-v2" for={name}>
                    {label}
                  </Label>
                )}
                {!includeTimePicker && !range && (
                  // <DatePicker
                  //   {...restField}
                  //   ref={dateRef}
                  //   format={format}
                  //   disabled={disabled}
                  //   value={value || {}}
                  //   onChange={(date: any) => {
                  //     if (isConvertToIsoDate) date = convertToIsoDate?.(date);
                  //     if (isConvertToDate) date = convertToDate?.(date);
                  //     if (convertDate) date = convertDate?.(date);
                  //     onChange(date);
                  //     props.onChange && props.onChange(date as DateObject);
                  //   }}
                  //   className={error ? 'invalid' : ''}
                  //   range={range}
                  //   onlyYearPicker={onlyYearPicker}
                  //   closeExportCalendar={closeExportCalendar}
                  //   portal={portal}
                  //   formState={formState}
                  //   placeholder={placeholder || 'Chọn'}
                  // />
                  <DateTimePicker
                    format={format}
                    placeholder={placeholder || 'Chọn'}
                    style={{ width: '100%', height: 40 }}
                    onChange={e => {
                      if (e) {
                        let eConvert = handleMergeTime(e) as any;
                        if (isConvertToIsoDate)
                          eConvert = convertToIsoDate?.(
                            new DateObject(e.toDate()),
                          );
                        if (isConvertToDate)
                          eConvert = convertToDate?.(
                            new DateObject(e.toDate()),
                          );
                        if (convertDate)
                          eConvert = convertDate?.(new DateObject(e.toDate()));
                        if (onChange) onChange(eConvert);
                        setValue(name, eConvert as any);
                      } else setValue(name, null);
                    }}
                    value={value ? handleMergeTime(dayjs(value)) : null}
                    defaultValue={value ? handleMergeTime(dayjs(value)) : null}
                    disabled={disabled}
                    className={error ? 'invalid' : ''}
                  />
                )}
                {!includeTimePicker && range && (
                  <RangePicker
                    format={format}
                    style={{ width: '100%', height: 40 }}
                    disabled={disabled}
                    className={error ? 'invalid' : ''}
                    onChange={dates => {
                      if (dates && dates.length === 2) {
                        const eConvert = [
                          handleMergeTime(dates[0] as any),
                          handleMergeTime(dates[1] as any),
                        ];
                        setValue(name, eConvert as any);
                      }
                    }}
                    value={
                      Array.isArray(value) && (value as any)?.length === 2
                        ? [
                            handleMergeTime(dayjs(value[0])) as any,
                            handleMergeTime(dayjs(value[1])) as any,
                          ]
                        : undefined
                    }
                    defaultValue={
                      Array.isArray(value) && (value as any)?.length === 2
                        ? [
                            handleMergeTime(dayjs(value[0])) as any,
                            handleMergeTime(dayjs(value[1])) as any,
                          ]
                        : undefined
                    }
                    placeholder={['Từ ngày', 'Đến ngày']}
                  />
                )}
                {includeTimePicker && (
                  <DateTimePicker
                    format={'DD/MM/YYYY HH:mm'}
                    showTime={{ format: 'HH:mm' }}
                    placeholder={placeholder || 'Chọn'}
                    style={{ width: '100%', height: 40 }}
                    onChange={e =>
                      setValue(name, dayjs(e).toISOString() as any)
                    }
                    value={value ? dayjs(value) : null}
                    defaultValue={value ? dayjs(value) : null}
                    disabled={disabled}
                    className={error ? 'invalid' : ''}
                  />
                )}
                {error && <FormError message={error.message} />}
              </FormGroup>
            );
          }}
        />
      ) : (
        <FormGroup className={`form-group-v2 ${mergedClassName}`}>
          {label && (
            <Label className="form-label-v2" for={name}>
              {label}
            </Label>
          )}
          <DatePicker
            ref={dateRef}
            format={format}
            disabled={disabled}
            onChange={(date: DateObject) => props.onChange?.(date)}
            range={range}
            onlyYearPicker={onlyYearPicker}
            closeExportCalendar={closeExportCalendar}
            portal={portal}
            formState={formState}
            placeholder={placeholder || 'Chọn'}
          />
        </FormGroup>
      )}
    </>
  );
};

export default FormDatePickerV2;
