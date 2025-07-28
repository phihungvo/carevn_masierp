import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IModalsTemplateUpdateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const ProvideAccountSuccessModals = (props: IModalsTemplateUpdateSuccess) => {
  const { isOpen, toggle } = props;

  // const navigate = useNavigate();

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      titleHeader='Cập nhật tài khoản thành công'
    >
      <Typography level={4}>Bạn đã cập nhật  tài khoản thành công
      </Typography>
    </Modal>
  );
};
