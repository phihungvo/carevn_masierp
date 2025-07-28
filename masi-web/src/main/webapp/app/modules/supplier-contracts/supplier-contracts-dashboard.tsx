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
import { useSupplierContractsExportXlsxLazyQuery } from 'app/hooks/use-supplier-contract';
import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import SupplierContractsApproveModal from './modals/supplier-contracts-approve-modal';
import SupplierContractsApproveSuccessModal from './modals/supplier-contracts-approve-success-modal';
import SupplierContractsConfirmDeleteModal from './modals/supplier-contracts-confirm-delete-modal';
import SupplierContractsConfirmReject from './modals/supplier-contracts-confirm-reject';
import SupplierContractsConfirmReview from './modals/supplier-contracts-confirm-review';
import SupplierContractsDeleteSuccessModal from './modals/supplier-contracts-delete-success-modal';
import SupplierContractsFilterModals from './modals/supplier-contracts-filter-modals';
import SupplierContractsRejectSuccessModal from './modals/supplier-contracts-reject-success-modal';
import SupplierContractsReviewSuccessModal from './modals/supplier-contracts-review-success-modal';
import { SupplierContractsContext } from './supplier-contracts-storage-provider';
import SupplierContractsTable from './supplier-contracts-storage-table';
import SupplierContractsConfirmChangeStatusModal from './modals/supplier-contracts-confirm-change-status-modal';
import SupplierContractsChangeStatusSuccessModal from './modals/supplier-contracts-change-status-success-modal';
import AuthGuard from 'app/components/guards/auth-guard';

const SupplierContractsDashboard = () => {
  const navigate = useNavigate();

  const { toggleFilter, setFilter } = useContext(SupplierContractsContext);

  const { trigger, data, isFetching } = useSupplierContractsExportXlsxLazyQuery(
    { page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE_NAX },
  );

  const [searchText, setSearchText] = useState('');
  const searchDebounce = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      search: searchDebounce === '' ? undefined : searchDebounce,
    }));
  }, [searchDebounce]);

  useDownloadXlsx(data?.data, 'supplier-contracts', 'xlsx');

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Hợp đồng mua</Typography>
            <AuthGuard permissionKey='LOGISTICS_SUPPLIER_CONTRACTS.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                onClick={() => navigate(PATH.SUPPLIER_CONTRACTS_CREATE)}
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
          <SupplierContractsTable />
        </div>
      </CardV2>

      <SupplierContractsFilterModals />

      <SupplierContractsConfirmChangeStatusModal />
      <SupplierContractsChangeStatusSuccessModal />
    </>
  );
};

export default SupplierContractsDashboard;
