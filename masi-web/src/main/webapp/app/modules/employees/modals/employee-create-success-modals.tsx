import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';
import { useNavigate } from 'react-router';

interface IModalsTemplateCreateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeCreateSuccessModals = (props: IModalsTemplateCreateSuccess) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      onOk={() => navigate(-1)}
      titleHeader='Tạo hồ sơ nhân viên thành công'
    >
      <Typography level={4}>Bạn đã tạo mới hồ sơ nhân viên thành công</Typography>
    </Modal>
  );
};
