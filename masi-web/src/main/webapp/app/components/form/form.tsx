import { FormType } from 'app/shared/model/enumerations/form.model';
import React, { FormEventHandler } from 'react';
import { Form as FormStrap, FormProps } from 'reactstrap';

interface IForm<T> extends FormProps {
  children: React.ReactNode;
  onSubmit?: FormEventHandler<HTMLFormElement>;
  id?: FormType; // Use this id for external submission
}

const Form = <T extends object>(props: IForm<T>) => {
  const { children, onSubmit, id, ...rest } = props;

  return (
    <FormStrap {...rest} onSubmit={onSubmit} id={id}>
      {children}
    </FormStrap>
  );
};

export default Form;
