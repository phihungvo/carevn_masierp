import { Input } from 'antd';
import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';
import './index.scss'

type Props<T> = ComponentProps<typeof Input.TextArea> & {
  label?: string;
  name: Path<T>;
};

const WrapTextArea = <T = any,>(props: Props<T>) => {
  const { label, name, ...rest } = props;

  const { control } = useFormContext();

  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState }) => (
        <FormWrap label={label} error={fieldState?.error?.message}>
          <Input.TextArea {...rest} {...field} placeholder="Nhập" />
        </FormWrap>
      )}
    />
  );
};

export default WrapTextArea;
