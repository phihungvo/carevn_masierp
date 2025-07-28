import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';
import InputDelay from '../input-delay/InputDelay';

type Props<T> = ComponentProps<typeof InputDelay> & {
  label?: string | React.ReactNode;
  name: Path<T>;
  disabled?: boolean;
  isExcludeHookForm?: boolean;
};

const WrapInputDelay = <T = any,>(props: Props<T>) => {
  const { label, name, disabled, isExcludeHookForm, ...rest } = props;

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
              <InputDelay
                {...rest}
                {...field}
                disabled={disabled}
                placeholder="Nhập"
              />
            </FormWrap>
          )}
        />
      )}
    </>
  );
};

export default WrapInputDelay;
