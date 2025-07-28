import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import usePurchase from 'app/hooks/use-purchase';
import { PURCHASE_STATUS } from 'app/shared/model/enumerations/purchase.model';
import { IFIle } from 'app/shared/model/file.model';
import React, { useEffect, useRef, useState } from 'react';

const { useGetPurchaseReviewByPurchaseRequestId, usePatchPurchaseReviewMutation } = usePurchase;
const { usePostFile } = useFile;

interface IPurchaseApproveSignModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PurchaseApproveSignModals = (props: IPurchaseApproveSignModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { data } = useGetPurchaseReviewByPurchaseRequestId(selectedRecord);
  const { mutate, isPending } = usePatchPurchaseReviewMutation(toggle, toggleSuccess);
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

  const findReviewerId = data?.find(purchaseReview => purchaseReview?.employeeId === account?.id)?.id;

  const onOk = async () => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    mutate({
      id: findReviewerId,
      status: PURCHASE_STATUS.APPROVED,
      approvalStatusFile: file?.id,
    });
    setSelectedRecord(null);
    setFile(null);
  };

  const disabledOkBtn = isPending || !findReviewerId;

  useEffect(() => {
    !isOpen && setFile(null);
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="modals-approve-sign"
      onOk={onOk}
      disabledOk={disabledOkBtn}
      loadingOk={isPending}
    >
      <Typography level={3}>Đồng ý xét duyệt</Typography>

      {!isPending && !findReviewerId ? (
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
            <InputFile onFileChange={file => uploadFile(file)} name={file?.name} hidden ref={fileInputRef} />
          </Flex>
        </Button>
      )}
      {file && <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />}
      {!file && isDirty && <p className="text-danger">Vui lòng chọn chữ ký</p>}
    </Modal>
  );
};

export default PurchaseApproveSignModals;
