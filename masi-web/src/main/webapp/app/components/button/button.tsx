import { ColorType } from 'app/shared/model/enumerations/color.model';
import './button.scss';
import React from 'react';
import { ButtonProps, Spinner } from 'reactstrap';
import { FormType } from 'app/shared/model/enumerations/form.model';

interface Props extends ButtonProps {
  color?: ColorType;
  form?: FormType; // Submit form type must be the same as form id to trigger submit form
  loading?: boolean;
}

const Button = (props: Props) => {
  const { color, className, children, outline, form, loading, ...rest } = props;

  const colorClass = `${className ? className : ''} ${color ? 'btn-' + color : ''} ${outline ? 'btn-outline' : ''}`.trim();

  return (
    <button {...rest} className={`btn ${colorClass} ${className ?? ''}`.trim()} form={form}>
      {loading && <Spinner size="sm" color="light" style={{ marginRight: 4 }} />}
      {children}
    </button>
  );
};

export default Button;
