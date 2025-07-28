import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAuthoritiesUsersUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AuthoritiesUsersUpdateSuccessModals = (props: IAuthoritiesUsersUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="authorities-user-update-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật quyền cho người dùng thành công</Typography>
    </Modal>
  );
};

export default AuthoritiesUsersUpdateSuccessModals;
