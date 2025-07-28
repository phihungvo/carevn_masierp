import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IAuthoritiesGroupUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const AuthoritiesGroupUpdateSuccessModals = (props: IAuthoritiesGroupUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-group-success"
      cancel={false}
      titleHeader='Cập nhật nhóm quyền thành công'
    >
      <Typography level={4}>Bạn đã cập nhật nhóm quyền thành công</Typography>
    </Modal>
  );
};

export default AuthoritiesGroupUpdateSuccessModals;
