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
import { useModalsProductionPackages } from 'app/hooks/use-modals-production-packages';
import {
  IProductionPackage,
  IProductionPackageParams,
} from 'app/shared/model/production-package.model';
import { useState } from 'react';
import ProductionPackagesCreateModals from './modals/production-packages-create-modals';
import ProductionPackagesCreateSuccessModals from './modals/production-packages-create-success-modals';
import ProductionPackagesDeleteModals from './modals/production-packages-delete-modals';
import ProductionPackagesDeleteSuccessModals from './modals/production-packages-delete-success-modals';
import ProductionPackagesFilterModals from './modals/production-packages-filter-modals';
import ProductionPackagesUpdateModals from './modals/production-packages-update-modals';
import ProductionPackagesUpdateSuccessModals from './modals/production-packages-update-success-modals';
import ProductionPackagesTable from './production-packages-table';
import AuthGuard from 'app/components/guards/auth-guard';

const ProductionPackages = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ] = useModalsProductionPackages();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IProductionPackage[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IProductionPackageParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Quản lý đóng gói</Typography>
            <AuthGuard permissionKey="PRODUCTION_PACKAGES.CREATE">
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
                onClick={toggleFilter}
              >
                Bộ lọc
              </ButtonV2>
            </Flex>
          </Flex>

          <ProductionPackagesTable
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            searchText={searchText}
            filter={filter}
            setFilter={setFilter}
            setSelectedRecord={setSelectedRecord}
            selectedRowKeys={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
            selectedRows={selectedRows}
            setSelectedRows={setSelectedRows}
          />
        </div>
      </CardV2>

      <ProductionPackagesFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />

      <ProductionPackagesCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
      />
      <ProductionPackagesCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />

      <ProductionPackagesUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ProductionPackagesUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />

      <ProductionPackagesDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRows={setSelectedRows}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <ProductionPackagesDeleteSuccessModals
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
    </>
  );
};

export default ProductionPackages;
