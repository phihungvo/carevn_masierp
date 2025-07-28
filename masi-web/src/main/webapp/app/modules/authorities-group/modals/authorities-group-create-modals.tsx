import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { IAuthorityParams } from 'app/shared/model/authority.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import AuthoritiesGroupForm from '../components/authorities-group-form';
import { AuthoritiesListConverted } from '../utils/format-data';

const { CREATE_GROUP } = MUTATION_KEY;

interface IAuthoritiesGroupCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  formSearchText: string;
  setFormSearchText: React.Dispatch<React.SetStateAction<string>>
  formFilter: IAuthorityParams
  setFormFilter: React.Dispatch<React.SetStateAction<IAuthorityParams>>
  permissionList: AuthoritiesListConverted
  setPermissionList: React.Dispatch<React.SetStateAction<AuthoritiesListConverted>>
  permissionListTmpRef: React.MutableRefObject<AuthoritiesListConverted>
  permissionId: React.MutableRefObject<Set<string>>
}

const AuthoritiesGroupCreateModals = (props: IAuthoritiesGroupCreateModals) => {
  const {
    isOpen, toggle, toggleSuccess, formSearchText, setFormSearchText,
    formFilter, setFormFilter, permissionList, setPermissionList, permissionListTmpRef,
    permissionId
  } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_GROUP],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-group"
      okText="Tạo mới"
      okSubmitForm={FORM.GROUP}
      disabledOk={!!isCreating}
      titleHeader='Tạo nhóm quyền'
    >
      <AuthoritiesGroupForm
        type="create"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        formSearchText={formSearchText}
        setFormSearchText={setFormSearchText}
        formFilter={formFilter}
        setFormFilter={setFormFilter}
        permissionList={permissionList}
        setPermissionList={setPermissionList}
        permissionListTmpRef={permissionListTmpRef}
        permissionId={permissionId}
      />
    </Modal>
  );
};

export default AuthoritiesGroupCreateModals;
