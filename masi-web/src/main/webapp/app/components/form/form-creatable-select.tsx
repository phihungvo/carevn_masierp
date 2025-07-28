import { ColorType } from 'app/shared/model/enumerations/color.model';
import React, { useEffect, useState } from 'react';
import { FormGroup, Label } from 'reactstrap';
import { Control, Controller, Path } from 'react-hook-form';
import FormError from './form-error';
import { GroupBase, Props } from 'react-select';
import CreatableSelect from '../select/createtable-select';

const createOption = (label: string) => ({
  label,
  value: label?.replace(/\W/g, ''),
});

interface ISelect<T, Option, IsMulti extends boolean = false, Group extends GroupBase<Option> = GroupBase<Option>>
  extends Props<Option, IsMulti, Group> {
  color?: ColorType;
  label?: string;
  control: Control<T>;
  name: Path<T>;
  disabled?: boolean;
  children?: React.ReactNode;
  errorMsg?: string;
  onCreateOption?: (inputValue: string) => void;
  onChanges?: (value) => void;
}

const FormCreatableSelect = <
  T extends object,
  Option,
  IsMulti extends boolean = false,
  Group extends GroupBase<Option> = GroupBase<Option>,
>(
  props: ISelect<T, Option, IsMulti, Group>,
) => {
  const { label, name, control, className, disabled, id, children, placeholder, onFocus, errorMsg, options, onCreateOption, ...rest } =
    props;

  const mergedClassName = `form-input ${className ? className : ''}`.trim();

  const [listOptions, setListOptions] = useState<any>([]);

  useEffect(() => {
    setListOptions(options);
  }, [options]);

  const handleCreate = (inputValue: string, onChange: (newValue) => void) => {
    const newOption = createOption(inputValue);
    setListOptions(prev => [...prev, newOption]);
    onChange(newOption?.value);
  };

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field, fieldState: { error } }) => (
          <>
            <FormGroup className="form-group">
              {label && <Label for={id}>{label}</Label>}
              <CreatableSelect
                {...rest}
                styles={{ menuPortal: (base) => ({ ...base, zIndex: 9999 }) }}
                menuPortalTarget={document.body}
                options={listOptions}
                value={listOptions?.filter((option: any) => option.value === field?.value)}
                onChange={(value: any) => {
                  field.onChange(value?.value);
                  rest?.onChanges && rest?.onChanges(value?.value);
                }}
                onBlur={field.onBlur}
                placeholder={placeholder}
                id={id}
                className={`${mergedClassName} ${error || errorMsg ? 'invalid' : ''}`.trim()}
                isDisabled={disabled}
                onCreateOption={value => (onCreateOption ? onCreateOption(value) : handleCreate(value, field.onChange))}
              />
              {(errorMsg || error) && <FormError message={errorMsg || error.message} />}
            </FormGroup>
          </>
        )}
      />
    </>
  );
};

export default FormCreatableSelect;
