import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import React, { useEffect, useRef, useState } from 'react';

const { usePostFile } = useFile;
const { usePostEmployeeImportMutation } = useEmployee;

interface IOfficesImportModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const OfficesImportModals = (props: IOfficesImportModalsProps) => {
  const { isOpen, toggle } = props;

  const account = useAppSelector(state => state.authentication.account);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate, isPending } = usePostEmployeeImportMutation(toggle);
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

    mutate(file?.id);

    setFile(null);
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} okText="Xác nhận" className="modals-import-offices" onOk={onOk} disabledOk={isPending}>
      <Typography level={3}>Tải lên NV</Typography>

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

      {file && <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />}
      {!file && isDirty && <p className="text-danger">Vui lòng chọn chữ ký</p>}
    </Modal>
  );
};

export default OfficesImportModals;
