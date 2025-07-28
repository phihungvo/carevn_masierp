import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useUom from 'app/hooks/use-uom';
import React from 'react';

const { useDeleteUom } = useUom;

interface IUomDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const UomDeleteModals = (props: IUomDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteUom(selectedRecord, onOkSuccess);

  const onOk = () => {
    mutate();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-uom-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa đơn vị'
    >
      <Typography level={4}>Bạn muốn xoá đơn vị này?</Typography>
    </Modal>
  );
};

export default UomDeleteModals;
