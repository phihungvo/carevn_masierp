import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useItems from 'app/hooks/use-items';
import React from 'react';

const { useDeleteItemMutation } = useItems;

interface ISuppliesDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const SuppliesDeleteModals = (props: ISuppliesDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useDeleteItemMutation(selectedRecord);

  const onOk = () => {
    if (selectedRecord)
      mutateAsync(selectedRecord)
        .then(() => toggleSuccess())
        .finally(() => {
          toggle();
        });

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id => mutateAsync(id));

      Promise.all(promises)
        .then(() => toggleSuccess())
        .catch(() => {})
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
      className="modal-delete-supplies-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader="Xóa mã VT - CCDC"
    >
      <Typography level={4}>Bạn muốn xoá mã VT - CCDC này?</Typography>
    </Modal>
  );
};

export default SuppliesDeleteModals;
