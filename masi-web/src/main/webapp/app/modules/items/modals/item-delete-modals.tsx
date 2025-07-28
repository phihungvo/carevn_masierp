import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useItems from 'app/hooks/use-items';
import React from 'react';

const { useDeleteItemMutation } = useItems;

interface IItemDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const ItemDeleteModals = (props: IItemDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteItemMutation(selectedRecord, onOkSuccess);

  const onOk = () => {
    mutate(selectedRecord);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-uom-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa vật phẩm'
    >
      <Typography level={4}>Bạn muốn xoá vật phẩm này?</Typography>
    </Modal>
  );
};

export default ItemDeleteModals;
