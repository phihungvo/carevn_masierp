import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import { convertCurrency, convertCurrencyWithMaximum } from 'app/shared/util/format';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';
import InputDelay from '../input-delay/InputDelay';

type Props<T> = ComponentProps<typeof InputDelay> & {
  label?: string | React.ReactNode;
  name: Path<T>;
  disabled?: boolean;
  isExcludeHookForm?: boolean;
  isFormatCurrency?: boolean;
  max?: number;
};

const WrapInputNumber = <T = any>(props: Props<T>) => {
  const {
    label,
    name,
    disabled,
    isExcludeHookForm,
    isFormatCurrency = true,
    onCompletedChange,
    max,
    ...rest
  } = props;

  const methods = useFormContext();

  return (
    <>
      {isExcludeHookForm ? (
        <FormWrap label={label}>
          <InputDelay {...rest} disabled={disabled}  placeholder="Nhập" />
        </FormWrap>
      ) : (
        <Controller
          key={name}
          name={name}
          control={methods.control}
          render={({ field: { value, ...restField }, fieldState }) => {
            return (
              <FormWrap label={label} error={fieldState?.error?.message}>
                <InputDelay
                  {...rest}
                  {...restField}
                  key={name}
                  value={isFormatCurrency ? convertCurrency(value) : value}
                  disabled={disabled}
                  placeholder="Nhập"
                  regrex={/\D+/ig}
                  convertValue={isFormatCurrency && max ? convertCurrencyWithMaximum(max) : convertCurrency as any}
                  onCompletedChange={(content) => {
                      content = content.replace(/\D+/ig, '');
                      restField.onChange(+content);
                      onCompletedChange && onCompletedChange(content);
                  }}
                />
              </FormWrap>
            )
          }}
        />
      )}
    </>
  );
};

export default WrapInputNumber;
