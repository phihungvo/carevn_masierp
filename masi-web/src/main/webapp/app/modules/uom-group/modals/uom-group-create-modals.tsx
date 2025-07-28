import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UomGroupForm from '../components/uom-group-form';

const { CREATE_UOM_GROUP } = MUTATION_KEY;

interface IUomGroupCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const UomGroupCreateModals = (props: IUomGroupCreateModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_UOM_GROUP],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      // className="modals-create-uom"
      fullscreen
      okText="Tạo mới"
      okSubmitForm={FORM.UOM}
      disabledOk={!!isCreating}
    >
      <Typography level={3}>Tạo nhóm đơn vị</Typography>
      <UomGroupForm type="create" toggle={toggle} toggleSuccess={toggleSuccess} />
    </Modal>
  );
};

export default UomGroupCreateModals;
