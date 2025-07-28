import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useItems from 'app/hooks/use-items';
import React from 'react';

const { useDisableItemMutation } = useItems;

interface ISuppliesDisposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const SuppliesDisposeModals = (props: ISuppliesDisposeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useDisableItemMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess() {
        toggle();
        toggleSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-dispose-supplies-success"
      okText="Xác nhận"
      onOk={onOk}
      isabledOk={isPending}
      titleHeader="Vô hiệu VT - CCDC"
    >
      <Typography level={4}>Bạn muốn vô hiệu VT - CCDC này?</Typography>
    </Modal>
  );
};

export default SuppliesDisposeModals;
