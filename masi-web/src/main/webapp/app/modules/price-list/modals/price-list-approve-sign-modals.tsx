import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import React, { useEffect, useRef, useState } from 'react';
import useQuotations from 'app/hooks/use-quotations';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';

const { usePatchQuotationInternalApprove } = useQuotations;
const { usePostFile } = useFile;

interface IPriceListApproveSignModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PriceListApproveSignModals = (props: IPriceListApproveSignModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);


  const { mutate, isPending } = usePatchQuotationInternalApprove(selectedRecord, toggle, toggleSuccess);
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

  useEffect(() => {
    !isOpen && setFile(null);
  }, [isOpen]);

  const onOk = async () => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    mutate({
      status: QUOTATION_STATUS.APPROVED,
      approvalSignFile: file?.id,
      approvalSignName: file?.name
    });

    setSelectedRecord(null);
    setFile(null);
  };

  const disabledOk = isPending;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="price-list-approve-sign-modals"
      onOk={onOk}
      disabledOk={disabledOk}
      loadingOk={isPending}
      titleHeader='Đồng ý xét duyệt'
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
          <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
        </Flex>
      </Button>

      {file && <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />}
      {!file && isDirty && <p className="text-danger">Vui lòng chọn chữ ký</p>}
    </Modal>
  );
};

export default PriceListApproveSignModals;
