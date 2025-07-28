import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useGroup from 'app/hooks/use-group';
import React from 'react';

const { useDeleteGroup } = useGroup;

interface IAuthoritiesGroupDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const AuthoritiesGroupDeleteModals = (props: IAuthoritiesGroupDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const onOkSuccess = () => {
    toggleSuccess();
    toggle();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteGroup(selectedRecord, onOkSuccess);

  const onOk = () => {
    mutate();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-group-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa nhóm quyền'
    >
      <Typography level={4}>Bạn muốn xoá nhóm quyền này?</Typography>
    </Modal>
  );
};

export default AuthoritiesGroupDeleteModals;
