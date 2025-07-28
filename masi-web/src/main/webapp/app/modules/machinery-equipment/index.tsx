import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  ICON_PATH,
} from 'app/constants/common';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import React, { useState } from 'react';
import MachineryEquipmentFilterModals from './modals/machinery-equipment-filter-modals';
import MachineryEquipmentUpdateModals from './modals/machinery-equipment-update-modals';
import MachineryEquipmentUpdateSuccessModals from './modals/machinery-equipment-update-success-modals';
import MachineryEquipmentTable from './machinery-equipment-table';
import './machinery-equipment.scss';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import InputSearch from 'app/components/input/input-search';
import useItems from 'app/hooks/use-items';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import { useModalsMachineryEquipment } from 'app/hooks/use-modals-machinery-equipment';

const { useGetInventoriesStorageQuery, useExportInventoriesStorageXlsxLazyQuery } = useInventoriesStorage;

export default function MachineryEquipment() {
  const navigate = useNavigate();

  const {
    filter: { openFilter, toggleFilter },
    update: { openUpdate, toggleUpdate },
    updateSuccess: { openUpdateSuccess, toggleUpdateSuccess },
  } = useModalsMachineryEquipment();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IItem[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IItemParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
    status: undefined,
  });

  const { trigger, data } = useExportInventoriesStorageXlsxLazyQuery(filter);

  useDownloadXlsx(data?.data, 'machinery-equipment', 'xlsx');

  return (
    <div className="page_container">
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Máy móc thiết bị</Typography>
          </Flex>
        }
      >
        <div className="rp__container" />
        <Flex
          className="rp__header"
          justify="space-between"
          align="center"
          style={{
            marginBottom: 12,
          }}
        >
          <InputSearch onChange={e => setSearchText(e.target.value)} />
          <Flex gap={16}>
            <ButtonV2
              left_section={
                <img src={ICON_PATH + 'three-line-filter.svg'} alt="filter" />
              }
              onClick={toggleFilter}
            >
              Bộ lọc
            </ButtonV2>
            {/* <ButtonV2
              left_section={
                <img src={ICON_PATH + 'upload-cloud.svg'} alt="export" />
              }
              onClick={trigger}
            >
              Excel
            </ButtonV2> */}
          </Flex>
        </Flex>
        <MachineryEquipmentTable
          filter={filter}
          searchText={searchText}
          selectedRowKeys={selectedRowKeys}
          selectedRows={selectedRows}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          setSelectedRowKeys={setSelectedRowKeys}
          setSelectedRows={setSelectedRows}
          toggleUpdate={toggleUpdate}
        />
      </CardV2>
      <MachineryEquipmentUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleUpdateSuccess}
      />
      <MachineryEquipmentUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
      <MachineryEquipmentFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />
    </div>
  );
}
