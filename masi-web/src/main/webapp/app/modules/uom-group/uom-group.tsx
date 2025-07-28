import './uom-group.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsUomGroup } from 'app/hooks/use-modals-uom';
import { IUomGroupParams } from 'app/shared/model/uom.model';
import React, { useState } from 'react';
import UomGroupHeader from './uom-group-header';
import UomGroupTable from './uom-group-table';
import UomGroupCreateModals from './modals/uom-group-create-modals';
import UomGroupCreateSuccessModals from './modals/uom-group-create-success-modals';
import UomGroupUpdateModals from './modals/uom-group-update-modals';
import UomGroupUpdateSuccessModals from './modals/uom-group-update-success-modals';
import UomGroupDeleteModals from './modals/uom-group-delete-modals';
import UomGroupDeleteSuccessModals from './modals/uom-group-delete-success-modals';
import UomGroupDetailModals from './modals/uom-group-detail-modals';

const UomGroup = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDetail, toggleOpenDetail },
  ] = useModalsUomGroup();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [filter, setFilter] = useState<IUomGroupParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  return (
    <>
      <Typography level={3}>Danh sách nhóm đơn vị</Typography>

      <Card header={<UomGroupHeader toggleCreate={toggleCreate} />}>
        <UomGroupTable
          filter={filter}
          setFilter={setFilter}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleDetail={toggleOpenDetail}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <UomGroupDetailModals isOpen={openDetail} toggle={toggleOpenDetail} selectedRecord={selectedRecord} />
      <UomGroupCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <UomGroupCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <UomGroupUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UomGroupUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <UomGroupDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UomGroupDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
    </>
  );
};

export default UomGroup;
