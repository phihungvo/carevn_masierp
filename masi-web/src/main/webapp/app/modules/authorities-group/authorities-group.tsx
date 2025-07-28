import React, { useEffect, useRef, useState } from 'react';

import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { useAuthoritiesConverted } from 'app/hooks/use-authority';
import { useModalsGroup } from 'app/hooks/use-modals-group';
import { IAuthorityParams } from 'app/shared/model/authority.model';
import { IGroupParams } from 'app/shared/model/group.model';
import AuthoritiesGroupDetail from './authorities-group-detail';
import AuthoritiesGroupHeader from './authorities-group-header';
import AuthoritiesGroupTable from './authorities-group-table';
import './authorities-group.scss';
import AuthoritiesGroupCreateModals from './modals/authorities-group-create-modals';
import AuthoritiesGroupCreateSuccessModals from './modals/authorities-group-create-success-modals';
import AuthoritiesGroupDeleteModals from './modals/authorities-group-delete-modals';
import AuthoritiesGroupDeleteSuccessModals from './modals/authorities-group-delete-success-modals';
import AuthoritiesGroupUpdateModals from './modals/authorities-group-update-modals';
import AuthoritiesGroupUpdateSuccessModals from './modals/authorities-group-update-success-modals';
import { AuthoritiesListConverted } from './utils/format-data';

const AuthoritiesGroup = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDetail, toggleOpenDetail },
  ] = useModalsGroup();

  const permissionListTmpRef = useRef<typeof permissionList>();
  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [filter, setFilter] = useState<IGroupParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const [formSearchText, setFormSearchText] = useState('');
  const [formFilter, setFormFilter] = useState<IAuthorityParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE_NAX,
    search: formSearchText,
  });
  const permissionId = useRef<Set<string>>(new Set());
  const [permissionList, setPermissionList] = useState<AuthoritiesListConverted>({ groups: [], total_checked_groups: 0 });

  useEffect(() => {
    if (!openCreate || !openUpdate) {
      setPermissionList(permissionListTmpRef.current);
      permissionId.current.clear()
    }
  }, [openCreate, openUpdate])

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách nhóm quyền</Typography>

      <Card header={<AuthoritiesGroupHeader toggleCreate={toggleCreate} />}>
        <AuthoritiesGroupTable
          filter={filter}
          setFilter={setFilter}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleDetail={toggleOpenDetail}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <AuthoritiesGroupCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
        formSearchText={formSearchText}
        setFormSearchText={setFormSearchText}
        formFilter={formFilter}
        setFormFilter={setFormFilter}
        permissionList={permissionList}
        setPermissionList={setPermissionList}
        permissionListTmpRef={permissionListTmpRef}
        permissionId={permissionId}
      />
      <AuthoritiesGroupCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />

      <AuthoritiesGroupUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        selectedRecord={selectedRecord}
      />
      <AuthoritiesGroupUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <AuthoritiesGroupDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <AuthoritiesGroupDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />

      <AuthoritiesGroupDetail isOpen={openDetail} toggle={toggleOpenDetail} selectedRecord={selectedRecord} />
    </div>
  );
};

export default AuthoritiesGroup;
