import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsWarehouse } from 'app/hooks/use-modals-warehouse';
import { IWarehouseParams } from 'app/shared/model/warehouse.model';
import React, { useState } from 'react';
import WarehouseCreateModals from './modals/warehouse-create-modals';
import WarehouseCreateSuccessModals from './modals/warehouse-create-success-modals';
import WarehouseDeleteModals from './modals/warehouse-delete-modals';
import WarehouseDeleteSuccessModals from './modals/warehouse-delete-success-modals';
import WarehouseDetailModals from './modals/warehouse-detail-modals';
import WarehouseUpdateModals from './modals/warehouse-update-modals';
import WarehouseUpdateSuccessModals from './modals/warehouse-update-success-modals';
import WarehouseHeader from './warehouse-header';
import WarehouseTable from './warehouse-table';
import './warehouse.scss';

const Warehouse = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openDetail, toggleOpenDetail },
  ] = useModalsWarehouse();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [filter, setFilter] = useState<IWarehouseParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    sort: ['create_at,desc'],
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách kho</Typography>

      <Card header={<WarehouseHeader toggleCreate={toggleCreate} />}>
        <WarehouseTable
          filter={filter}
          setFilter={setFilter}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleDetail={toggleOpenDetail}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <WarehouseDetailModals isOpen={openDetail} toggle={toggleOpenDetail} selectedRecord={selectedRecord} />
      <WarehouseCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <WarehouseCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <WarehouseUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <WarehouseUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <WarehouseDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <WarehouseDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
    </div>
  );
};

export default Warehouse;
