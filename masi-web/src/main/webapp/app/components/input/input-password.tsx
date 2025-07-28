import { ColorType } from 'app/shared/model/enumerations/color.model';
import './input.scss';
import React, { useState } from 'react';
import { Input as InputStrap, InputProps } from 'reactstrap';

interface IInput extends InputProps {
  color?: ColorType;
}

const InputPassword = (props: IInput) => {
  const { min, ...rest } = props;

  const [isShow, setIsShow] = useState(false);

  return (
    <div className="input-container">
      <InputStrap {...rest} type={isShow ? 'text' : 'password'} />
      {isShow ? (
        <img
          src="content/images/vuesax/linear/eye-slash.svg"
          alt="hide-password"
          className="input-reveal-password"
          onClick={() => setIsShow(false)}
        />
      ) : (
        <img
          src="content/images/vuesax/linear/eye.svg"
          alt="show-password"
          className="input-reveal-password"
          onClick={() => setIsShow(true)}
        />
      )}
    </div>
  );
};

export default InputPassword;
