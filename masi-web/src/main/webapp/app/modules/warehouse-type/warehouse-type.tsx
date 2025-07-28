import './warehouse-type.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { useModalsWarehouse } from 'app/hooks/use-modals-warehouse';
import React, { useState } from 'react';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IWarehouseTypeParams } from 'app/shared/model/warehouse-type.model';
import WarehouseTypeHeader from './warehouse-type-header';
import WarehouseTypeTable from './warehouse-type-table';
import WarehouseTypeDetailModals from './modals/warehouse-type-detail-modals';
import WarehouseTypeCreateModals from './modals/warehouse-type-create-modals';
import WarehouseTypeCreateSuccessModals from './modals/warehouse-type-create-success-modals';
import WarehouseTypeUpdateModals from './modals/warehouse-type-update-modals';
import WarehouseTypeUpdateSuccessModals from './modals/warehouse-type-update-success-modals';
import WarehouseTypeDeleteModals from './modals/warehouse-type-delete-modals';
import WarehouseTypeDeleteSuccessModals from './modals/warehouse-type-delete-success-modals';

const WarehouseType = () => {
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
  const [filter, setFilter] = useState<IWarehouseTypeParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách kho</Typography>

      <Card header={<WarehouseTypeHeader toggleCreate={toggleCreate} />}>
        <WarehouseTypeTable
          filter={filter}
          setFilter={setFilter}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleDetail={toggleOpenDetail}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <WarehouseTypeDetailModals isOpen={openDetail} toggle={toggleOpenDetail} selectedRecord={selectedRecord} />
      <WarehouseTypeCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <WarehouseTypeCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <WarehouseTypeUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <WarehouseTypeUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <WarehouseTypeDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <WarehouseTypeDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
    </div>
  );
};

export default WarehouseType;
