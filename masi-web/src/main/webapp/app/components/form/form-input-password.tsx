import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { FormGroup, InputProps, Label } from 'reactstrap';
import { Control, Controller, Path } from 'react-hook-form';
import FormError from './form-error';
import InputPassword from '../input/input-password';

interface IFormInput<T> extends InputProps {
  color?: ColorType;
  label?: string;
  control: Control<T>;
  name: Path<T>;
  disabled?: boolean;
}

const FormInputPassword = <T extends object>(props: IFormInput<T>) => {
  const { label, name, control, type, className, disabled, placeholder = 'Mật khẩu', ...rest } = props;

  const mergedClassName = `form-input ${className ? className : ''}`.trim();

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field, fieldState: { error } }) => (
          <>
            <FormGroup className="form-group">
              {label && <Label for={name}>{label}</Label>}
              <InputPassword
                {...rest}
                {...field}
                type={type}
                placeholder={placeholder}
                className={`${mergedClassName} ${error ? 'invalid' : ''}`.trim()}
                disabled={disabled}
              />
              {error && <FormError message={error.message} />}
            </FormGroup>
          </>
        )}
      />
    </>
  );
};

export default FormInputPassword;
