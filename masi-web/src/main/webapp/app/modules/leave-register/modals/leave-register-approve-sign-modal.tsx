import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import { LEAVE_REGIME_STATUS } from 'app/shared/model/enumerations/leave-regime.model';
import { IFIle } from 'app/shared/model/file.model';
import React, { useEffect, useRef, useState } from 'react';

const { useReviewLeaveRegime, useGetLeaveRegimeById } = useLeaveRegime;
const { usePostFile } = useFile;

interface ILeaveRegisterApproveSignModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const LeaveRegisterApproveSignModals = (props: ILeaveRegisterApproveSignModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const onOkSuccess = () => {
    toggle();
    toggleSuccess();
    setSelectedRecord(null);
  };

  const { data } = useGetLeaveRegimeById(selectedRecord);
  const { mutate, isPending } = useReviewLeaveRegime(selectedRecord, onOkSuccess);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);

  const findReviewerId = data?.processLeaveRegimeRequests?.find(element => element?.approver?.id === account?.id)?.id;

  useEffect(() => {
    if (file) setIsDirty(false);
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
    if (!findReviewerId) {
      setIsDirty(false);
      return;
    }

    if (!file) {
      setIsDirty(true);
      return;
    }

    mutate({
      status: LEAVE_REGIME_STATUS.APPROVED,
      fileId: file?.id,
      reason: '',
      fileName: file?.name,
    });

    setFile(null);
  };

  useEffect(() => {
    !isOpen && setFile(null);
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      ok={!!findReviewerId}
      className="modals-approve-leave-register"
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader='Đồng ý xét duyệt'
    >
      {!findReviewerId ? (
        <p className="text-danger">Bạn không phải là người xét duyệt!</p>
      ) : (
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
      )}

      {file && <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />}
      {isDirty && <p className="text-danger">Vui lòng chọn chữ ký</p>}
    </Modal>
  );
};

export default LeaveRegisterApproveSignModals;
