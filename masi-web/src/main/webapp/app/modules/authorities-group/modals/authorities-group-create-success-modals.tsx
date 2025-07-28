import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IAuthoritiesGroupCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const AuthoritiesGroupCreateSuccessModals = (props: IAuthoritiesGroupCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-group-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới nhóm quyền thành công</Typography>
    </Modal>
  );
};

export default AuthoritiesGroupCreateSuccessModals;
