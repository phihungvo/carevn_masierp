import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useQuotations from 'app/hooks/use-quotations';
import React from 'react';

const { usePatchQuotationCancel } = useQuotations;

interface IPriceListCancelModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  toggleError: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const PriceListCancelModals = (props: IPriceListCancelModals) => {
  const { isOpen, toggle, toggleSuccess, toggleError, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = usePatchQuotationCancel();

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync(selectedRecord)
        .then(() => toggleSuccess())
        .catch(() => toggleError())
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
        .catch(() => toggleError())
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
      className="modal-create-pl-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Hủy bảng báo giá'
    >
      <Typography level={4}>Bạn muốn hủy bảng báo giá này?</Typography>
    </Modal>
  );
};

export default PriceListCancelModals;
