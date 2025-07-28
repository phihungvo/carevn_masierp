import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import { FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import { useConfirmInventories } from 'app/hooks/use-inventories';
import { IFIle } from 'app/shared/model/file.model';
import { useContext, useEffect, useRef, useState } from 'react';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import Button from 'app/components/button/button';

const { usePostFile } = useFile;

const InventoriesApproveModal = () => {
  const {
    isOpenApprove,
    toggleApprove,
    selectedRecord,
    setSelectedRecord,
    toggleApproveSuccess,
  } = useContext(InventoriesStorageContext);

  const account = useAppSelector(state => state.authentication.account);

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate: confirm, isPending: loadingConfirm } =
    useConfirmInventories();
  const { mutate: uploadFile } = usePostFile(setFile);

  const onOk = () => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    confirm(
      {
        id: selectedRecord,
        data: { assignSign: file.id, approvedSignName: file?.name },
      },
      {
        onSuccess: () => {
          toggleApprove();
          toggleApproveSuccess();
        },
      },
    );
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
      loadingOk={loadingConfirm}
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

export default InventoriesApproveModal;
