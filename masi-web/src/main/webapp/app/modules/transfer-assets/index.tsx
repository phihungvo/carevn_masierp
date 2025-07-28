import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  ICON_PATH,
} from 'app/constants/common';
import { useModalsTransferAssets } from 'app/hooks/use-modals-transfer-assets';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import React, { useState } from 'react';
import TransferAssetsActivateModals from './modals/transfer-assets-activate-modals';
import TransferAssetsActivateSuccessModals from './modals/transfer-assets-activate-success-modals';
import TransferAssetsCreateModals from './modals/transfer-assets-create-modals';
import TransferAssetsCreateSuccessModals from './modals/transfer-assets-create-success-modals';
import TransferAssetsDeleteModals from './modals/transfer-assets-delete-modals';
import TransferAssetsDeleteSuccessModals from './modals/transfer-assets-delete-success-modals';
import TransferAssetsDetailsModal from './modals/transfer-assets-details-modal';
import TransferAssetsDisposeModals from './modals/transfer-assets-dispose-modals';
import TransferAssetsDisposeSuccessModals from './modals/transfer-assets-dispose-success-modals';
import TransferAssetsFilterModals from './modals/transfer-assets-filter-modals';
import TransferAssetsUpdateSuccessModals from './modals/transfer-assets-update-success-modals';
import TransferAssetsTable from './transfer-assets-table';
import './transfer-assets.scss';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import InputSearch from 'app/components/input/input-search';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useTransferAssets from 'app/hooks/use-transfer-assets';
import AuthGuard from 'app/components/guards/auth-guard';

const { useExportTransferAssetsXlsxLazyQuery } = useTransferAssets;

export default function TransferAssets() {
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
  } = useModalsTransferAssets();

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

  const { trigger, data } = useExportTransferAssetsXlsxLazyQuery(filter);

  useDownloadXlsx(data?.data, 'transfer-assets', 'xlsx');

  return (
    <div className="page_container">
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Điều chuyển tài sản</Typography>
            <AuthGuard permissionKey='ASSET_TRANSFER.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                onClick={() => navigate(PATH.TRANSFER_ASSETS_CREATE)}
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
            <AuthGuard permissionKey='ASSET_TRANSFER.EXPORT'>
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
        <TransferAssetsTable
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
      <TransferAssetsCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
      />
      <TransferAssetsCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />
      <TransferAssetsUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
      <TransferAssetsFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />

      <TransferAssetsDisposeModals
        isOpen={openDispose}
        toggle={toggleDispose}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleDisposeSuccess}
      />
      <TransferAssetsDisposeSuccessModals
        isOpen={openDisposeSuccess}
        toggle={toggleDisposeSuccess}
      />
      <TransferAssetsActivateModals
        isOpen={openActivate}
        toggle={toggleActivate}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleActivateSuccess}
      />
      <TransferAssetsActivateSuccessModals
        isOpen={openActivateSuccess}
        toggle={toggleActivateSuccess}
      />
    </div>
  );
}
