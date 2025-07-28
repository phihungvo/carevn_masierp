import FormError from 'app/components/form/form-error';
import SelectV2 from 'app/components/SelectV2/SelectV2';
import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { Control, Controller, Path } from 'react-hook-form';
import { GroupBase, Props } from 'react-select';
import { FormGroup, Label } from 'reactstrap';
import './form-select.scss';

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

const FormSelectV2 = <
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

  const mergedClassName = `form-select-v2 ${className ? className : ''}`.trim();

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field, fieldState: { error } }) => {
          return (
            <>
              <FormGroup className="form-group-v2">
                {label && (
                  <Label className="form-label-v2" for={id}>
                    {label}
                  </Label>
                )}
                <SelectV2
                  value={field?.value ?? ''}
                  onChange={(value: React.ChangeEvent<HTMLSelectElement>) => {
                    field.onChange(value?.target.value);
                    onSelect && onSelect();
                    props.onChanges?.(value?.target.value);
                  }}
                  onBlur={field.onBlur}
                  id={id}
                  className={`${mergedClassName} ${
                    error || errorMsg ? 'invalid' : ''
                  }`.trim()}
                  disabled={disabled}
                  options={options?.map(x => ({
                    value: x?.['value'],
                    label: x?.['label'],
                  }))}
                  placeholder={placeholder}
                />
                {(errorMsg || error) && (
                  <FormError message={errorMsg || error.message} />
                )}
              </FormGroup>
            </>
          );
        }}
      />
    </>
  );
};

export default FormSelectV2;
