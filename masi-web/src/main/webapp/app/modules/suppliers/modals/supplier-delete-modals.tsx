import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useSupplier from 'app/hooks/use-supplier';
import React from 'react';

const { useDeleteSupplier } = useSupplier;

interface ISupplierDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const SupplierDeleteModals = (props: ISupplierDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteSupplier(onOkSuccess);

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
      titleHeader="Xóa nhà cung cấp"
    >
      <Typography level={4}>Bạn muốn xoá nhà cung cấp này?</Typography>
    </Modal>
  );
};

export default SupplierDeleteModals;
