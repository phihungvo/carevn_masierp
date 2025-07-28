import React, { ComponentProps } from 'react';
import { useFormContext } from 'react-hook-form';
import { Form, Label } from 'reactstrap';
import Select from '../select/select';

type Props<T> = ComponentProps<typeof Select> & {
  SelectComponent?: React.ReactNode;
  label?: string;
};

const FormSelectV3 = <T = any>(props: Props<T>) => {
  const { label, SelectComponent, name, ...rest } = props;

  return (
    <>
      <Form className="form-group-v2">
        {label && (
          <Label className="form-label-v2" htmlFor={rest.id}>
            {label}
          </Label>
        )}
        {SelectComponent ?? <Select {...props} />}
      </Form>
    </>
  );
};

export default FormSelectV3;
