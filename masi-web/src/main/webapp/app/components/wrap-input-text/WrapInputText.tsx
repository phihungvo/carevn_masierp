import { Input } from 'antd';
import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';

type Props<T> = ComponentProps<typeof Input> & {
  label?: string | React.ReactNode;
  name: Path<T>;
  disabled?: boolean;
  isExcludeHookForm?: boolean;
};

const WrapInputText = <T = any>(props: Props<T>) => {
  const { label, name, disabled, isExcludeHookForm, ...rest } = props;

  const methods = useFormContext();

  return (
    <>
      {isExcludeHookForm ? (
        <FormWrap label={label}>
          <Input {...rest} disabled={disabled} placeholder="Nhập" />
        </FormWrap>
      ) : (
        <Controller
          name={name}
          control={methods.control}
          render={({ field, fieldState }) => {
            // console.log(field?.name, field);
            return (
              <FormWrap label={label} error={fieldState?.error?.message}>
                <Input
                  {...rest}
                  {...field}
                  value={field?.value}
                  onChange={field?.onChange}
                  disabled={disabled}
                  placeholder="Nhập"
                />
              </FormWrap>
            )
          }}
        />
      )}
    </>
  );
};

export default WrapInputText;
