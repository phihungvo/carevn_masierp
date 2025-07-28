import React from 'react';

import Modal from 'app/components/modal/modal';
import LogisticsMaterialProposalForm from '../components/logistics-material-proposal-form';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';


interface ILogisticsMaterialProposalUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const LogisticsMaterialProposalUpdateModals = (props: ILogisticsMaterialProposalUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, } = props;



  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="logistics-material-proposal-update-modals"
      okText="Cập nhật"
      okSubmitForm={FORM.LOGISTICS_MATERIAL_PROPOSAL}
    >
      <Typography level={3}>Cập nhật đề xuất vật tư</Typography>
      <LogisticsMaterialProposalForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default LogisticsMaterialProposalUpdateModals;
