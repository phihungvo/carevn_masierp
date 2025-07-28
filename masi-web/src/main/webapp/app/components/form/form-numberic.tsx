import React from 'react';
import { FormGroup, Label } from 'reactstrap';
import { Control, Controller, Path } from 'react-hook-form';

import FormError from './form-error';
import InputNumberic from 'app/components/input/input-numberic';
import { NumericFormatProps } from 'react-number-format';

interface IFormNumbericProps<T> extends NumericFormatProps {
  label?: string;
  control: Control<T>;
  name: Path<T>;
  id: Path<T>;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  disabled?: boolean;
  errorMsg?: string;
  displayType?: 'input' | 'text';
  showError?: boolean;
}

const FormNumberic = <T extends object>(props: IFormNumbericProps<T>) => {
  const { label, name, control, disabled, id, errorMsg, onChange, displayType, showError = true, className, ...rest } = props;

  const mergedClassName = `form-input ${className ? className : ''}`.trim();

  return (
    <Controller
      control={control}
      name={name}
      render={({ field, fieldState: { error } }) => (
        <>
          <FormGroup className="form-group">
            {label && <Label for={id}>{label}</Label>}
            <InputNumberic
              value={field.value}
              disabled={disabled}
              onChange={e => {
                field.onChange(e);
                onChange(e);
              }}
              displayType={displayType}
              className={`${mergedClassName} ${showError && (error || errorMsg) ? 'invalid' : ''}`.trim()}
            />
            {(errorMsg || error) && <FormError message={errorMsg || error.message} />}
          </FormGroup>
        </>
      )}
    />
  );
};

export default FormNumberic;
