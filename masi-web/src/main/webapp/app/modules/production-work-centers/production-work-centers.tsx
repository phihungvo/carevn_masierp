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
import { useModalsProductionWorkCenters } from 'app/hooks/use-modals-production-machines';
import { IWorkCenterParams } from 'app/shared/model/work-center.model';
import { useState } from 'react';
import ProductionWorkCentersCreateModals from './modals/production-work-centers-create-modals';
import ProductionWorkCentersCreateSuccessModals from './modals/production-work-centers-create-success-modals';
import ProductionWorkCentersDeleteModals from './modals/production-work-centers-delete-modals';
import ProductionWorkCentersDeleteSuccessModals from './modals/production-work-centers-delete-success-modals';
import ProductionWorkCentersFilterModals from './modals/production-work-centers-filter-modals';
import ProductionWorkCentersUpdateModals from './modals/production-work-centers-update-modals';
import ProductionWorkCentersUpdateSuccessModals from './modals/production-work-centers-update-success-modals';
import ProductionWorkCentersTable from './production-work-centers-table';
import AuthGuard from 'app/components/guards/auth-guard';

const ProductionWorkCenters = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ] = useModalsProductionWorkCenters();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IWorkCenterParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Quản lý cụm máy sản xuất</Typography>
            <AuthGuard permissionKey="PRODUCTION_WORK_CENTERS.CREATE">
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

          <ProductionWorkCentersTable
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            setSelectedRecord={setSelectedRecord}
            searchText={searchText}
            filter={filter}
            setFilter={setFilter}
          />
        </div>
      </CardV2>

      <ProductionWorkCentersFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />
      <ProductionWorkCentersCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
      />
      <ProductionWorkCentersCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />

      <ProductionWorkCentersUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <ProductionWorkCentersUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />

      <ProductionWorkCentersDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <ProductionWorkCentersDeleteSuccessModals
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
    </>
  );
};

export default ProductionWorkCenters;
