import React, { useState } from 'react';

import './uniform-settings.scss';
import Card from 'app/components/card/card';
import UniformSettingsTable from './uniform-settings-table';
import UniformSettingsHeader from './uniform-settings-header';
import UniformSettingsCreateModals from './modals/uniform-settings-create-modals';
import UniformSettingsUpdateModals from './modals/uniform-settings-update-modals';
import UniformSettingsDisableModals from './modals/uniform-settings-disable-modals';
import UniformSettingsUpdateSuccessModals from './modals/uniform-settings-update-success-modals';
import UniformSettingsCreateSuccessModals from './modals/uniform-settings-create-success-modals';
import UniformSettingsDisableSuccessModals from './modals/uniform-settings-disable-success-modals';
import { IUniformParams } from 'app/shared/model/uniform.model';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsUniformSettings } from 'app/hooks/use-modals-uniform';
import UniformSettingsEnableModals from './modals/uniform-settings-enable-modals';
import UniformSettingsEnableSuccessModals from './modals/uniform-settings-enable-success-modals';

const UniformSettings = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openActive, toggleActive },
    { openActiveSuccess, toggleActiveSuccess },
  ] = useModalsUniformSettings();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [filter, setFilter] = useState<IUniformParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    sort: 'create_at,desc',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách đồng phục</Typography>
      <Card header={<UniformSettingsHeader toggleCreate={toggleCreate} />}>
        <UniformSettingsTable
          filter={filter}
          setFilter={setFilter}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleActive={toggleActive}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <UniformSettingsCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <UniformSettingsCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <UniformSettingsUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UniformSettingsUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <UniformSettingsDisableModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UniformSettingsDisableSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <UniformSettingsEnableModals
        isOpen={openActive}
        toggle={toggleActive}
        toggleSuccess={toggleActiveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UniformSettingsEnableSuccessModals isOpen={openActiveSuccess} toggle={toggleActiveSuccess} />
    </div>
  );
};

export default UniformSettings;
