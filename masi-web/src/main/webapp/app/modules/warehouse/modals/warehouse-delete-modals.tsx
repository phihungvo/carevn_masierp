import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useWarehouse from 'app/hooks/use-warehouse';
import React from 'react';

const { useDeleteWarehouse } = useWarehouse;

interface IWarehouseDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const WarehouseDeleteModals = (props: IWarehouseDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteWarehouse(selectedRecord, onOkSuccess);

  const onOk = () => {
    mutate();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-warehouse-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa kho'
    >
      <Typography level={4}>Bạn muốn xoá kho này?</Typography>
    </Modal>
  );
};

export default WarehouseDeleteModals;
