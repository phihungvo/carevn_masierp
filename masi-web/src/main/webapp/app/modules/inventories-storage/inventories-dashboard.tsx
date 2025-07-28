import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import InputSearch from 'app/components/input/input-search';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE_NAX,
  ICON_PATH,
} from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { useInventoriesExportXlsxLazyQuery } from 'app/hooks/use-inventories';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import { InventoriesStorageContext } from './inventories-storage-provider';
import InventoriesStorageTable from './inventories-storage-table';
import InventoriesApproveModal from './modals/inventories-approve-modal';
import InventoriesApproveSuccessModal from './modals/inventories-approve-success-modal';
import InventoriesConfirmDeleteModal from './modals/inventories-confirm-delete-modal';
import InventoriesConfirmReject from './modals/inventories-confirm-reject';
import InventoriesConfirmReview from './modals/inventories-confirm-review';
import InventoriesDeleteSuccessModal from './modals/inventories-delete-success-modal';
import InventoriesFilterModals from './modals/inventories-filter-modals';
import InventoriesRejectSuccessModal from './modals/inventories-reject-success-modal';
import InventoriesReviewSuccessModal from './modals/inventories-review-success-modal';
import AuthGuard from 'app/components/guards/auth-guard';

const InventoriesDashboard = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const { toggleFilter, setFilter } = useContext(InventoriesStorageContext);

  const { trigger, data, isFetching } = useInventoriesExportXlsxLazyQuery({
    'warehouseGroupType.equals': warehouseImportType,
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const [searchText, setSearchText] = useState('');
  const searchDebounce = useDebounce(searchText, 1000);

  useEffect(() => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      search: searchDebounce,
    }));
  }, [searchDebounce]);

  useDownloadXlsx(data?.data, 'inventories', 'xlsx');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
      ? 'TS - CCDC'
      : '';

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Nhập kho {titlePrefix}</Typography>
            <AuthGuard permissionKey='LOGISTICS_INVENTORIES_STORAGE.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                onClick={() =>
                  navigate(PATH.INVENTORIES_STORAGE_CREATE + location.search)
                }
              >
                Thêm
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
                  <img src={ICON_PATH + 'three-line-filter.svg'} alt="filter" />
                }
                onClick={toggleFilter}
              >
                Bộ lọc
              </ButtonV2>
              <AuthGuard permissionKey='LOGISTICS_INVENTORIES_STORAGE.EXPORT'>
                <ButtonV2
                  left_section={
                    <img src={ICON_PATH + 'upload-cloud.svg'} alt="export" />
                  }
                  onClick={trigger}
                  disabled={isFetching}
                >
                  Excel
                </ButtonV2>
              </AuthGuard>
            </Flex>
          </Flex>
          <InventoriesStorageTable />
        </div>
      </CardV2>

      <InventoriesFilterModals />

      <InventoriesApproveModal />
      <InventoriesApproveSuccessModal />
      <InventoriesConfirmDeleteModal />
      <InventoriesDeleteSuccessModal />
      <InventoriesConfirmReject />
      <InventoriesRejectSuccessModal />
      <InventoriesConfirmReview />
      <InventoriesReviewSuccessModal />
    </>
  );
};

export default InventoriesDashboard;
