import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface ISupplierUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const SupplierUpdateSuccessModals = (props: ISupplierUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  const onOk = () => {
    toggle();
    navigate(PATH.SUPPLIERS);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-supplier-success"
      cancel={false}
      titleHeader="Cập nhật thành công"
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã cập nhật nhà cung cấp thành công</Typography>
    </Modal>
  );
};

export default SupplierUpdateSuccessModals;
