import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useUniform from 'app/hooks/use-uniform';

const { useUpdateStatusUniform, useUniformById } = useUniform;

interface IUniformSettingsEnableModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const UniformSettingsEnableModals = (props: IUniformSettingsEnableModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { data: detail } = useUniformById(selectedRecord);
  const status = 'ENABLE';
  const { mutate, isPending } = useUpdateStatusUniform(selectedRecord, status, onOkSuccess);
  const onOk = () => {
    mutate();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-disable-uniform-settings"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Kích hoạt đồng phục'
    >
      <Typography level={4}>Bạn muốn kích hoạt đồng phục này?</Typography>
    </Modal>
  );
};

export default UniformSettingsEnableModals;
