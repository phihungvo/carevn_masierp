import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface ISuppliesCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const SuppliesCreateSuccessModals = (props: ISuppliesCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  const onOk = () => {
    toggle();
    navigate(PATH.SUPPLIES);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-supplies-success"
      cancel={false}
      titleHeader="Tạo mới thành công"
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã tạo mới hàng hoá thành công</Typography>
    </Modal>
  );
};

export default SuppliesCreateSuccessModals;
