import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useSupplier from 'app/hooks/use-supplier';
import React from 'react';

const { useDisableSupplier } = useSupplier;

interface ISupplierDisposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const SupplierDisposeModals = (props: ISupplierDisposeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useDisableSupplier();

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
      titleHeader="Vô hiệu NCC"
    >
      <Typography level={4}>Bạn muốn vô hiệu NCC này?</Typography>
    </Modal>
  );
};

export default SupplierDisposeModals;
