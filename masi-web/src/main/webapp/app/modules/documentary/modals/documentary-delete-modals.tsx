import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useDocumentary from 'app/hooks/use-documentary';
import React from 'react';

const { useDeleteDocumentaryMutation } = useDocumentary;

interface IDocumentaryDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const DocumentaryDeleteModals = (props: IDocumentaryDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useDeleteDocumentaryMutation();

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
      titleHeader='Xóa công văn'
    >
      <Typography level={4}>Bạn muốn xoá công văn này?</Typography>
    </Modal>
  );
};

export default DocumentaryDeleteModals;
