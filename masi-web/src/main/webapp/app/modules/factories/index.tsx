import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsFactories } from 'app/hooks/use-modals-factories';
import {
  IFactoryLogistics,
  IFactoryLogisticsParams,
} from 'app/shared/model/factory-logistics.model';
import React, { useEffect, useState } from 'react';
import FactoriesHeader from './factories-header';
import FactoriesTable from './factories-table';
import './factories.scss';
import FactoriesActivateModals from './modals/factories-activate-modals';
import FactoriesActivateSuccessModals from './modals/factories-activate-success-modals';
import FactoriesCreateModals from './modals/factories-create-modals';
import FactoriesCreateSuccessModals from './modals/factories-create-success-modals';
import FactoriesDeleteModals from './modals/factories-delete-modals';
import FactoriesDeleteSuccessModals from './modals/factories-delete-success-modals';
import FactoriesDetailsModal from './modals/factories-details-modal';
import FactoriesDisposeModals from './modals/factories-dispose-modals';
import FactoriesDisposeSuccessModals from './modals/factories-dispose-success-modals';
import FactoriesFilterModals from './modals/factories-filter-modals';
import FactoriesUpdateModals from './modals/factories-update-modals';
import FactoriesUpdateSuccessModals from './modals/factories-update-success-modals';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import InputSearch from 'app/components/input/input-search';
import { useDebounce } from 'app/hooks/use-debounce';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import AuthGuard from 'app/components/guards/auth-guard';

const icon_path = 'content/images/vuesax/linear/';

export default function Factories() {
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
  } = useModalsFactories();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IFactoryLogistics[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IFactoryLogisticsParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    'code.contains': '',
    'name.contains': '',
    'isActive.equals': undefined,
  });

  const searchDebounce = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter({
      ...filter,
      'code.contains': searchDebounce,
      'name.contains': searchDebounce,
      page: DEFAULT_PAGE,
    });
  }, [searchDebounce]);

  return (
    <div className='page_container'>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Danh sách nhà máy</Typography>
            <AuthGuard permissionKey='LOGISTICS_FACTORIES.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                onClick={() => navigate(PATH.FACTORIES_CREATE)}
              >
                <img src={icon_path + 'add_white.svg'} alt="plus" />
                Tạo mới
              </ButtonV2>
            </AuthGuard>
          </Flex>
        }
      >
        <div className="rp__container">
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
                  <img src={icon_path + 'three-line-filter.svg'} alt="filter" />
                }
                onClick={toggleFilter}
              >
                Bộ lọc
              </ButtonV2>
            </Flex>
          </Flex>
          <FactoriesTable
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
        </div>
      </CardV2>
      <FactoriesFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />

      <FactoriesDeleteSuccessModals
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
      <FactoriesDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />

      <FactoriesDetailsModal
        isOpen={openDetail}
        toggle={toggleDetail}
        selectedRecord={selectedRecord}
      />
      <FactoriesDisposeModals
        isOpen={openDispose}
        toggle={toggleDispose}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleDisposeSuccess}
      />
      <FactoriesDisposeSuccessModals
        isOpen={openDisposeSuccess}
        toggle={toggleDisposeSuccess}
      />
      <FactoriesActivateModals
        isOpen={openActivate}
        toggle={toggleActivate}
        selectedRecord={selectedRecord}
        toggleSuccess={toggleActivateSuccess}
      />
      <FactoriesActivateSuccessModals
        isOpen={openActivateSuccess}
        toggle={toggleActivateSuccess}
      />
    </div>
  );
}
