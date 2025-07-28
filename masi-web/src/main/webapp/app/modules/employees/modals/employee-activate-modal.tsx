import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useAccount from 'app/hooks/use-account';
import useEmployee from 'app/hooks/use-employee';
import React from 'react';

const { usePatchEnableEmployeeProfileMutation } = useEmployee;
const { useToggleActivate } = useAccount;
interface IEmployeeActivateModalProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (id: string | null) => void;
}

const EmployeeActivateModal = (props: IEmployeeActivateModalProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutate } = usePatchEnableEmployeeProfileMutation(selectedRecord, toggle, toggleSuccess);
  const { mutate: toggleActivate } = useToggleActivate();
  const onOk = () => {
    mutate();
    setSelectedRecord(null);
    toggleActivate(selectedRecord);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      onOk={onOk}
      titleHeader='Kích hoạt lại hồ sơ NV'
    >
      <Typography level={4}>Bạn muốn Kích hoạt lại hồ sơ NV này?</Typography>
    </Modal>
  );
};

export default EmployeeActivateModal;
