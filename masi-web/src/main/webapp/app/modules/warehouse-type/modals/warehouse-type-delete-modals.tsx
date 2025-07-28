import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useWarehouseType from 'app/hooks/use-warehouse-type';
import React from 'react';

const { useDeleteWarehouseType } = useWarehouseType;

interface IWarehouseTypeDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const WarehouseTypeDeleteModals = (props: IWarehouseTypeDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteWarehouseType(selectedRecord, onOkSuccess);

  const onOk = () => {
    mutate();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-warehouse-type-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa loại kho'
    >
      <Typography level={4}>Bạn muốn xoá loại kho này?</Typography>
    </Modal>
  );
};

export default WarehouseTypeDeleteModals;
