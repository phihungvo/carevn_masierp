import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';
import { useNavigate } from 'react-router';

interface IModalsTemplateCreateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalsTemplateCreateSuccess = (props: IModalsTemplateCreateSuccess) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  return (
    <Modal isOpen={isOpen} toggle={toggle} cancel={false} okText="Đồng ý" onOk={() => navigate(-1)}>
      <Typography level={3}>Tạo biểu mẫu thành công</Typography>
      <Typography level={4}>Bạn đã tạo mới biểu mẫu thành công</Typography>
    </Modal>
  );
};

interface IModalsTemplateUpdateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalsTemplateUpdateSuccess = (props: IModalsTemplateUpdateSuccess) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      className="modals-template-update-success"
      onOk={() => navigate(-1)}
    >
      <Typography level={3}>Cập nhật biểu mẫu thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật biểu mẫu thành công</Typography>
    </Modal>
  );
};
