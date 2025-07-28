import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface AccountISaveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const AccountSaveSuccessModals = (props: AccountISaveSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="account-update-account-success" cancel={false}>
      <Typography level={3}>Cập nhật thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật thông tin cá nhân thành công</Typography>
    </Modal>
  );
};

export default AccountSaveSuccessModals;
