import React, { useState, useEffect } from 'react';
import { translate } from 'react-jhipster';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';
import AuthContainer from 'app/shared/layout/auth/auth';
import Form from 'app/components/form/form';
import { SubmitHandler, useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { RegisterSchema, registerSchema } from 'app/validation/auth.validation';
import FormInput from 'app/components/form/form-input';
import Flex from 'app/components/flex/flex';
import { Button } from 'reactstrap';

export const RegisterPage = () => {
  const { control, handleSubmit } = useForm({
    resolver: zodResolver(registerSchema),
  });

  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const currentLocale = useAppSelector(state => state.locale.currentLocale);

  const handleValidSubmit = ({ username, email, firstPassword }) => {
    dispatch(handleRegister({ login: username, email, password: firstPassword, langKey: currentLocale }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const successMessage = useAppSelector(state => state.register.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  const onSubmit: SubmitHandler<RegisterSchema> = data => {
    handleValidSubmit({ username: data.username, email: data.email, firstPassword: data.password });
  };

  return (
    <AuthContainer label="Đăng ký">
      <Form<RegisterSchema> onSubmit={handleSubmit(onSubmit)} className="auth-form">
        <Flex direction="column" gap={56}>
          <Flex direction="column" gap={12}>
            <FormInput control={control} name="username" label="Tên NV" placeholder="Tên NV" autoFocus />
            <FormInput control={control} name="email" label="Email" placeholder="Email" />
            <FormInput
              control={control}
              name="password"
              label="Mật khẩu"
              placeholder="Mật khẩu"
              type="password"
              onChange={updatePassword}
            />
            <FormInput control={control} name="confirmPassword" label="Nhập lại mật khẩu" placeholder="Nhập lại mật khẩu" type="password" />
          </Flex>

          <Flex direction="column">
            <Button color="primary" type="submit" data-cy="submit">
              Đăng ký
            </Button>
          </Flex>
        </Flex>
      </Form>
    </AuthContainer>
  );
};

export default RegisterPage;
