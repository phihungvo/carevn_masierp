import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import React from 'react';
import { useNavigate } from 'react-router';

interface ISupplierCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const SupplierCreateSuccessModals = (props: ISupplierCreateSuccessModals) => {
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
      titleHeader="Tạo mới thành công"
      onOk={onOk}
    >
      <Typography level={4}>Bạn đã tạo mới nhà cung cấp thành công</Typography>
    </Modal>
  );
};

export default SupplierCreateSuccessModals;
