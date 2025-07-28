import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useFactoryLogistics from 'app/hooks/use-factory-logistics';
import React from 'react';

const { useEnableFactoryMutation } = useFactoryLogistics;

interface IFactoriesActivateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const FactoriesActivateModals = (props: IFactoriesActivateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useEnableFactoryMutation();

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
      titleHeader="Kích hoạt nhà máy"
    >
      <Typography level={4}>Bạn muốn kích hoạt nhà máy này?</Typography>
    </Modal>
  );
};

export default FactoriesActivateModals;
