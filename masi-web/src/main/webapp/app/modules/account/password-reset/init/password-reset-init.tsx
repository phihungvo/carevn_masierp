import React, { useEffect } from 'react';
import { translate } from 'react-jhipster';
import { toast } from 'react-toastify';

import { handlePasswordResetInit, reset } from '../password-reset.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import AuthContainer from 'app/shared/layout/auth/auth';
import Form from 'app/components/form/form';
import { SubmitHandler, useForm } from 'react-hook-form';
import FormInput from 'app/components/form/form-input';
import { zodResolver } from '@hookform/resolvers/zod';
import { ResetPasswordRequestSchema, resetPasswordRequestSchema } from 'app/validation/auth.validation';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';

export const PasswordResetInit = () => {
  const { control, handleSubmit } = useForm({
    resolver: zodResolver(resetPasswordRequestSchema),
  });

  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ email }) => {
    dispatch(handlePasswordResetInit(email));
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  const onSubmit: SubmitHandler<ResetPasswordRequestSchema> = data => {
    handleValidSubmit({ email: data.email });
  };

  return (
    <AuthContainer label="Quên mật khẩu">
      <Form<ResetPasswordRequestSchema> onSubmit={handleSubmit(onSubmit)} className="auth-form">
        <Flex direction="column" gap={176}>
          <FormInput control={control} name="email" label="Email" placeholder="Email" />

          <Flex direction="column">
            <Button color="primary" type="submit" data-cy="submit">
              Tiếp theo
            </Button>
          </Flex>
        </Flex>
      </Form>
    </AuthContainer>
  );
};

export default PasswordResetInit;
