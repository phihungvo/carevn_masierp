import React, { useEffect, useState } from 'react';

import Modal from 'app/components/modal/modal';
import useQuotations from 'app/hooks/use-quotations';
import { useAppSelector } from 'app/config/store';
import { IFIle } from 'app/shared/model/file.model';
import { Typography } from 'app/components/typography/typography';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';

const { usePatchQuotationCustomerProcess } = useQuotations;
// const { usePostFile } = useFile;

interface IPriceListApproveModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PriceListApproveModals = (props: IPriceListApproveModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);
  // const fileInputRef = useRef<HTMLInputElement>(null);

  // const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const { mutate, isPending } = usePatchQuotationCustomerProcess(selectedRecord, toggle, toggleSuccess);
  // const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);

  const disabledOkBtn = isPending;

  // useEffect(() => {
  //   file && setIsDirty(false);
  // }, [file]);

  useEffect(() => {
    if (account?.signatureId) {
      setFile({
        id: account?.signatureId,
        name: account?.signatureFileName,
      });
    }
  }, [account, isOpen]);

  const onOk = async () => {
    // if (!file) {
    //   setIsDirty(true);
    //   return;
    // }

    mutate({
      status: QUOTATION_STATUS.CUSTOMER_APPROVED,
      // fileId: file.id,
      // fileName: file.name,
    });

    // setFile(null);
  };

  useEffect(() => {
    !isOpen && setSelectedRecord(null);
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="modals-approve-pl"
      onOk={onOk}
      disabledOk={disabledOkBtn}
      loadingOk={isPending}
      cancel={false}
      titleHeader='Đồng ý xét duyệt'
    >
      <Typography level={4}>Bạn muốn xét duyệt bảng báo giá này?</Typography>

      {/* <Button
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
      </Button> */}

      {/* {file && <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />} */}
      {/* {!file && isDirty && <p className="text-danger">Vui lòng chọn chữ ký</p>} */}
    </Modal>
  );
};

export default PriceListApproveModals;
