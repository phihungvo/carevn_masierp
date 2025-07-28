import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface IFactoriesCreateSuccessModals {
  isOpen: boolean;
  toggle?: () => void;
}

const FactoriesCreateSuccessModals = (props: IFactoriesCreateSuccessModals) => {
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
      className="modal-create-factories-success"
      cancel={false}
      titleHeader="Tạo mới thành công"
      style={{ width: 400 }}
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã tạo mới nhà máy thành công</Typography>
    </Modal>
  );
};

export default FactoriesCreateSuccessModals;
