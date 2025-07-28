import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { Input as InputStrap, InputProps } from 'reactstrap';

interface IInputNumber extends InputProps {
  color?: ColorType;
}

const InputNumberDecimal = (props: IInputNumber) => {
  const { min, ...rest } = props;

  const preventMinus = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.code === 'Minus') {
      e.preventDefault();
    }
  };

  const preventPasteNegative = (e: React.ClipboardEvent<HTMLInputElement>) => {
    const clipboardData = e.clipboardData || window['clipboardData'];
    const pastedData = parseFloat(clipboardData.getData('text'));

    if (pastedData < 0) {
      e.preventDefault();
    }
  };

  return (
    <InputStrap
      {...rest}
      type="text"
      lang="en"
      step={0.01}
      {...(min === 0 && { onKeyPress: preventMinus, onPaste: preventPasteNegative })}
    />
  );
};

export default InputNumberDecimal;
