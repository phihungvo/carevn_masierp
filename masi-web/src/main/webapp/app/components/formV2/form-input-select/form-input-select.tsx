import { Input, Select } from 'antd';
import FormError from 'app/components/form/form-error';
import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { Control, Controller, Path, UseFormSetValue } from 'react-hook-form';
import { FormGroup, InputProps, Label } from 'reactstrap';
import './form-input-select.scss';

interface IFormInputSelectProps<T> extends InputProps {
  color?: ColorType;
  label?: string;
  control?: Control<T>;
  name: Path<T>;
  disabled?: boolean;
  children?: React.ReactNode;
  errorMsg?: string;
  showError?: boolean;
  selectKey?: string;
  setValue?: UseFormSetValue<T>;
  selectValue?: string;
  selectOptions: { value: string; label: string }[];
}

const FormInputSelect = <T extends object>(props: IFormInputSelectProps<T>) => {
  const {
    label,
    name,
    control,
    disabled,
    onClick,
    id,
    placeholder,
    errorMsg,
    showError = true,
    selectKey,
    setValue,
    selectValue,
    selectOptions,
    className,
  } = props;

  const mergedClassName = `form-input-select ${
    className ? className : ''
  }`.trim();

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field: { onChange, value }, fieldState: { error } }) => (
          <>
            <FormGroup className="form-group-v2" onClick={onClick}>
              {label && (
                <Label className="form-label-v2" for={id}>
                  {label}
                </Label>
              )}
              <Input
                id={id}
                className={`${mergedClassName} ${
                  showError && (error || errorMsg) ? 'invalid' : ''
                }`.trim()}
                addonBefore={
                  <Select
                    id={selectKey}
                    placeholder="Chọn"
                    options={selectOptions}
                    style={{ height: 40, width: '70px' }}
                    disabled={disabled}
                    onChange={e => {
                      if (setValue) setValue(selectKey as any, e as any);
                    }}
                    value={selectValue}
                  />
                }
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                disabled={disabled}
              />
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

export default FormInputSelect;
