import './uom.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import UomTable from './uom-table';
import UomHeader from './uom-header';
import { IUomParams } from 'app/shared/model/uom.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsUom } from 'app/hooks/use-modals-uom';
import UomCreateModals from './modals/uom-create-modals';
import UomCreateSuccessModals from './modals/uom-create-success-modals';
import UomUpdateModals from './modals/uom-update-modals';
import UomDeleteModals from './modals/uom-delete-modals';
import UomDeleteSuccessModals from './modals/uom-delete-success-modals';
import UomUpdateSuccessModals from './modals/uom-update-success-modals';

const Uom = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ] = useModalsUom();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [filter, setFilter] = useState<IUomParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách đơn vị</Typography>

      <Card header={<UomHeader toggleCreate={toggleCreate} />}>
        <UomTable
          filter={filter}
          setFilter={setFilter}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <UomCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <UomCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <UomUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UomUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <UomDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UomDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
    </div>
  );
};

export default Uom;
