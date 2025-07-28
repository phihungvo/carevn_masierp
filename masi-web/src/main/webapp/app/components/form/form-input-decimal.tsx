import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { FormGroup, InputProps, Label } from 'reactstrap';
import { Control, Controller, Path } from 'react-hook-form';
import FormError from './form-error';
import InputNumberDecimal from '../input/input-number-decimal';

interface IFormInput<T> extends InputProps {
  color?: ColorType;
  label?: string;
  control: Control<T>;
  name: Path<T>;
  disabled?: boolean;
}

const FormInputDecimal = <T extends object>(props: IFormInput<T>) => {
  const { label, name, control, type, className, disabled, onClick, id, ...rest } = props;

  const mergedClassName = `form-input ${className ? className : ''}`.trim();

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field, fieldState: { error } }) => (
          <>
            <FormGroup className="form-group" check={type === 'checkbox'} onClick={onClick}>
              {label && (
                <Label for={id} check={type === 'checkbox'}>
                  {label}
                </Label>
              )}
              <InputNumberDecimal
                {...field}
                {...rest}
                onChange={e => {
                  rest.onChange && rest.onChange(e);
                }}
                id={id}
                type={type}
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

export default FormInputDecimal;
