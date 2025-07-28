import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { IAuthorityParams } from 'app/shared/model/authority.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import AuthoritiesGroupForm from '../components/authorities-group-form';
import { AuthoritiesListConverted } from '../utils/format-data';

const { UPDATE_GROUP } = MUTATION_KEY;

interface IAuthoritiesGroupUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const AuthoritiesGroupUpdateModals = (props: IAuthoritiesGroupUpdateModals) => {
  const {
    isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord
  } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_GROUP],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-group"
      okText="Cập nhật"
      okSubmitForm={FORM.GROUP}
      disabledOk={!!isUpdating}
      titleHeader='Cập nhật nhóm quyền'
    >
      <AuthoritiesGroupForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
      />
    </Modal>
  );
};

export default AuthoritiesGroupUpdateModals;
