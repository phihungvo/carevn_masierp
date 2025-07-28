import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useSupplier from 'app/hooks/use-supplier';
import React from 'react';

const { useEnableSupplier } = useSupplier;

interface ISupplierActivateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const SupplierActivateModals = (props: ISupplierActivateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useEnableSupplier();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        toggle();
        toggleSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-dispose-supplier-success"
      okText="Xác nhận"
      onOk={onOk}
      isabledOk={isPending}
      titleHeader="Kích hoạt NCC"
    >
      <Typography level={4}>Bạn muốn kích hoạt NCC này?</Typography>
    </Modal>
  );
};

export default SupplierActivateModals;
