import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsItem } from 'app/hooks/use-modals-item';
import { IItemParams } from 'app/shared/model/item.model';
import React, { useState } from 'react';
import ItemHeader from './item-header';
import ItemTable from './item-table';
import './item.scss';
import ItemCreateModals from './modals/item-create-modals';
import ItemCreateSuccessModals from './modals/item-create-success-modals';
import ItemDeleteModals from './modals/item-delete-modals';
import ItemDeleteSuccessModals from './modals/item-delete-success-modals';
import ItemDetailModals from './modals/item-detail-modals';
import ItemUpdateModals from './modals/item-update-modals';
import ItemUpdateSuccessModals from './modals/Item-update-success-modals';
const Item = () => {
  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
  ] = useModalsItem();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);

  const [filter, setFilter] = useState<IItemParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    'name.contains': null,
  });
  const [searchText, setSearchText] = useState<string | null>(null);

  return (
    <div className='page_container'>
      <Typography level={4}>Quản lý vật phẩm</Typography>
      <Card header={<ItemHeader toggleCreate={toggleCreate} toggleFilter={toggleFilter} setSearchText={setSearchText} />}>
        <ItemTable
          filter={filter}
          setFilter={setFilter}
          toggleDelete={toggleDelete}
          toggleUpdate={toggleUpdate}
          setSelectedRecord={setSelectedRecord}
          toggleDetail={toggleDetail}
          searchText={searchText}
        />
      </Card>
      <ItemDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <ItemCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <ItemCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <ItemUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ItemUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <ItemDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ItemDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
    </div>
  );
};

export default Item;
