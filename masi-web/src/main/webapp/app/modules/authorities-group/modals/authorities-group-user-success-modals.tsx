import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IAuthoritiesGroupUserSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const AuthoritiesGroupUserSuccessModals = (props: IAuthoritiesGroupUserSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-group-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật tài khoản cho nhóm quyền thành công</Typography>
    </Modal>
  );
};

export default AuthoritiesGroupUserSuccessModals;
