import { Col, Row } from 'reactstrap';
import { toast } from 'react-toastify';
import { useForm } from 'react-hook-form';
import { translate } from 'react-jhipster';
import React, { useEffect, useRef, useState } from 'react';

import useFile from 'app/hooks/use-file';
import Form from 'app/components/form/form';
import Flex from 'app/components/flex/flex';
import Button from 'app/components/button/button';
import FormInput from 'app/components/form/form-input';
import InputFile from 'app/components/input/input-file';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import AccountSaveSuccessModals from 'app/modules/account/modals/account-save-success-modals';
import { FILE_UTIL } from 'app/constants/common';
import { IFIle } from 'app/shared/model/file.model';
import { zodResolver } from '@hookform/resolvers/zod';
import { saveAccountSettings, reset } from './settings.reducer';
import { getSession } from 'app/shared/reducers/authentication';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { AccountSchema, accountSchema } from 'app/validation/account.validation';

const { usePostFile } = useFile;

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [openAccountSaveSuccess, setOpenAccountSaveSuccess] = useState<boolean>(false);

  const toggleAccountSaveSucces = () => setOpenAccountSaveSuccess(!openAccountSaveSuccess);


  const { control, setValue, handleSubmit } = useForm<AccountSchema>({
    resolver: zodResolver(accountSchema),
  });

  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  useEffect(() => {
    if (account) {
      setValue('firstName', account.firstName);
      setValue('lastName', account.lastName);
      setValue('email', account.email);
      setValue('signatureId', account?.signatureId)
      if (account?.signatureId) {
        setFile({
          id: account?.signatureId,
          name: account?.signatureFileName,
        });
      }
    }
  }, [account]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
        signatureId: file?.id,
        signatureFileName: file?.name,
      }),
    );

    toggleAccountSaveSucces()
  };

  return (
    <div className='page_container'>
      <Row className="justify-content-start">
        <Col md="12">
          <h4 id="settings-title">Cập nhật thông tin cá nhân</h4>

          <Form onSubmit={handleSubmit(handleValidSubmit)}>
            <FormInput control={control} name="lastName" label="Họ" />
            <FormInput control={control} name="firstName" label="Tên" />
            <FormInput control={control} name="email" label="Email" />
            <FormInput control={control} name="signatureId" label="Chữ ký" type="hidden" />
            <Button
              key={new Date().getMilliseconds()}
              type="button"
              color="primary"
              onClick={() => fileInputRef.current?.click()}
              className="btn-upload"
              loading={loadingUpload}
            >
              <Flex align="center" gap={8}>
                <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
                Đính kèm chữ ký
                <InputFile
                  onFileChange={file =>
                    uploadFile(file, {
                      onSuccess: data => {
                        setValue('signatureId', data?.data?.id);
                      },
                    })
                  }
                  name="fileAttachment"
                  hidden
                  ref={fileInputRef}
                />
              </Flex>
            </Button>

            <div className="divider" />
            {file?.id && (
              <>
                <p className="attachment">Tệp đính kèm</p>
                <Flex flexWrap="wrap" gap={12}>
                  <AttachmentPreview
                    name={file?.name}
                    onClose={() => {
                      setFile(null);
                      setValue('signatureId', null);
                    }}
                    fileUrl={`${FILE_UTIL}/${file?.id}`}
                  />
                </Flex>
              </>
            )}
            <Button color="primary" style={{ float: 'right' }}>
              Cập nhật
            </Button>
          </Form>
        </Col>
      </Row>

      <AccountSaveSuccessModals isOpen={openAccountSaveSuccess} toggle={toggleAccountSaveSucces} />
    </div>
  );
};

export default SettingsPage;
