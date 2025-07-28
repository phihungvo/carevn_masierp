import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IAuthoritiesGroupPermissionSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const AuthoritiesGroupPermissionSuccessModals = (props: IAuthoritiesGroupPermissionSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-group-success"
      cancel={false}
      titleHeade='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật quyền cho nhóm quyền thành công</Typography>
    </Modal>
  );
};

export default AuthoritiesGroupPermissionSuccessModals;
