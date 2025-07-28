import { zodResolver } from '@hookform/resolvers/zod';
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
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { useModalsSupplier } from 'app/hooks/use-modals-supplier';
import useSupplier from 'app/hooks/use-supplier';
import { ISupplierParams } from 'app/shared/model/supplier.model';
import {
  SupplierSchema,
  supplierSchema,
} from 'app/validation/supplier.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import SupplierCreateModals from './modals/supplier-create-modals';
import SupplierCreateSuccessModals from './modals/supplier-create-success-modals';
import SupplierDeleteModals from './modals/supplier-delete-modals';
import SupplierDeleteSuccessModals from './modals/supplier-delete-success-modals';
import SupplierDetailModals from './modals/supplier-detail-modals';
import SupplierFilterModals from './modals/supplier-filter-modals';
import SupplierUpdateModals from './modals/supplier-update-modals';
import SupplierUpdateSuccessModals from './modals/supplier-update-success-modals';
import SupplierActivateModals from './modals/suppliers-activate-modal';
import SupplierActivateSuccessModals from './modals/suppliers-activate-success-modals';
import SupplierDisposeModals from './modals/suppliers-dispose-modals';
import SupplierDisposeSuccessModals from './modals/suppliers-dispose-success-modals';
import ISupplierTable from './supplier-table';
import './supplier.scss';
import AuthGuard from 'app/components/guards/auth-guard';

const { useExportSuppliersXlsxLazyQuery } = useSupplier;

const Suppliers = () => {
  const navigate = useNavigate();

  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openItem, toggleItem },
    { openDispose, toggleDispose },
    { openDisposeSuccess, toggleDisposeSuccess },
    { openActivate, toggleActivate },
    { openActivateSuccess, toggleActivateSuccess },
  ] = useModalsSupplier();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [filter, setFilter] = useState<ISupplierParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: null,
    status: null,
  });
  const [searchText, setSearchText] = useState<string | null>(null);

  const search = useDebounce(searchText, 500);

  const { trigger: exportSuppliers, data } =
    useExportSuppliersXlsxLazyQuery(filter);

  const methods = useForm<SupplierSchema>({
    defaultValues: {},
    resolver: zodResolver(supplierSchema),
  });

  useEffect(() => {
    setFilter(prev => ({ ...prev, search }));
  }, [search]);

  useDownloadXlsx(data?.data, 'NCC', 'xlsx');

  return (
    <div className="page_container">
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Nhà cung cấp</Typography>
              <AuthGuard permissionKey='LOGISTICS_SUPPLIERS.CREATE'>
                <ButtonV2
                  color="blue"
                  variant="solid"
                  onClick={() => navigate(PATH.SUPPLIERS_CREATE)}
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
              <AuthGuard permissionKey='LOGISTICS_SUPPLIERS.EXPORT'>
                <ButtonV2
                  left_section={
                    <img src={ICON_PATH + 'upload-cloud.svg'} alt="export" />
                  }
                  onClick={exportSuppliers}
                >
                  Excel
                </ButtonV2>
              </AuthGuard>
            </Flex>
          </Flex>
          <ISupplierTable
            filter={filter}
            setFilter={setFilter}
            toggleDelete={toggleDelete}
            toggleUpdate={toggleUpdate}
            setSelectedRecord={setSelectedRecord}
            toggleDetail={toggleDetail}
            toggleDispose={toggleDispose}
            toggleActivate={toggleActivate}
            searchText={searchText}
          />
        </CardV2>
        <SupplierDetailModals
          isOpen={openDetail}
          toggle={toggleDetail}
          selectedRecord={selectedRecord}
        />
        <SupplierCreateModals
          isOpen={openCreate}
          toggle={toggleCreate}
          toggleSuccess={toggleCreateSuccess}
          toggleOpenItem={toggleItem}
        />
        <SupplierCreateSuccessModals
          isOpen={openCreateSuccess}
          toggle={toggleCreateSuccess}
        />
        <SupplierUpdateModals
          isOpen={openUpdate}
          toggle={toggleUpdate}
          toggleSuccess={toggleUpdateSuccess}
          toggleSelectItem={toggleItem}
          selectedRecord={selectedRecord}
          setSelectedRecord={setSelectedRecord}
        />
        <SupplierUpdateSuccessModals
          isOpen={openUpdateSuccess}
          toggle={toggleUpdateSuccess}
        />
        <SupplierDeleteModals
          isOpen={openDelete}
          toggle={toggleDelete}
          toggleSuccess={toggleDeleteSuccess}
          selectedRecord={selectedRecord}
          setSelectedRecord={setSelectedRecord}
        />
        <SupplierDeleteSuccessModals
          isOpen={openDeleteSuccess}
          toggle={toggleDeleteSuccess}
        />
        <SupplierFilterModals
          isOpen={openFilter}
          toggle={toggleFilter}
          setFilter={setFilter}
        />
        <SupplierDisposeModals
          isOpen={openDispose}
          toggle={toggleDispose}
          toggleSuccess={toggleDisposeSuccess}
          selectedRecord={selectedRecord}
        />
        <SupplierDisposeSuccessModals
          isOpen={openDisposeSuccess}
          toggle={toggleDisposeSuccess}
        />{' '}
        <SupplierActivateModals
          isOpen={openActivate}
          toggle={toggleActivate}
          toggleSuccess={toggleActivateSuccess}
          selectedRecord={selectedRecord}
        />
        <SupplierActivateSuccessModals
          isOpen={openActivateSuccess}
          toggle={toggleActivateSuccess}
        />
      </FormProvider>
    </div>
  );
};

export default Suppliers;
