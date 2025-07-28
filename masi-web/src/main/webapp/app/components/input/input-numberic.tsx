import React, { ReactElement } from 'react';
import { Input as InputStrap } from 'reactstrap';
import { NumericFormat, NumericFormatProps } from 'react-number-format';

interface IInputNumbericProps extends NumericFormatProps {
  value: number;
  disabled: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  displayType?: 'input' | 'text';
}

function InputNumberic({ value, disabled, onChange, displayType, className }: IInputNumbericProps): ReactElement {
  return (
    <NumericFormat
      onChange={(e: React.ChangeEvent<HTMLInputElement>) => onChange(e)}
      disabled={disabled}
      className={`${className || ''} input-numberic`}
      value={value}
      customInput={InputStrap}
      thousandSeparator
      displayType={displayType}
    />
  );
}

export default InputNumberic;
