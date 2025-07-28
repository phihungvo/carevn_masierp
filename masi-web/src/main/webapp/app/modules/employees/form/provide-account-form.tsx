import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import useAccount from 'app/hooks/use-account';
import useEmployee from 'app/hooks/use-employee';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { createAccountSchema, CreateAccountSchema } from 'app/validation/account.validation';
import React from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetEmployeeProfileByIdQuery } = useEmployee;
const { useCreateEmployeeAccount, useGetAccountById, useGetAccountByUsername } = useAccount;

interface IProvideAccountFormProps {
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const ProvideAccountForm = (props: IProvideAccountFormProps) => {
  const { toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;
  const { control, handleSubmit, setValue,
    setError,
    formState: { errors }
  } = useForm<CreateAccountSchema>({
    resolver: zodResolver(createAccountSchema),
  });
  const { data: employeeProfile } = useGetEmployeeProfileByIdQuery(selectedRecord);
  const { data: account } = useGetAccountById(selectedRecord);

  React.useEffect(() => {
    if (employeeProfile) {
      setValue('username', employeeProfile.employeeCode);
    }
    if (account) {
      setValue('username', account.userName);
    }
  }, [employeeProfile, account]);

  const { mutate } = useCreateEmployeeAccount();
  const { mutateAsync } = useGetAccountByUsername();
  const onSubmit: SubmitHandler<CreateAccountSchema> = async data => {
    const { fullName, id: employeeId, email } = employeeProfile ?? {};
    const fullNameArr = fullName?.split(' ') || [];
    const firstName = fullNameArr?.pop() || '';
    const lastName = fullNameArr?.join(' ');

    // try {
    //   const result = await mutateAsync(data.username);

    //   if (result && result?.id !== employeeId) {
    //     setError('username', {
    //       type: 'onChange',
    //       message: 'Tên đăng nhập đã tồn tại',
    //     });
    //     return;
    //   }
    // } catch {
    //   return;
    // }

    mutate({
      userName: data.username,
      email,
      employeeId,
      firstName,
      lastName,
      imageUrl: '',
      id: employeeId,
      // password: data.password,
    });

    setSelectedRecord(null);
    toggle();
  };

  return (
    // eslint-disable-next-line @typescript-eslint/no-misused-promises
    <Form id={FORM.CREATE_ACCOUNT} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={12}>
          <FormInput
            control={control} name="username" label="Tên đăng nhập" />
        </Col>


      </Row>
      {/* <Row>
        <Col md={12}>
          <FormInput control={control} type='password' name="password" label="Mật khẩu" />
        </Col>
      </Row>
      <Row>
        <Col md={12}>
          <FormInput control={control} type='password' name="repeatPassword" label="Nhập lại mật khẩu" />
        </Col>
      </Row> */}
    </Form>
  );
};

export default ProvideAccountForm;
