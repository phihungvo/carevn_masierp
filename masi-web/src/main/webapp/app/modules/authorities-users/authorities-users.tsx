import { useState } from 'react';

import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsAuthorities } from 'app/hooks/use-modals-Authorities-users';
import AuthoritiesUsersDetail from 'app/modules/authorities-users/authorities-users-detail';
import AuthoritiesUsersHeader from 'app/modules/authorities-users/authorities-users-header';
import AuthoritiesUsersTable from 'app/modules/authorities-users/authorities-users-table';
import AuthoritiesUsersUpdateModals from 'app/modules/authorities-users/modals/authorities-users-update-modals';
import AuthoritiesUsersUpdateSuccessModals from 'app/modules/authorities-users/modals/authorities-users-update-success-modals';
import { IGroupParams } from 'app/shared/model/group.model';
import './authorities-users.scss';
import AuthoritiesUserUpdateAccoutn from './modals/authorities-user-update-account';
import AuthoritiesUserSettingCompanies from './modals/authorities-user-setting-companies';

const AuthoritiesUsers = () => {
    const [
        { openUpdateUsers, toggleUpdateUsers },
        { openDetail, toggleDetail },
        { openUpdateSuccess, toggleUpdateSuccess },
        { isOpenUpdateModal, toggleUpdateAccount },
        { isOpenSettingCompanies, toggleSettingCompanies }
    ] = useModalsAuthorities();

    const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
    const [selectedRow, setSelectedRow] = useState(null);

    const [searchText, setSearchText] = useState<string>('');


    const [filter, setFilter] = useState<IGroupParams>({
        page: DEFAULT_PAGE,
        size: DEFAULT_PAGE_SIZE,
    });

    return (
        <div className='page_container'>
            <Typography level={4}>Danh sách người dùng</Typography>

            <Card header={<AuthoritiesUsersHeader setSearchText={setSearchText} />} >
                <AuthoritiesUsersTable
                    filter={filter}
                    setFilter={setFilter}
                    searchText={searchText}
                    setSelectedRecord={setSelectedRecord}
                    toggleUpdateUsers={toggleUpdateUsers}
                    toggleDetail={toggleDetail}
                    selectedRow={selectedRow}
                    setSelectedRow={setSelectedRow}
                    toggleUpdateAccount={toggleUpdateAccount}
                    toggleSettingCompanies={toggleSettingCompanies}
                />
            </Card>

            <AuthoritiesUsersUpdateModals isOpen={openUpdateUsers} toggle={toggleUpdateUsers} toggleSuccess={toggleUpdateSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />

            <AuthoritiesUsersDetail isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />

            <AuthoritiesUsersUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />

            <AuthoritiesUserUpdateAccoutn isOpen={isOpenUpdateModal} toggle={toggleUpdateAccount} selectedRecord={selectedRow} />

            <AuthoritiesUserSettingCompanies isOpen={isOpenSettingCompanies} toggle={toggleSettingCompanies} selectedRecord={selectedRow} />
        </div>
    );
};

export default AuthoritiesUsers;
