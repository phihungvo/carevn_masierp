import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import {
  useConfirmLiquidationSupplierContracts,
  useConfirmSupplierContracts,
} from 'app/hooks/use-supplier-contract';
import { IFIle } from 'app/shared/model/file.model';
import { useContext, useEffect, useRef, useState } from 'react';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const { usePostFile } = useFile;

const SupplierContractsApproveModal = (props: { isLiquidation?: boolean }) => {
  const {
    isOpenApprove,
    toggleApprove,
    selectedRecord,
    setSelectedRecord,
    toggleApproveSuccess,
  } = useContext(SupplierContractsContext);
  const { isLiquidation = false } = props;

  const account = useAppSelector(state => state.authentication.account);

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate: confirm, isPending: loadingConfirm } =
    useConfirmSupplierContracts();

  const { mutate: confirmLiq, isPending: loadingConfirmLiq } =
    useConfirmLiquidationSupplierContracts();

  const { mutate: uploadFile } = usePostFile(setFile);

  const onOk = () => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    const onSuccess = () => {
      toggleApprove();
      toggleApproveSuccess();
    };

    const body = { assignSign: file.id, approvedSignName: file?.name };

    if (!isLiquidation)
      confirm({ id: selectedRecord, data: body }, { onSuccess: onSuccess });
    else
      confirmLiq({ id: selectedRecord, data: body }, { onSuccess: onSuccess });

    setSelectedRecord(null);
    setFile(null);
  };

  useEffect(() => {
    if (account?.signatureId) {
      setFile({
        id: account?.signatureId,
        name: account?.signatureFileName,
      });
    }
  }, [account]);

  return (
    <Modal
      isOpen={isOpenApprove}
      toggle={toggleApprove}
      okText="Xác nhận"
      onOk={onOk}
      titleHeader="Đồng ý xét duyệt"
      disabledOk={!file}
      loadingOk={loadingConfirm || loadingConfirmLiq}
    >
      <Button
        key={new Date().getMilliseconds()}
        style={{ marginBottom: 24 }}
        type="button"
        color="primary"
        onClick={() => fileInputRef.current?.click()}
        className="btn-upload"
      >
        <Flex align="center" gap={8}>
          <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
          Đính kèm
          <InputFile
            onFileChange={file => uploadFile(file)}
            name={file?.name}
            hidden
            ref={fileInputRef}
          />
        </Flex>
      </Button>
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

export default SupplierContractsApproveModal;
