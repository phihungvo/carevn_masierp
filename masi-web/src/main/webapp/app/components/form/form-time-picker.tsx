import './form.scss';
import { ColorType } from 'app/shared/model/enumerations/color.model';
import React, { useState } from 'react';
import { FormGroup, InputProps, Label } from 'reactstrap';
import { Control, Controller, Path } from 'react-hook-form';
import Input from '../input/input';
import FormError from './form-error';

interface IFormInput<T> extends InputProps {
  color?: ColorType;
  label?: string;
  control: Control<T>;
  name: Path<T>;
  disabled?: boolean;
}

const FormTimePicker = <T extends object>(props: IFormInput<T>) => {
  const { label, name, control, type, className, disabled, ...rest } = props;

  const [timeVal, setTimeVal] = useState('');

  const mergedClassName = `form-input ${className ? className : ''}`.trim();

  const timePicker = document.getElementById('time') as HTMLInputElement;

  const handleResetTime = () => {
    timePicker.value = '';
    setTimeVal('');
  };

  return (
    <>
      <Controller
        control={control}
        name={name}
        render={({ field, fieldState: { error } }) => (
          <>
            <FormGroup className="form-group time-picker">
              {label && <Label for={name}>{label}</Label>}
              <div className="input-time-wrapper">
                <Input
                  {...rest}
                  {...field}
                  // onChange={() => {
                  //   field.onChange();
                  //   setTimeVal(field.value);
                  // }}
                  // value={timeVal}
                  disabled={disabled}
                  id="time"
                  type="time"
                  className={`${mergedClassName} ${error ? 'invalid' : ''}`.trim()}
                />
                {/* {timePicker && timePicker.value && <FontAwesomeIcon icon={faClose} className="close-icon" onClick={handleResetTime} />} */}
              </div>
              {error && <FormError message={error.message} />}
            </FormGroup>
          </>
        )}
      />
    </>
  );
};

export default FormTimePicker;
