import { Input } from 'antd';
import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import useEmployeeLabel from 'app/hooks/use-employee-lable';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';
import { z } from 'zod';

type Props<T> = ComponentProps<typeof Input> & {
  label?: string | React.ReactNode;
  name: Path<T>;
  disabled?: boolean;
  isExcludeHookForm?: boolean;
};

const uuid = z.string().uuid()
const isUUID = (value: string) => {
  if (!value) return undefined
  let isValid = uuid.safeParse(value).success
  if (isValid) return value
  return undefined
}

const PersonLabel = <T = any>(props: Props<T>) => {
  const { label, name, disabled, isExcludeHookForm, ...rest } = props;

  const methods = useFormContext();
  const { watch, setValue } = methods
  const id = watch(name)

  useEmployeeLabel({
    id: isUUID(id),
    autoSetLabel: (data) => setValue(name, data?.label as any)
  })

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
            return (
              <FormWrap label={label} error={fieldState?.error?.message}>
                <Input
                  {...rest}
                  {...field}
                  value={!isUUID(field?.value) && field?.value}
                  onChange={field?.onChange}
                  disabled={true}
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

export default PersonLabel;
