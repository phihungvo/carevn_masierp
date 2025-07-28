import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import InputSearch from 'app/components/input/input-search';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  ICON_PATH,
} from 'app/constants/common';
import { useModalsProductionRoutings } from 'app/hooks/use-modals-production-routings';
import { IProductionMaintainParams } from 'app/shared/model/production-maintain.model';
import { useState } from 'react';
import ProductionMaintenanceCreateModals from './modals/production-maintenance-create-modals';
import ProductionMaintenanceCreateSuccessModals from './modals/production-maintenance-create-success-modals';
import ProductionMaintenanceDeleteModals from './modals/production-maintenance-delete-modals';
import ProductionMaintenanceDeleteSuccessModals from './modals/production-maintenance-delete-success-modals';
import ProductionMaintenanceFilterModals from './modals/production-maintenance-filter-modals';
import ProductionMaintenanceUpdateModals from './modals/production-maintenance-update-modals';
import ProductionMaintenanceUpdateSuccessModals from './modals/production-maintenance-update-success-modals';
import ProductionMaintenanceTable from './production-maintenance-table';
import AuthGuard from 'app/components/guards/auth-guard';

const ProductionMaintenance = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ] = useModalsProductionRoutings();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IProductionMaintainParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Quản lý bảo trì sản phẩm</Typography>
            <AuthGuard permissionKey="PRODUCTION_MAINTENANCE.CREATE">
              <ButtonAdd variant="primary" text="Thêm" onClick={toggleCreate} />
            </AuthGuard>
          </Flex>
        }
      >
        <div className="rp__container">
          <Flex
            className="rp__header"
            justify="space-between"
            align="center"
            style={{ marginBottom: 12 }}
          >
            <InputSearch onChange={e => setSearchText(e.target.value)} />
            <Flex gap={16}>
              <ButtonV2
                left_section={
                  <img src={ICON_PATH + 'three-line-filter.svg'} alt="filter" />
                }
                onClick={() => toggleFilter()}
              >
                Bộ lọc
              </ButtonV2>
            </Flex>
          </Flex>

          <ProductionMaintenanceTable
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            setSelectedRecord={setSelectedRecord}
            setSelectedRowKeys={setSelectedRowKeys}
            searchText={searchText}
            filter={filter}
            setFilter={setFilter}
          />
        </div>
      </CardV2>

      <ProductionMaintenanceFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />

      <ProductionMaintenanceCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
      />
      <ProductionMaintenanceCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />

      <ProductionMaintenanceUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ProductionMaintenanceUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />

      <ProductionMaintenanceDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <ProductionMaintenanceDeleteSuccessModals
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
    </>
  );
};

export default ProductionMaintenance;
