import { DatePicker } from 'antd';
import AntDatePicker from 'app/components/date-picker/ant-date-picker';
import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import { DATE_FORMAT } from 'app/constants/common';
import dayjs from 'dayjs';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';

type Props<T> = ComponentProps<typeof DatePicker> & {
  label?: string;
  name: Path<T>;
  convertToIsoString?: boolean;
  isExcludeHookForm?: boolean;
  onDateChange?: (date: string) => void;
};

// Đầu vào luôn luôn là ISOString
const WrapDate = <T = any,>(props: Props<T>) => {
  const {
    label,
    name,
    convertToIsoString = true,
    isExcludeHookForm,
    value,
    onDateChange,
    ...rest
  } = props;

  const methods = useFormContext();

  return (
    <>
      {isExcludeHookForm ? (
        <FormWrap label={label}>
          <AntDatePicker
            {...rest}
            value={value && dayjs(value as string)}
            onChange={(date: any) => {
              if (convertToIsoString) {
                date = date?.toISOString();
              }
              onDateChange && onDateChange(date);
            }}
          />
        </FormWrap>
      ) : (
        <Controller
          name={name}
          control={methods.control}
          render={({ field, fieldState }) => (
            <FormWrap label={label} error={fieldState?.error?.message}>
              <AntDatePicker
                {...rest}
                value={field?.value && dayjs(field?.value)}
                onChange={(date: any) => {
                  if (convertToIsoString) {
                    date = date?.toISOString();
                  }
                  field.onChange(date);
                  onDateChange && onDateChange(date);
                }}
              />
            </FormWrap>
          )}
        />
      )}
    </>
  );
};

export default WrapDate;
