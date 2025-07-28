import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface IFactoriesUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const FactoriesUpdateSuccessModals = (props: IFactoriesUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  const onOk = () => {
    toggle && toggle();
    navigate(PATH.FACTORIES);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-supplies-success"
      cancel={false}
      titleHeader="Cập nhật thành công"
      style={{
        width: 400,
      }}
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã cập nhật nhà máy thành công</Typography>
    </Modal>
  );
};

export default FactoriesUpdateSuccessModals;
