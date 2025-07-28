import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useFactoryLogistics from 'app/hooks/use-factory-logistics';
import React from 'react';

const { useDisableFactoryMutation } = useFactoryLogistics;

interface IFactoriesDisposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const FactoriesDisposeModals = (props: IFactoriesDisposeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useDisableFactoryMutation();

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
      className="modal-dispose-factories-success"
      okText="Xác nhận"
      onOk={onOk}
      isabledOk={isPending}
      titleHeader="Vô hiệu nhà máy"
    >
      <Typography level={4}>Bạn muốn vô hiệu nhà máy này?</Typography>
    </Modal>
  );
};

export default FactoriesDisposeModals;
