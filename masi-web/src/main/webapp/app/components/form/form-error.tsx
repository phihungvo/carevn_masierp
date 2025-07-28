import React from 'react';
import Flex from '../flex/flex';
import { FormFeedback } from 'reactstrap';

interface IFormError extends React.HTMLAttributes<HTMLDivElement> {
  message: string;
}

const FormError = (props: IFormError) => {
  const { message, ...rest } = props;

  return (
    <Flex {...rest} align="center" gap={4} className="form-error">
      <img src="content/images/vuesax/linear/cancel.svg" alt="cancel-icon" />
      <FormFeedback>{message}</FormFeedback>
    </Flex>
  );
};

export default FormError;
