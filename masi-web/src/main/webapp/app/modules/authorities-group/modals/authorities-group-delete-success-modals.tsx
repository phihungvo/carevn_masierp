import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IAuthoritiesGroupDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const AuthoritiesGroupDeleteSuccessModals = (props: IAuthoritiesGroupDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-group-success"
      cancel={false}
      titleHeader='Xóa nhóm quyền thành công'
    >
      <Typography level={4}>Bạn đã xóa nhóm quyền thành công</Typography>
    </Modal>
  );
};

export default AuthoritiesGroupDeleteSuccessModals;
