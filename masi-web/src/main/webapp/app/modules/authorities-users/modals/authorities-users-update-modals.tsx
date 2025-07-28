import React from 'react';

import Modal from 'app/components/modal/modal';
import AuthoritiesUsersForm from 'app/modules/authorities-users/components/authorities-users-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';

const { CREATE_ADMIN_USER_GROUP } = MUTATION_KEY;

interface IAuthoritiesUsersUpdateModalsProps {
    isOpen: boolean;
    toggle: () => void;
    toggleSuccess: () => void;
    selectedRecord: string;
    setSelectedRecord: (id: string) => void;
}

const AuthoritiesUsersUpdateModals = (props: IAuthoritiesUsersUpdateModalsProps) => {
    const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;


    const isUpdating = useIsMutating({
        mutationKey: [CREATE_ADMIN_USER_GROUP],
    });

    return (
        <Modal
            isOpen={isOpen}
            toggle={toggle}
            className="authorities-users-update-modal"
            okText="Cập nhật"
            okSubmitForm={FORM.UPDATE_ROLE}
            disabledOk={!!isUpdating}
            titleHeader='Cập nhật quyền của người dùng'
        >
            <AuthoritiesUsersForm selectedRecord={selectedRecord} toggleUpdateUsers={toggle} toggleSuccess={toggleSuccess} />
        </Modal>
    );
};

export default AuthoritiesUsersUpdateModals;
