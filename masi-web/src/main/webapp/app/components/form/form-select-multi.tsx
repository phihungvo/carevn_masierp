import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { FormGroup, Label } from 'reactstrap';
import { Control, Controller, Path } from 'react-hook-form';
import FormError from './form-error';
import Select from '../select/select';
import { GroupBase, Props } from 'react-select';

interface ISelect<T, Option, IsMulti extends boolean = false, Group extends GroupBase<Option> = GroupBase<Option>>
  extends Props<Option, IsMulti, Group> {
  color?: ColorType;
  label?: string;
  control: Control<T>;
  name: Path<T>;
  disabled?: boolean;
  children?: React.ReactNode;
  errorMsg?: string;
  maxLength?: number;
}

const FormSelectMulti = <T extends object, Option, IsMulti extends boolean = false, Group extends GroupBase<Option> = GroupBase<Option>>(
  props: ISelect<T, Option, IsMulti, Group>,
) => {
  const { label, name, control, className, disabled, id, children, placeholder, onFocus, errorMsg, options, maxLength, ...rest } = props;

  const mergedClassName = `form-input ${className ? className : ''}`.trim();

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field, fieldState: { error } }) => (
          <>
            <FormGroup className="form-group">
              {label && <Label for={id}>{label}</Label>}
              <Select
                {...rest}
                options={options}
                value={field.value}
                onChange={field.onChange}
                onBlur={field.onBlur}
                placeholder={placeholder}
                id={id}
                className={`${mergedClassName} ${error || errorMsg ? 'invalid' : ''}`.trim()}
                isDisabled={disabled}
                isMulti={true as IsMulti}
              />
              {(errorMsg || error) && <FormError message={errorMsg || error.message} />}
            </FormGroup>
          </>
        )}
      />
    </>
  );
};

export default FormSelectMulti;
