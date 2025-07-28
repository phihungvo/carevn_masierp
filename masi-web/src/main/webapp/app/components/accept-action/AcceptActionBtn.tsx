import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { FILE_UTIL } from 'app/constants/common';
import useAccountApp from 'app/hooks/use-account-app';
import useFile from 'app/hooks/use-file';
import useSuppliesRequest from 'app/hooks/use-supplies-request';
import { IFIle } from 'app/shared/model/file.model';
import { useEffect, useMemo, useRef, useState } from 'react';
import ModalWrapper from '../ButtonV2/ModalWrapper';
import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import useModalRedux from 'app/hooks/use-modal-redux';

const { useApproveSignSuppliesRequest } = useSuppliesRequest;
const { usePostFile } = useFile;

type Props = {
  label?: string;
  axiosFunc: any
  invalidationKey: string
  disabled?: boolean;
  render?: (onToggle: () => void) => JSX.Element
};

const AcceptActionBtn = (props: Props) => {
  const { axiosFunc, label = 'Phê duyệt', disabled, invalidationKey } = props;

  const queryClient = useQueryClient()
  const account = useAccountApp();
  const [step, setStep] = useState<'action' | 'modal'>('action');
  const [status, setStatus] = useState<'init' | 'success' | 'fail'>('init');
  const [file, setFile] = useState<IFIle | null>({
    id: account?.signatureId,
    name: account?.signatureFileName,
  });
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [error, setError] = useState<string>('');

  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(
    setFile,
    () => {
      setError('Lỗi upload file');
    },
  );

  const acceptMutation = useMutation({
    mutationFn: axiosFunc,
    onSuccess: () => {
      setStep('modal');
      setStatus('success');
      queryClient.invalidateQueries({
        queryKey: [invalidationKey]
      })
    },
    onError: () => {
      setStep('modal');
      setStatus('fail');
    },
  })

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
    setStep('action');
    setFile({ id: account?.signatureId, name: account?.signatureFileName });
    setError('');
  };

  const onAccept = () => {
    if (!file) {
      setError('Vui lòng chọn chữ ký');
      return;
    }
    (acceptMutation as any).mutate({
      approvedSign: file?.id,
      approvedSignName: file?.name ?? '',
    });
  };

  return (
    <ModalWrapper
      renderTarget={({ onToggle }) => (
        props.render ? props.render(onToggle) : (
          <Button
            onClick={onToggle}
            disabled={disabled}
            className="btn-approve"
          >
            {label}
          </Button>
        )
      )}
      renderModal={() => modalLayout?.content}
      toggle={onCloseModal}
      okText={modalLayout.okText}
      className="modal-voucher-request-payment-approve-sign"
      onOk={step === 'action' ? onAccept : onCloseModal}
      disabledOk={acceptMutation?.['isPending']}
      loadingOk={acceptMutation?.['isPending']}
      titleHeader={modalLayout.header_title}
      cancel={step === 'action'}
    />
  );
};

export default AcceptActionBtn;
