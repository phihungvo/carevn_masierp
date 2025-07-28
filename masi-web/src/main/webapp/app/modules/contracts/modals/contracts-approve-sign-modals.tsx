import React, { useEffect, useRef, useState } from 'react';

import useFile from 'app/hooks/use-file';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import Button from 'app/components/button/button';
import useContracts from 'app/hooks/use-contracts'
import InputFile from 'app/components/input/input-file';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import { FILE_UTIL } from 'app/constants/common';
import { useAppSelector } from 'app/config/store';
import { IFIle } from 'app/shared/model/file.model';
import { IContract } from 'app/shared/model/contract.model';
import { Typography } from 'app/components/typography/typography';

const { usePatchContractNormalReview } = useContracts;
const { usePostFile } = useFile;

interface IContractsApproveSignModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  record?: IContract
}

const ContractsApproveSignModals = (props: IContractsApproveSignModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, record } = props;

  const account = useAppSelector(state => state.authentication.account);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate, isPending } = usePatchContractNormalReview(selectedRecord, toggle, toggleSuccess);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);

  const isRole = record?.normalApprovals?.find(item => item?.employeeId === account?.id);
  const isCheck = record?.normalApprovals?.find(item => item?.employeeId === account?.id)?.result;

  const disabledOk = isPending;

  const onOk = async () => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    mutate({
      documentId: selectedRecord,
      approvedSign: file?.id,
      approvedSignName: file?.name,
      isApproved: true
    });

    setSelectedRecord(null);
    setFile(null);
  };

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

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="contracs-approve-sign-modals"
      onOk={onOk}
      disabledOk={disabledOk}
      loadingOk={isPending}
      cancel={!!isRole && isCheck == undefined}
      ok={!!isRole && isCheck == undefined}
      titleHeader='Đồng ý xét duyệt'
    >
      {
        isRole ?
          <>
            {
              isCheck == undefined &&
              <>
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
              </>
            }
            {
              isCheck === true && <p className="text-danger">Bạn đã xét duyệt rồi, không được phép xét duyệt nữa!</p>
            }
            {
              isCheck === false && <p className="text-danger">Bạn đã từ chối rồi rồi, không được phép xét duyệt nữa!</p>
            }
          </> : <p className="text-danger">Bạn không phải là người xét duyệt đơn này!</p>
      }
    </Modal>
  );
};

export default ContractsApproveSignModals;
