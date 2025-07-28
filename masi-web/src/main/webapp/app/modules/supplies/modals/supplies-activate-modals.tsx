import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useItems from 'app/hooks/use-items';
import React from 'react';

const { useEnableItemMutation } = useItems;

interface ISuppliesActivateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const SuppliesActivateModals = (props: ISuppliesActivateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useEnableItemMutation();

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
      className="modal-dispose-supplies-success"
      okText="Xác nhận"
      onOk={onOk}
      isabledOk={isPending}
      titleHeader="Kích hoạt VT - CCDC"
    >
      <Typography level={4}>Bạn muốn kích hoạt VT - CCDC này?</Typography>
    </Modal>
  );
};

export default SuppliesActivateModals;
