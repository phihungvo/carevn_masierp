import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface IMachineryEquipmentUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const MachineryEquipmentUpdateSuccessModals = (props: IMachineryEquipmentUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  const onOk = () => {
    toggle();
    navigate(PATH.MACHINERY_EQUIPMENT);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-machinery-equipment-success"
      cancel={false}
      titleHeader="Cập nhật thành công"
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã cập nhật máy móc thiết bị thành công</Typography>
    </Modal>
  );
};

export default MachineryEquipmentUpdateSuccessModals;
