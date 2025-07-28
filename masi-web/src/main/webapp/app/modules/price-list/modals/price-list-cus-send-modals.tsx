import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useQuotations from 'app/hooks/use-quotations';
import React from 'react';

const { usePatchQuotationCustomerSend } = useQuotations;

interface IPriceListCusSendModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  toggleError: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const PriceListCusSendModals = (props: IPriceListCusSendModals) => {
  const { isOpen, toggle, toggleSuccess, toggleError, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = usePatchQuotationCustomerSend();

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
      okText="Gửi"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Gửi bảng báo giá cho khách hàng'
    >
      <Typography level={4}>Bạn muốn gửi bảng báo giá?</Typography>
    </Modal>
  );
};

export default PriceListCusSendModals;
