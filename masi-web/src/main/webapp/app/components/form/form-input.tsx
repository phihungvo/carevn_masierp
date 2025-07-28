import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { Control, Controller, Path } from 'react-hook-form';
import { FormGroup, InputProps, Label } from 'reactstrap';
import Input from '../input/input';
import InputNumber from '../input/input-number';
import FormError from './form-error';

interface IFormInput<T> extends InputProps {
  color?: ColorType;
  label?: string;
  control: Control<T>;
  name: Path<T>;
  disabled?: boolean;
  children?: React.ReactNode;
  errorMsg?: string;
  showError?: boolean;
}

const FormInput = <T extends object>(props: IFormInput<T>) => {
  const {
    label,
    name,
    control,
    type,
    className,
    disabled,
    onClick,
    id,
    children,
    placeholder,
    hidden,
    min,
    onFocus,
    onPaste,
    rows,
    errorMsg,
    showError = true,
    ...rest
  } = props;
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
              {type === 'number' ? (
                <InputNumber
                  {...field}
                  placeholder={placeholder}
                  id={id}
                  type={type}
                  onChange={e => {
                    field.onChange(e.target.value);
                    rest.onChange && rest.onChange(e);
                  }}
                  onKeyDown={event => {
                    if (event.key === 'e') event.preventDefault();
                  }}
                  className={`${mergedClassName} ${showError && (error || errorMsg) ? 'invalid' : ''}`.trim()}
                  disabled={disabled}
                  hidden={hidden}
                  min={min}
                />
              ) : (
                <Input
                  {...field}
                  onFocus={onFocus}
                  hidden={hidden}
                  placeholder={placeholder}
                  id={id}
                  onChange={e => {
                    field.onChange(e.target.value);
                    rest.onChange && rest.onChange(e);
                  }}
                  type={type}
                  className={`${mergedClassName} ${showError && (error || errorMsg) ? 'invalid' : ''}`.trim()}
                  disabled={disabled}
                  rows={rows}
                  onPaste={onPaste}
                >
                  {children}
                </Input>
              )}
              {showError && (errorMsg || error) && <FormError message={errorMsg || error.message} />}
            </FormGroup>
          </>
        )}
      />
    </>
  );
};

export default FormInput;
