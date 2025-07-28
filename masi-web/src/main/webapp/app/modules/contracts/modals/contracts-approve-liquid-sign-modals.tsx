import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useContracts from 'app/hooks/use-contracts';
import useFile from 'app/hooks/use-file';
import { IContract } from 'app/shared/model/contract.model';
import { IFIle } from 'app/shared/model/file.model';
import React, { useEffect, useRef, useState } from 'react';

const { usePatchContractReview } = useContracts;
const { usePostFile } = useFile;

interface IContractApproveLiquidSignModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (id: string) => void;
  record?: IContract
}

const ContractApproveLiquidSignModals = (props: IContractApproveLiquidSignModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, record } = props;



  const account = useAppSelector(state => state.authentication.account);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const isRole = record?.requestApprovals?.find(item => item?.employeeId === account?.id);
  const isCheck = record?.requestApprovals?.find(item => item?.employeeId === account?.id)?.result;

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate, isPending } = usePatchContractReview(toggle, toggleSuccess);
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
    if (isCheck) {
      toggle()
      return;
    }

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
    !isOpen && setFile(null);
  }, [isOpen]);

  const disabledOkBtn = isPending;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="modals-liquid-sign"
      disabledOk={disabledOkBtn}
      onOk={onOk}
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
                    <InputFile onFileChange={file => uploadFile(file)} name={file?.name} hidden ref={fileInputRef} />
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

export default ContractApproveLiquidSignModals;
