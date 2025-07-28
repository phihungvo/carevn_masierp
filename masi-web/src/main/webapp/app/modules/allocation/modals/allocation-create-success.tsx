import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IAllocationCreateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

const AllocationCreateSuccess = (props: IAllocationCreateSuccess) => {
  const { isOpen, toggle } = props;
  return (
    <Modal isOpen={isOpen} toggle={toggle} titleHeader="Tạo mới thành công">
      <Typography level={4}> Bạn đã tạo mới cuột gọi thành công</Typography>
    </Modal>
  );
};

export default AllocationCreateSuccess;
