import { ColorType } from 'app/shared/model/enumerations/color.model';
import './input.scss';
import React, { forwardRef } from 'react';
import { Input as InputStrap, InputProps } from 'reactstrap';

interface IInput extends InputProps {
  color?: ColorType;
}

const Input = (props: IInput) => {
  const { ...rest } = props;

  return <InputStrap {...rest} />;
};

export default forwardRef(Input);
