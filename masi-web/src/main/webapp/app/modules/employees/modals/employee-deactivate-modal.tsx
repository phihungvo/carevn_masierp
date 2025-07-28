import React from 'react';

import Modal from 'app/components/modal/modal';
import useAccount from 'app/hooks/use-account';
import useEmployee from 'app/hooks/use-employee';
import { Typography } from 'app/components/typography/typography';

const { usePatchDisableEmployeeProfileMutation } = useEmployee;
const { useToggleActivate } = useAccount
interface IEmployeeDeactivateModalProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (id: string | null) => void;
}

const EmployeeDeactivateModal = (props: IEmployeeDeactivateModalProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutate } = usePatchDisableEmployeeProfileMutation(selectedRecord, toggle, toggleSuccess);
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
      titleHeader='Vô hiệu nhân viên'
    >
      <Typography level={4}>Bạn muốn vô hiệu nhân viên này?</Typography>
    </Modal>
  );
};

export default EmployeeDeactivateModal;
