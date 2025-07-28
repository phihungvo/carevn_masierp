import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  ICON_PATH,
} from 'app/constants/common';
import { useModalsSupplies } from 'app/hooks/use-modals-supplies';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import React, { useState } from 'react';
import SuppliesActivateModals from './modals/supplies-activate-modals';
import SuppliesActivateSuccessModals from './modals/supplies-activate-success-modals';
import SuppliesCreateModals from './modals/supplies-create-modals';
import SuppliesCreateSuccessModals from './modals/supplies-create-success-modals';
import SuppliesDeleteModals from './modals/supplies-delete-modals';
import SuppliesDeleteSuccessModals from './modals/supplies-delete-success-modals';
import SuppliesDetailsModal from './modals/supplies-details-modal';
import SuppliesDisposeModals from './modals/supplies-dispose-modals';
import SuppliesDisposeSuccessModals from './modals/supplies-dispose-success-modals';
import SuppliesFilterModals from './modals/supplies-filter-modals';
import SuppliesUpdateModals from './modals/supplies-update-modals';
import SuppliesUpdateSuccessModals from './modals/supplies-update-success-modals';
import SuppliesTable from './supplies-table';
import './supplies.scss';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import InputSearch from 'app/components/input/input-search';
import useItems from 'app/hooks/use-items';
import { useDownloadXlsx } from 'app/hooks/use-download';
import AuthGuard from 'app/components/guards/auth-guard';

const { useExportItemsXlsxLazyQuery } = useItems;

export default function Supplies() {
  const navigate = useNavigate();

  const {
    create: { openCreate, toggleCreate },
    filter: { openFilter, toggleFilter },
    approve: { toggleApprove },
    createSuccess: { openCreateSuccess, toggleCreateSuccess },
    delete: { openDelete, toggleDelete },
    deleteSuccess: { openDeleteSuccess, toggleDeleteSuccess },
    detail: { openDetail, toggleDetail },
    propose: { togglePropose },
    update: { openUpdate, toggleUpdate },
    updateSuccess: { openUpdateSuccess, toggleUpdateSuccess },
    dispose: { openDispose, toggleDispose },
    disposeSuccess: { openDisposeSuccess, toggleDisposeSuccess },
    activate: { openActivate, toggleActivate },
    activateSuccess: { openActivateSuccess, toggleActivateSuccess },
  } = useModalsSupplies();

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

  const { trigger, data } = useExportItemsXlsxLazyQuery(filter);

  useDownloadXlsx(data?.data, 'supplies', 'xlsx');

  return (
    <div className="page_container">
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Hàng hoá</Typography>
            <AuthGuard permissionKey='LOGISTICS_SUPPLIES.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                onClick={() => navigate(PATH.SUPPLIES_CREATE)}
              >
                Thêm
              </ButtonV2>
            </AuthGuard>
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
            <AuthGuard permissionKey='LOGISTICS_SUPPLIES.EXPORT'>
              <ButtonV2
                left_section={
                  <img src={ICON_PATH + 'upload-cloud.svg'} alt="export" />
                }
                onClick={trigger}
              >
                Excel
              </ButtonV2>
            </AuthGuard>
          </Flex>
        </Flex>
        <SuppliesTable
          filter={filter}
          searchText={searchText}
          selectedRowKeys={selectedRowKeys}
          selectedRows={selectedRows}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          setSelectedRowKeys={setSelectedRowKeys}
          setSelectedRows={setSelectedRows}
          toggleApprove={toggleApprove}
          toggleDelete={toggleDelete}
          toggleDetail={toggleDetail}
          togglePropose={togglePropose}
          toggleUpdate={toggleUpdate}
          toggleDispose={toggleDispose}
          toggleActivate={toggleActivate}
        />
      </CardV2>
      <SuppliesCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
      />
      <SuppliesUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleUpdateSuccess}
      />
      <SuppliesCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />
      <SuppliesUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
      <SuppliesDeleteSuccessModals
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
      <SuppliesDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <SuppliesFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />
      <SuppliesDetailsModal
        isOpen={openDetail}
        toggle={toggleDetail}
        selectedRecord={selectedRecord}
      />
      <SuppliesDisposeModals
        isOpen={openDispose}
        toggle={toggleDispose}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleDisposeSuccess}
      />
      <SuppliesDisposeSuccessModals
        isOpen={openDisposeSuccess}
        toggle={toggleDisposeSuccess}
      />
      <SuppliesActivateModals
        isOpen={openActivate}
        toggle={toggleActivate}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleActivateSuccess}
      />
      <SuppliesActivateSuccessModals
        isOpen={openActivateSuccess}
        toggle={toggleActivateSuccess}
      />
    </div>
  );
}
