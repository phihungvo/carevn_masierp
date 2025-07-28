import './button-icon.scss';
import React from 'react';
import Button from '../button/button';
import { ButtonProps } from 'reactstrap';

interface IButtonIcon extends ButtonProps {
  icon: React.ReactNode;
  children?: React.ReactNode;
  id?: string;
}

const ButtonIcon = (props: IButtonIcon) => {
  const { icon, disabled, onClick, children, className, id } = props;
  const mergeClassName = className ? `btn-icon ${className}` : 'btn-icon';
  return (
    <Button id={id} className={mergeClassName} disabled={disabled} onClick={onClick}>
      {icon} {children}
    </Button>
  );
};

export default ButtonIcon;
