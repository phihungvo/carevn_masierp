import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import usePaymentRequest from 'app/hooks/use-payment-request';
import { IFIle } from 'app/shared/model/file.model';
import { useEffect, useRef, useState } from 'react';

const { usePostFile } = useFile;
const { useApproveRequestPayment } = usePaymentRequest;

interface IAdvanceRequestApproveSignModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const AdvanceRequestApproveSignModals = (
  props: IAdvanceRequestApproveSignModalsProps,
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

  useEffect(() => {
    if (account?.signatureId) {
      setFile({
        id: account?.signatureId,
        name: account?.signatureFileName,
      });
    }
  }, [account, isOpen]);

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

export default AdvanceRequestApproveSignModals;
