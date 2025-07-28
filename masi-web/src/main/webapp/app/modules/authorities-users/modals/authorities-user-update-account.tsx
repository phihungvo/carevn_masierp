import { zodResolver } from '@hookform/resolvers/zod';
import FormInputPassword from 'app/components/form/form-input-password';
import Modal from 'app/components/modal/modal';
import WrapInputText from 'app/components/wrap-input-text/WrapInputText';
import useAccount from 'app/hooks/use-account';
import useModalRedux from 'app/hooks/use-modal-redux';
import {
  udpateAccountSchema,
  UpdateAccountSchema,
} from 'app/validation/account.validation';
import { isAxiosError } from 'axios';
import { useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';

const { useUpdateAccountMutation } = useAccount;

type Props = {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: any;
};

const AuthoritiesUserUpdateAccoutn = (props: Props) => {
  const { isOpen, selectedRecord, toggle } = props;

  const { handleToggleFailModal } = useModalRedux()

  const updateAccountForm = useForm<UpdateAccountSchema>({
    resolver: zodResolver(udpateAccountSchema),
    defaultValues: {
      username: selectedRecord?.role,
    },
  });
  const { handleSubmit, watch, reset } = updateAccountForm;
  const { username } = watch();

  const updateAccount = useUpdateAccountMutation(
    selectedRecord?.id,
    username,
  );

  const onSubmit = async (values: any) => {
    try {
      const res = await updateAccount.mutateAsync(values);
      if (res) {
        toggle();
      }
    } catch (err) {
      if (
        isAxiosError(err) &&
        err?.response?.data?.properties?.message === 'error.USERNAME_EXISTED'
      ) {
        updateAccountForm.setError('username', {
          message: 'Tên tài khoản đã tồn tại',
        });
      } else {
        toggle()
        handleToggleFailModal({
           content: 'Có lỗi xảy ra. Vui lòng thử lại sau',
        })
      }
    }
  };

  useEffect(() => {
    reset({
      username: selectedRecord?.userName,
    });
  }, [selectedRecord]);

  return (
    <FormProvider {...updateAccountForm}>
      <Modal
        titleHeader="Cập nhật tài khoản"
        isOpen={isOpen}
        toggle={toggle}
        okText="Cập nhật"
        cancelText="Trở về"
        onOk={handleSubmit(onSubmit)}
        onCancel={toggle}
      >
        <WrapInputText<UpdateAccountSchema>
          label="Tên tài khoản"
          name="username"
        />
        <FormInputPassword
          control={updateAccountForm.control}
          label="Mật khẩu"
          name="password"
          type="password"
        />
        <FormInputPassword
          control={updateAccountForm.control}
          label="Nhập lại mật khẩu"
          name="repeatPassword"
          type="password"
        />
        <WrapInputText<any> name="root" hidden />
      </Modal>
    </FormProvider>
  );
};

export default AuthoritiesUserUpdateAccoutn;
