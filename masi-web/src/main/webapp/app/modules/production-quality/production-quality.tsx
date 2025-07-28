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
import { PATH } from 'app/constants/path';
import { useModalsProductionQuality } from 'app/hooks/use-modals-production-quality';
import {
  IProductionQualityParams,
  IQualityCheckSample,
} from 'app/shared/model/production-quality-control.model';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { ProductionQualityTable } from './components/production-quality-table';
import QualityDeleteModals from './modals/quality-delete-modals';
import QualityDeleteSuccessModals from './modals/quality-delete-success-modals';
import QualityFilterModals from './modals/quality-filter-modals';
import AuthGuard from 'app/components/guards/auth-guard';

const ProductionQuality = () => {
  const navigate = useNavigate();

  const [
    { isOpenFilter, toggleFilter },
    { isOpenCreateSuccess, toggleCreateSuccess },
    { isOpenUpdateSuccess, toggleUpdateSuccess },
    { isOpenApprove, toggleApprove },
    { isOpenApproveSuccess, toggleApproveSuccess },
    { isOpenRejectCancel, toggleRejectCancel },
    { isOpenRejectCancelSuccess, toggleRejectCancelSuccess },
    { isOpenDelete, toggleDelete },
    { isOpenDeleteSuccess, toggleDeleteSuccess },
    { isOpenPass, toggleModalPass },
    { isOpenPassSuccess, toggleModalPassSuccess },
    { selectedRecord, setSelectedRecord },
  ] = useModalsProductionQuality();

  const [filter, setFilter] = useState<IProductionQualityParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    status: [],
  });
  const [searchText, setSearchText] = useState<string>('');

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Quản lý chất lượng</Typography>
            <AuthGuard permissionKey='PRODUCTION_QUALITY.CREATE'>
              <ButtonAdd
                variant="primary"
                text="Thêm"
                onClick={() => navigate(PATH.PRODUCTION_QUALITY_CREATE)}
              />
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

          <ProductionQualityTable
            toggleDeleteTestingSample={toggleDelete}
            setSelectedRecord={setSelectedRecord}
            filter={filter}
            setFilter={setFilter}
            searchText={searchText}
          />
        </div>
      </CardV2>

      <QualityDeleteModals
        isOpen={isOpenDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <QualityFilterModals
        isOpen={isOpenFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />

      <QualityDeleteSuccessModals
        isOpen={isOpenDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
    </>
  );
};

export default ProductionQuality;
