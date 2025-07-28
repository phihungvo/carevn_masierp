import './offices.scss';
import Card from 'app/components/card/card';
import React, { useState } from 'react';
import OfficesHeader from './offices-header';
import OfficesTable from './offices-table';
import { useModalsOffices } from 'app/hooks/use-modals-offices';
import OfficesCreateModals from './modals/offices-create-modals';
import OfficesCreateSuccessModals from './modals/offices-create-success-modals';
import { Typography } from 'app/components/typography/typography';
import OfficesUpdateModals from './modals/offices-update-modals';
import OfficesUpdateSuccessModals from './modals/offices-update-success-modals';
import OfficesDeleteModals from './modals/offices-delete-modals';
import OfficesDeleteSuccessModals from './modals/offices-delete-success-modals';
import { IWorkspaceParams } from 'app/shared/model/workspace.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';

const Offices = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ] = useModalsOffices();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IWorkspaceParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    searchString: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Văn phòng / Nhà máy</Typography>
      <Card header={<OfficesHeader setSearchText={setSearchText} toggleCreate={toggleCreate} />}>
        <OfficesTable
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <OfficesCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <OfficesCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <OfficesUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <OfficesUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <OfficesDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <OfficesDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
    </div>
  );
};

export default Offices;
