import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import AuthContainer from 'app/shared/layout/auth/auth';
import { RegisterEmailSchema, registerEmailSchema } from 'app/validation/auth.validation';
import React from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import { Button } from 'reactstrap';

const RegisterEmail = () => {
  const navigate = useNavigate();

  const { control, handleSubmit } = useForm({
    resolver: zodResolver(registerEmailSchema),
  });

  const onSubmit = (data: RegisterEmailSchema) => {
  };

  return (
    <AuthContainer label="Đăng ký">
      <Form<RegisterEmailSchema> onSubmit={handleSubmit(onSubmit)} className="auth-form">
        <Flex direction="column" gap={176}>
          <FormInput control={control} name="email" label="Email" placeholder="Email" />

          <Flex direction="column">
            <Button onClick={() => navigate('/account/register')} color="primary" type="submit" data-cy="submit">
              Tiếp theo
            </Button>
          </Flex>
        </Flex>
      </Form>
    </AuthContainer>
  );
};

export default RegisterEmail;
