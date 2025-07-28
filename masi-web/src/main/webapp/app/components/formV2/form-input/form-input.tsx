import FormError from 'app/components/form/form-error';
import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { Control, Controller, Path } from 'react-hook-form';
import { FormGroup, InputProps, Label } from 'reactstrap';
import Input from '../../input/input';
import InputNumber from '../../input/input-number';
import './form-input.scss';

interface IFormInput<T> extends InputProps {
  color?: ColorType;
  label?: string;
  control?: Control<T>;
  name: Path<T>;
  disabled?: boolean;
  children?: React.ReactNode;
  errorMsg?: string;
  showError?: boolean;
  isRowLayout?: boolean;
  isBorderBottomInput?: boolean;
  row?: boolean;
}

const FormInputV2 = <T extends object>(props: IFormInput<T>) => {
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
    isRowLayout,
    isBorderBottomInput,
    row,
    ...rest
  } = props;

  const mergedClassName = `form-input-v2 ${className ? className : ''}`.trim();

  function isNumberKey(evt) {
    const charCode = evt.which ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) return false;
    return true;
  }

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field, fieldState: { error } }) => (
          <>
            <FormGroup
              className="form-group-v2"
              check={type === 'checkbox'}
              onClick={onClick}
              data-row={isRowLayout}
              row={row}
            >
              {label && (
                <Label
                  className="form-label-v2"
                  for={id}
                  check={type === 'checkbox'}
                >
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
                    isNumberKey(event);
                  }}
                  className={`${mergedClassName} ${
                    showError && (error || errorMsg) ? 'invalid' : ''
                  }`.trim()}
                  disabled={disabled}
                  hidden={hidden}
                  min={min}
                  value={field.value || props.value}
                  data-border-bottom={isBorderBottomInput}
                />
              ) : (
                <Input
                  {...field}
                  onFocus={onFocus}
                  hidden={hidden}
                  placeholder={placeholder !== undefined ? placeholder : 'Điền'}
                  id={id}
                  onChange={e => {
                    field.onChange(e.target.value);
                    rest.onChange && rest.onChange(e);
                  }}
                  type={type}
                  className={`${mergedClassName} ${
                    showError && (error || errorMsg) ? 'invalid' : ''
                  }`.trim()}
                  disabled={disabled}
                  rows={rows}
                  onPaste={onPaste}
                  value={
                    field.value !== null && field.value !== undefined
                      ? field.value
                      : props.value
                  }
                  data-border-bottom={isBorderBottomInput}
                  style={{ ...rest?.style }}
                >
                  {children}
                </Input>
              )}
              {showError && (errorMsg || error) && (
                <FormError message={errorMsg || error.message} />
              )}
            </FormGroup>
          </>
        )}
      />
    </>
  );
};

export default FormInputV2;
