import React, { useEffect } from 'react';
import { translate } from 'react-jhipster';
import { useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import { handlePasswordResetFinish, reset } from '../password-reset.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import AuthContainer from 'app/shared/layout/auth/auth';
import { SubmitHandler, useForm } from 'react-hook-form';
import Form from 'app/components/form/form';
import FormInputPassword from 'app/components/form/form-input-password';
import Flex from 'app/components/flex/flex';
import { Button } from 'reactstrap';
import { zodResolver } from '@hookform/resolvers/zod';
import { ResetPasswordSchema, resetPasswordSchema } from 'app/validation/auth.validation';

export const PasswordResetFinishPage = () => {
  const { control, handleSubmit } = useForm({
    resolver: zodResolver(resetPasswordSchema),
  });
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ newPassword }) => dispatch(handlePasswordResetFinish({ key, newPassword }));

  const onSubmit: SubmitHandler<ResetPasswordSchema> = data => {
    handleValidSubmit({
      newPassword: data.newPassword,
    });
  };

  const getResetForm = () => {
    return (
      <Form<ResetPasswordSchema> onSubmit={handleSubmit(onSubmit)} className="auth-form">
        <Flex direction="column" gap={56}>
          <Flex direction="column" gap={12}>
            <FormInputPassword control={control} name="oldPassword" label="Mật khẩu cũ" />
            <FormInputPassword control={control} name="newPassword" label="Mật khẩu mới" />
          </Flex>
          <Flex direction="column">
            <Button color="primary" type="submit" data-cy="submit">
              Cập nhật
            </Button>
          </Flex>
        </Flex>
        {/* <ValidatedField
          name="newPassword"
          label={translate('global.form.newpassword.label')}
          placeholder={translate('global.form.newpassword.placeholder')}
          type="password"
          validate={{
            required: { value: true, message: translate('global.messages.validate.newpassword.required') },
            minLength: { value: 4, message: translate('global.messages.validate.newpassword.minlength') },
            maxLength: { value: 50, message: translate('global.messages.validate.newpassword.maxlength') },
          }}
          onChange={updatePassword}
          data-cy="resetPassword"
        />
        <PasswordStrengthBar password={password} />
        <ValidatedField
          name="confirmPassword"
          label={translate('global.form.confirmpassword.label')}
          placeholder={translate('global.form.confirmpassword.placeholder')}
          type="password"
          validate={{
            required: { value: true, message: translate('global.messages.validate.confirmpassword.required') },
            minLength: { value: 4, message: translate('global.messages.validate.confirmpassword.minlength') },
            maxLength: { value: 50, message: translate('global.messages.validate.confirmpassword.maxlength') },
            validate: v => v === password || translate('global.messages.error.dontmatch'),
          }}
          data-cy="confirmResetPassword"
        />
        <Button color="success" type="submit" data-cy="submit">
          <Translate contentKey="reset.finish.form.button">Validate new password</Translate>
        </Button> */}
      </Form>
    );
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  return <AuthContainer label="Cập nhật mật khẩu">{key ? getResetForm() : null}</AuthContainer>;
};

export default PasswordResetFinishPage;
