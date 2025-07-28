import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import React from 'react';

const { useDeleteIncomingInvoice } = useIncomingInvoice;

interface IIncomingInvoiceDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const IncomingInvoiceDeleteModals = (props: IIncomingInvoiceDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useDeleteIncomingInvoice();

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
      className="modal-delete-order-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa hoá đơn'
    >
      <Typography level={4}>Bạn muốn xoá hoá đơn này?</Typography>
    </Modal>
  );
};

export default IncomingInvoiceDeleteModals;
