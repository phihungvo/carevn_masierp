import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useUom from 'app/hooks/use-uom';
import React from 'react';

const { useDeleteUomGroup } = useUom;

interface IUomGroupDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const UomGroupDeleteModals = (props: IUomGroupDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteUomGroup(selectedRecord, onOkSuccess);

  const onOk = () => {
    mutate();
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-delete-uom-success" okText="Xác nhận" disabledOk={isPending} onOk={onOk}>
      <Typography level={3}>Xóa nhóm đơn vị</Typography>
      <Typography level={4}>Bạn muốn xoá nhóm đơn vị này?</Typography>
    </Modal>
  );
};

export default UomGroupDeleteModals;
