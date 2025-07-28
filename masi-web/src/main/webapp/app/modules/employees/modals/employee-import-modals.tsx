import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useEmployee from 'app/hooks/use-employee';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import { setImportResponse } from 'app/shared/reducers/employees';
import React, { useEffect, useRef, useState } from 'react';
import { Alert } from 'reactstrap';

const { usePostEmployeeImportMutation, useGetXlsxTemplate } = useEmployee;
const { usePostFile } = useFile;

interface IEmployeeImportModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const EmployeeImportModals = (props: IEmployeeImportModalsProps) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate, isPending, isError } = usePostEmployeeImportMutation(toggle, toggleSuccess);
  const { trigger, data, isFetching } = useGetXlsxTemplate();
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);

  useEffect(() => {
    file && setIsDirty(false);
  }, [file]);

  useEffect(() => {
    if (account?.signatureId) {
      setFile({
        id: account?.signatureId,
        name: account?.signatureFileName,
      });
    }
  }, [account, isOpen]);

  const onOk = async () => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    mutate(file?.id, {
      onSuccess: data => {
        dispatch(setImportResponse(data?.data));
      },
    });

    setFile(null);
  };

  const handleOnclickBtn = () => {
    trigger();
  };

  useDownloadXlsx(data?.data, 'xlsx-template', 'xlsx');

  useEffect(() => {
    if (isOpen) setFile(null);
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="modals-import-employee"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader='Tải lên NV'
    >
      <Flex gap={16}>
        <Button
          key={new Date().getMilliseconds()}
          style={{ marginBottom: 24 }}
          type="button"
          color="primary"
          onClick={() => fileInputRef.current?.click()}
          className="btn-upload"
          loading={loadingUpload}
        >
          <Flex align="center" gap={8}>
            <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
            Đính kèm
            <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
          </Flex>
        </Button>

        <Button
          style={{ marginBottom: 24 }}
          type="button"
          color="primary"
          onClick={() => handleOnclickBtn()}
          className="btn-upload"
          loading={isFetching}
        >
          <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
          Tải mẫu
        </Button>
      </Flex>

      {file && <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />}
      {!file && isDirty && <p className="text-danger">Vui lòng chọn tệp</p>}
      {isError && <Alert color="danger">Tải lên danh sách nhân viên thất bại</Alert>}
    </Modal>
  );
};

export default EmployeeImportModals;
