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
import { useModalsProductionStandard } from 'app/hooks/use-modals-production-standard';
import { IProductionStandardParams } from 'app/shared/model/production-command.model';
import { IProductionStandard } from 'app/shared/model/production-standard.model';
import { useState } from 'react';
import { ModalDisposeProductionStandard } from './modals/production-standard-dispose-modals';
import { ModalDisposeProductionStandardSuccess } from './modals/production-standard-dispose-success-modals';
import ProductionStandardFilterModals from './modals/production-standard-filter-modals';
import { ModalCreateProductionStandard } from './production-standard-create';
import { ProductionStandardTable } from './production-standard-table';
import { ModalUpdateProductionStandard } from './production-standard-update';
import AuthGuard from 'app/components/guards/auth-guard';

const ProductionStandard = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openUpdate, toggleUpdate },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
  ] = useModalsProductionStandard();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [record, setRecord] = useState<IProductionStandard[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IProductionStandardParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    name: '',
  });

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Quản lý định mức sản xuất</Typography>
            <AuthGuard permissionKey="PRODUCTION_STANDARD.CREATE">
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

          <ProductionStandardTable
            toggleUpdateProductionStandard={toggleUpdate}
            toggleDisposeProductionStandard={toggleDelete}
            setSelectedRecord={setSelectedRecord}
            selectedRowKeys={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
            searchText={searchText}
            filter={filter}
            setFilter={setFilter}
            setRecord={setRecord}
          />
        </div>

        <ProductionStandardFilterModals
          isOpen={openFilter}
          setFilter={setFilter}
          toggle={toggleFilter}
        />
      </CardV2>

      <ModalCreateProductionStandard
        isOpen={openCreate}
        toggle={toggleCreate}
      />

      <ModalUpdateProductionStandard
        isOpen={openUpdate}
        toggle={toggleUpdate}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <ModalDisposeProductionStandard
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleDisposeProductionStandardSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />

      <ModalDisposeProductionStandardSuccess
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
    </>
  );
};

export default ProductionStandard;
