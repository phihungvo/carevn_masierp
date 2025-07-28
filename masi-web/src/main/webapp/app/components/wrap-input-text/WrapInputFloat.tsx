import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';
import InputDelay from '../input-delay/InputDelay';
import { Input } from 'antd';

type Props<T> = ComponentProps<typeof InputDelay> & {
  label?: string | React.ReactNode;
  name: Path<T>;
  disabled?: boolean;
  isExcludeHookForm?: boolean;
  onInputChange?: (value: string) => void;
};

const WrapInputFloat = <T = any>(props: Props<T>) => {
  const { label, name, disabled, isExcludeHookForm, onInputChange, ...rest } = props;

  const methods = useFormContext();

  return (
    <>
      {isExcludeHookForm ? (
        <FormWrap label={label}>
          <InputDelay {...rest} disabled={disabled} placeholder="Nhập" />
        </FormWrap>
      ) : (
        <Controller
          name={name}
          control={methods.control}
          render={({ field, fieldState }) => (
            <FormWrap label={label} error={fieldState?.error?.message}>
              <Input
                {...rest}
                {...field}
                value={!field?.value ? '' : field?.value}
                disabled={disabled}
                placeholder="Nhập"
                onChange={(e) => {
                    let content = e.target.value
                    const sanitized = content.replace(/[^0-9.]/g, '');
                    const result = sanitized.replace(/\.{2,}/g, '').replace(/(.*?\..*?)\./g, '$1');
                    field.onChange(result);
                    onInputChange && onInputChange(result);
                }}
              />
            </FormWrap>
          )}
        />
      )}
    </>
  );
};

export default WrapInputFloat;
