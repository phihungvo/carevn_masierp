import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import InputSearch from 'app/components/input/input-search';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsProductionRoutings } from 'app/hooks/use-modals-production-routings';
import { IProductionRoutingParams } from 'app/shared/model/production-routing.model';
import { useState } from 'react';
import ProductionRoutingsCreateModals from './modals/production-routings-create-modals';
import ProductionRoutingsCreateSuccessModals from './modals/production-routings-create-success-modals';
import ProductionRoutingsDeleteModals from './modals/production-routings-delete-modals';
import ProductionRoutingsDeleteSuccessModals from './modals/production-routings-delete-success-modals';
import ProductionRoutingsFilterModals from './modals/production-routings-filter-modals';
import ProductionRoutingsUpdateModals from './modals/production-routings-update-modals';
import ProductionRoutingsUpdateSuccessModals from './modals/production-routings-update-success-modals';
import ProductionRoutingsTable from './production-routings-table';
import AuthGuard from 'app/components/guards/auth-guard';

const ProductionRoutings = () => {
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
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IProductionRoutingParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Quản lý tuyến kho sản xuất</Typography>
            <AuthGuard permissionKey="PRODUCTION_ROUTINGS.CREATE">
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
          </Flex>

          <ProductionRoutingsTable
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            searchText={searchText}
            filter={filter}
            setFilter={setFilter}
            setSelectedRecord={setSelectedRecord}
          />
        </div>
      </CardV2>

      <ProductionRoutingsFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />

      <ProductionRoutingsCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
      />

      <ProductionRoutingsCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />

      <ProductionRoutingsUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ProductionRoutingsUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />

      <ProductionRoutingsDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ProductionRoutingsDeleteSuccessModals
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
    </>
  );
};

export default ProductionRoutings;
