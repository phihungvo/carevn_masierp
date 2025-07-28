import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { Control, Controller, Path } from 'react-hook-form';
import { GroupBase, Props } from 'react-select';
import { FormGroup, Label } from 'reactstrap';
import Select from '../select/select';
import FormError from './form-error';

interface ISelect<
  T,
  Option,
  IsMulti extends boolean = false,
  Group extends GroupBase<Option> = GroupBase<Option>,
> extends Props<Option, IsMulti, Group> {
  color?: ColorType;
  label?: string;
  control?: Control<T>;
  name: Path<T>;
  disabled?: boolean;
  children?: React.ReactNode;
  errorMsg?: string;
  onSelect?: () => void;
  onChanges?: (value: string) => void;
}

const FormSelect = <
  T extends object,
  Option,
  IsMulti extends boolean = false,
  Group extends GroupBase<Option> = GroupBase<Option>,
>(
  props: ISelect<T, Option, IsMulti, Group>,
) => {
  const {
    label,
    name,
    control,
    className,
    disabled,
    id,
    children,
    placeholder,
    onFocus,
    errorMsg,
    options,
    isClearable,
    onSelect,
    onChange,
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
            <FormGroup className="form-group-v2">
              {label && (
                <Label className="form-label-v2" for={id}>
                  {label}
                </Label>
              )}
              <Select
                {...rest}
                options={options}
                value={
                  field?.value
                    ? options?.filter(
                        (option: any) => option.value === field?.value,
                      )
                    : null
                }
                onChange={(value: any) => {
                  field.onChange(value?.value);
                  onSelect && onSelect();
                  props.onChanges?.(value?.value);
                }}
                onBlur={field.onBlur}
                placeholder={placeholder}
                id={id}
                className={`${mergedClassName} ${
                  error || errorMsg ? 'invalid' : ''
                }`.trim()}
                isDisabled={disabled}
                isClearable={isClearable}
                styles={{ menuPortal: base => ({ ...base, zIndex: 9999 }) }}
                menuPortalTarget={document.body}
              />
              {(errorMsg || error) && (
                <FormError message={errorMsg || error.message} />
              )}
            </FormGroup>
          </>
        )}
      />
    </>
  );
};

export default FormSelect;
