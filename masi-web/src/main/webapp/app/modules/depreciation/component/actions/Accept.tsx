import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { FILE_UTIL } from 'app/constants/common';
import useAccountApp from 'app/hooks/use-account-app';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import { useMemo, useRef, useState } from 'react';
import { useApproveDepreciation } from '../../apis/hook';

const { usePostFile } = useFile;

type Props = {
  isOpen: boolean;
  id_detail: string;
  toggle?: () => void;
};

const Accept = (props: Props) => {
  const { isOpen, id_detail, toggle } = props;

  const account = useAccountApp()
  const [step, setStep] = useState<'action' | 'modal'>('action');
  const [status, setStatus] = useState<'init' | 'success' | 'fail'>('init');
  const [file, setFile] = useState<IFIle | null>(
    account?.signatureId && account?.signatureFileName ? {
      id: account?.signatureId,
      name: account?.signatureFileName,
    } : null
  )
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [error, setError] = useState<string>('')

  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile, () => {
    setError('Lỗi upload file');
  });

  const acceptMutation = useApproveDepreciation(
    id_detail,
    () => {
      setStep('modal');
      setStatus('success');
    },
    () => {
      setStep('modal');
      setStatus('fail');
    }
  );

  const modalLayout = useMemo(() => {
    switch (status) {
      case 'init':
        return {
          header_title: 'Yêu cầu phê duyệt',
          content: (
            <>
              <Button
                key={new Date().getMilliseconds()}
                type="button"
                color="primary"
                onClick={() => fileInputRef.current?.click()}
                className="btn-upload"
                loading={loadingUpload}
              >
                <Flex align="center" gap={8}>
                  <img
                    src="content/images/vuesax/linear/paperclip.svg"
                    alt="attach"
                  />
                  Đính kèm
                  <InputFile
                    onFileChange={newFile => {
                      setError('');
                      setFile(null);
                      uploadFile(newFile);
                    }}
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

              {error && <p className="text-danger">{error}</p>}
            </>
          ),
          okText: 'Xác nhận',
          step: 'action',
        };
      case 'success':
        return {
          header_title: 'Thành công',
          content: 'Phê duyệt thành công',
          okText: 'Trở về',
          step: 'modal',
        };
      default:
        return {
          header_title: 'Thất bại',
          content: 'Có lỗi xảy ra, vui lòng thử lại sau',
          okText: 'Trở về',
          step: 'modal',
        };
    }
  }, [status, file, loadingUpload, error]);

  const onCloseModal = () => {
    setStatus('init');
    setStep('action')
    setFile({ id: account?.signatureId, name: account?.signatureFileName });
    setError('');
    toggle();
  };
  console.log('file', file);

  const onAccept = () => {
    if (!file || !file?.id || !file?.name) {
      setError('Vui lòng chọn chữ ký');
      return;
    }
    acceptMutation.mutate({ approvedSign: file?.id, approvedSignName: file?.name ?? '', isApproved: true });
  }

  return (
    <Modal
      isOpen={isOpen}
      toggle={onCloseModal}
      okText={modalLayout.okText}
      className="modal-voucher-request-payment-approve-sign"
      onOk={step === 'action' ? onAccept : onCloseModal}
      disabledOk={acceptMutation.isPending}
      loadingOk={acceptMutation.isPending}
      titleHeader={modalLayout.header_title}
      cancel={step === 'action'}
    >
      {modalLayout.content}
    </Modal>
  );
};

export default Accept;
