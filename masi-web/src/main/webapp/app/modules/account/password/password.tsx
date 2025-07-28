import React, { useState, useEffect } from 'react';
import { translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Row, Col, Button } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { savePassword, reset } from './password.reducer';

export const PasswordPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success("Cập nhật mật khẩu thành công");
    } else if (errorMessage) {
      toast.error("Cập nhật mật khẩu thất bại");
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="password-title">
              Đổi mật khẩu {account.login}
          </h2>
          <ValidatedForm id="password-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="currentPassword"
              label={"Mật khẩu hiện tại"}
              placeholder={"Vui lòng nhập mật khẩu hiện tại"}
              type="password"
              validate={{
                required: { value: true, message: "Vui lòng nhập mật khẩu hiện tại" },
              }}
              data-cy="currentPassword"
            />
            <ValidatedField
              name="newPassword"
              label={"Mật khẩu mới"}
              placeholder={"Vui lòng nhập mật khẩu mới"}
              type="password"
              validate={{
                required: { value: true, message: "Vui lòng nhập mật khẩu mới" },
                minLength: { value: 4, message: "Mật khẩu phải có ít nhất 4 ký tự." },
                maxLength: { value: 50, message: "Mật khẩu không được vượt quá 50 ký tự." },
              }}
              onChange={updatePassword}
              data-cy="newPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="confirmPassword"
              label={"Mật khẩu xác nhận"}
              placeholder={"Vui lòng nhập mật khẩu"}
              type="password"
              validate={{
                required: { value: true, message: "Bạn phải nhập lại mật khẩu để xác nhận." },
                minLength: { value: 4, message: "Mật khẩu phải có ít nhất 4 ký tự." },
                maxLength: { value: 50, message: "Mật khẩu không được vượt quá 50 ký tự." },
                validate: v => v === password || translate('global.messages.error.dontmatch'),
              }}
              data-cy="confirmPassword"
            />
            <Button color="success" type="submit" data-cy="submit">
              Lưu
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordPage;
