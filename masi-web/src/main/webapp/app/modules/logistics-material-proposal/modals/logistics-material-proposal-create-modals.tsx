import React from 'react';

import Modal from 'app/components/modal/modal';
import LogisticsMaterialProposalForm from 'app/modules/logistics-material-proposal/components/logistics-material-proposal-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Typography } from 'app/components/typography/typography';


interface ILogisticsMaterialProposalCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const LogisticsMaterialProposalCreateModals = (props: ILogisticsMaterialProposalCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="logistics-material-proposal-create-modals"
      okText="Tạo mới"
      okSubmitForm={FORM.LOGISTICS_MATERIAL_PROPOSAL}
    >
      <Typography level={3}>Tạo mới đề xuất vật tư</Typography>
      <LogisticsMaterialProposalForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default LogisticsMaterialProposalCreateModals;
