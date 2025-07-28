import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useQuotations from 'app/hooks/use-quotations';
import React from 'react';

const { usePatchQuotationInternalSend } = useQuotations;

interface IPriceListInternalApproveModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const PriceListInternalApproveModals = (props: IPriceListInternalApproveModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = usePatchQuotationInternalSend();

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync(selectedRecord)
        .then(() => toggleSuccess())
        .finally(() => {
          toggle();
          setSelectedRecord(null);
        });
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id => mutateAsync(id));
      Promise.all(promises)
        .then(() => toggleSuccess())
        .catch(() => { })
        .finally(() => {
          toggle();
          setSelectedRowKeys([]);
        });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-in-approve-pl"
      okText="Gửi duyệt"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Gửi bảng báo giá duyệt nội bộ'
    >
      <Typography level={4}>Bạn muốn gửi duyệt bảng báo giá?</Typography>
    </Modal>
  );
};

export default PriceListInternalApproveModals;
