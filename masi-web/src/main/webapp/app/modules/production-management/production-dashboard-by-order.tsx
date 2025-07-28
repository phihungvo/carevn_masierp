import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  ICON_PATH,
} from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useProductionCommand from 'app/hooks/use-production-command';
import { PRODUCTION_COMMAND_TYPE } from 'app/shared/model/enumerations/production-command.model';
import { IProductionCommandParams } from 'app/shared/model/production-command.model';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import ManufactureOrderCancelModals from './modals/manufacture-order-cancel-modals';
import ManufactureOrderCancelSuccessModals from './modals/manufacture-order-cancel-success-modals';
import { ProductionDashboardByOrderTable } from './production-dashboard-by-order-table';
import ProductionDashboardFilterModals from './production-dashboard-filter-modals';

const { useGeProductionCommandExportReportExcel } = useProductionCommand;

const ProductionDashboardByOrder = () => {
  const navigate = useNavigate();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [openFilter, setOpenFilter] = useState<boolean>(false);
  const [openCancel, setOpenCancel] = useState<boolean>(false);
  const [openCancelSuccess, setOpenCancelSuccess] = useState<boolean>(false);

  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IProductionCommandParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const icon_path = 'content/images/vuesax/linear/';

  const { trigger, data: dataFile } = useGeProductionCommandExportReportExcel(
    PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_ORDER,
  );

  const onExportRequestPaymentData = () => trigger();

  // hook download xlsx
  useDownloadXlsx(dataFile?.data, `manufacture-order`, 'xlsx');

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Trộn bột</Typography>
            <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER.CREATE">
              <ButtonAdd
                variant="primary"
                text="Thêm"
                onClick={() =>
                  navigate(PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER_CREATE)
                }
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
                onClick={() => setOpenFilter(true)}
              >
                Bộ lọc
              </ButtonV2>

              <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER.EXPORT">
                <ButtonV2
                  left_section={
                    <img src={icon_path + 'export-excel.svg'} alt="filter" />
                  }
                  onClick={() => onExportRequestPaymentData()}
                >
                  Excel
                </ButtonV2>
              </AuthGuard>
            </Flex>
          </Flex>

          <ProductionDashboardByOrderTable
            setSelectedRecord={setSelectedRecord}
            searchText={searchText}
            filter={filter}
            setFilter={setFilter}
            toggleCancel={() => setOpenCancel(true)}
          />

          <ProductionDashboardFilterModals
            isOpen={openFilter}
            toggle={() => setOpenFilter(false)}
            setFilter={setFilter}
          />

          <ManufactureOrderCancelModals
            isOpen={openCancel}
            selectedRecord={selectedRecord}
            setSelectedRecord={setSelectedRecord}
            toggle={() => setOpenCancel(false)}
            toggleSuccess={() => setOpenCancelSuccess(true)}
          />

          <ManufactureOrderCancelSuccessModals
            isOpen={openCancelSuccess}
            toggle={() => setOpenCancelSuccess(false)}
          />
        </div>
      </CardV2>
    </>
  );
};

export default ProductionDashboardByOrder;
