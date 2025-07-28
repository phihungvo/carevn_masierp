import React, { useEffect, useRef, useState } from 'react';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import useFile from 'app/hooks/use-file';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import InputFile from 'app/components/input/input-file';
import { FILE_UTIL } from 'app/constants/common';
import usePaymentRequest from 'app/hooks/use-payment-request';
import { IFIle } from 'app/shared/model/file.model';
import { useAppSelector } from 'app/config/store';

const { usePostFile } = useFile;
const { useApproveRequestPayment } = usePaymentRequest;

interface IRequestPaymentApproveSignModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const RequestPaymentApproveSignModals = (
  props: IRequestPaymentApproveSignModalsProps,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate, isPending } = useApproveRequestPayment(
    selectedRecord,
    toggle,
    toggleSuccess,
  );
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

  const onOk = () => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    mutate({
      approvedSign: file?.id,
      approvedSignName: file?.name ?? '',
    });

    setFile(null);
  };

  const disabledOk = isPending;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="modal-voucher-request-payment-approve-sign"
      onOk={onOk}
      disabledOk={disabledOk}
      loadingOk={isPending}
      titleHeader="Đồng ý xét duyệt"
    >
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
          Đính kèm
          <InputFile
            onFileChange={file => uploadFile(file)}
            name="fileAttachment"
            hidden
            ref={fileInputRef}
          />
        </Flex>
      </Button>

      <div style={{ height: '16px' }} />

      {file && (
        <AttachmentPreview
          name={file?.name}
          onClose={() => setFile(null)}
          fileUrl={`${FILE_UTIL}/${file?.id}`}
        />
      )}

      {!file && isDirty && <p className="text-danger">Vui lòng chọn chữ ký</p>}
    </Modal>
  );
};

export default RequestPaymentApproveSignModals;
