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
import { useStocktakingExportXlsxLazyQuery } from 'app/hooks/use-stocktaking';
import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import StocktakingApproveModal from './modals/stocktaking-approve-modal';
import StocktakingApproveSuccessModal from './modals/stocktaking-approve-success-modal';
import StocktakingConfirmDeleteModal from './modals/stocktaking-confirm-delete-modal';
import StocktakingConfirmReject from './modals/stocktaking-confirm-reject';
import StocktakingConfirmReview from './modals/stocktaking-confirm-review';
import StocktakingDeleteSuccessModal from './modals/stocktaking-delete-success-modal';
import StocktakingFilterModals from './modals/stocktaking-filter-modals';
import StocktakingRejectSuccessModal from './modals/stocktaking-reject-success-modal';
import StocktakingReviewSuccessModal from './modals/stocktaking-review-success-modal';
import { StocktakingContext } from './stocktaking-provider';
import StocktakingTable from './stocktaking-storage-table';
import AuthGuard from 'app/components/guards/auth-guard';

const StocktakingDashboard = () => {
  const navigate = useNavigate();

  const { toggleFilter, setFilter } = useContext(StocktakingContext);

  const { trigger, data, isFetching } = useStocktakingExportXlsxLazyQuery({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const [searchText, setSearchText] = useState('');
  const searchDebounce = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'code.contains': searchDebounce === '' ? undefined : searchDebounce,
    }));
  }, [searchDebounce]);

  useDownloadXlsx(data?.data, 'stocktaking', 'xlsx');

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Kiểm kê</Typography>
            <AuthGuard permissionKey='LOGISTICS_STOCKTAKING.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                onClick={() => navigate(PATH.STOCKTAKING_CREATE)}
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
              <AuthGuard permissionKey='LOGISTICS_STOCKTAKING.EXPORT'>
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
          <StocktakingTable />
        </div>
      </CardV2>

      <StocktakingFilterModals />

      <StocktakingApproveModal />
      <StocktakingApproveSuccessModal />
      <StocktakingConfirmDeleteModal />
      <StocktakingDeleteSuccessModal />
      <StocktakingConfirmReject />
      <StocktakingRejectSuccessModal />
      <StocktakingConfirmReview />
      <StocktakingReviewSuccessModal />
    </>
  );
};

export default StocktakingDashboard;
