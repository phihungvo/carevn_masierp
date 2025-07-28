import './index.scss';
import React from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { login } from 'app/shared/reducers/authentication';
import { Translate } from 'react-jhipster';
import { Button, Alert, Row, Col } from 'reactstrap';
import { Link } from 'react-router-dom';
import { SubmitHandler, useForm } from 'react-hook-form';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import { LoginSchema, loginSchema } from 'app/validation/auth.validation';
import { zodResolver } from '@hookform/resolvers/zod';
import FormInputPassword from 'app/components/form/form-input-password';
import AuthContainer from 'app/shared/layout/auth/auth';
import { PATH } from 'app/constants/path';
import { permissions } from 'app/config/permission';



export const Login = () => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);

  const navigate = useNavigate();
  const pageLocation = useLocation();
  const { control, handleSubmit } = useForm<LoginSchema>({
    resolver: zodResolver(loginSchema),
  });

  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const checkAndGetInitPage = (initPage: string) => {
    const isSuperAdmin = authorities?.indexOf('ROLE_SUPER_ADMIN') != -1;
    const isAdmin = authorities?.indexOf('ROLE_ADMIN') != -1;
    if (isSuperAdmin || isAdmin) { return initPage; }
    const groupMapPermission = {}
    permissions.forEach(permission => {
      Object.keys(permission.actions).forEach(action => {
        groupMapPermission[`PERMISSION.${permission.key}.${action}`] = permission
      })
    })
    const isHasRoleInit = authorities?.filter(it => groupMapPermission[it]?.path === initPage)?.length > 0;
    if(isHasRoleInit) { return initPage; }

    const userPermissions = authorities?.filter(it => it.includes("PERMISSION."))

    if(userPermissions.length > 0 && (userPermissions[0] in groupMapPermission) ) { return groupMapPermission[userPermissions[0]].path; }

    return "/";
  }

  const { from } = pageLocation.state || { from: { pathname: checkAndGetInitPage(PATH.TIME_SHEET), search: pageLocation.search } };
  if (isAuthenticated) {
    return <Navigate to={from} replace />;
  }

  const onSubmit: SubmitHandler<LoginSchema> = data => {
    handleLogin(data.username, data.password);
  };

  return (
    <AuthContainer label="Đăng nhập">
      <Form<LoginSchema> onSubmit={handleSubmit(onSubmit)} className="auth-form">
        <Row>
          <Col md="12">
            {loginError ? (
              <Alert color="danger" data-cy="loginError">
                <Translate contentKey="login.messages.error.authentication">
                  <strong>Đăng nhập thất bại!</strong> Vui lòng kiểm tra lại thông tin đăng nhập.
                </Translate>
              </Alert>
            ) : null}
          </Col>
          <Col md="12">
            <Flex direction="column" gap={12}>
              <FormInput control={control} name="username" label={'Tài khoản'} placeholder={'Tài khoản'} autoFocus />
              <Flex direction="column">
                <FormInputPassword control={control} name="password" type="password" label={'Mật khẩu'} placeholder={'Mật khẩu'} />
                {/* <Link to={'/account/reset/request'} className="forget-password-link">
                  Quên mật khẩu
                </Link> */}
              </Flex>
            </Flex>
          </Col>
        </Row>
        <div className="mt-1">&nbsp;</div>

        <Flex direction="column" gap={18}>
          <Button color="primary" type="submit" data-cy="submit">
            Đăng nhập
          </Button>
          {/* <Button color="primary" onClick={() => navigate('/account/register')} tabIndex={1}>
            Đăng ký
          </Button> */}
        </Flex>
      </Form>
    </AuthContainer>
  );
};

export default Login;
