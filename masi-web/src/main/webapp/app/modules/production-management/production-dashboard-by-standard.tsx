import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import InputSearch from 'app/components/input/input-search';
import { Typography } from 'app/components/typography/typography';
import {
  DATE_FORMAT,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  ICON_PATH,
} from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useProductionCommand from 'app/hooks/use-production-command';
import useProductionStandard from 'app/hooks/use-production-standard';
import { IProductionCommandParams } from 'app/shared/model/production-command.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import ManufactureOrderCancelModals from './modals/manufacture-order-cancel-modals';
import ManufactureOrderCancelSuccessModals from './modals/manufacture-order-cancel-success-modals';
import { ProductionDashboardByStandardTable } from './production-dashboard-by-standard-table';
import ProductionDashboardFilterModals from './production-dashboard-filter-modals';
import { PRODUCTION_COMMAND_TYPE } from 'app/shared/model/enumerations/production-command.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { PRODUCTION_STANDARD_STATUS } from 'app/shared/model/enumerations/production-standard.model';

const { useGetProductionStandards } = useProductionStandard;
const {
  useGetProductionCommandCountStandardsQuery,
  useGeProductionCommandExportReportExcel,
} = useProductionCommand;

const ProductionDashboardByStandard = () => {
  const navigate = useNavigate();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [openFilter, setOpenFilter] = useState<boolean>(false);
  const [openCancel, setOpenCancel] = useState<boolean>(false);
  const [openCancelSuccess, setOpenCancelSuccess] = useState<boolean>(false);
  const [standardId, setStandardId] = useState<string>(undefined);

  const { data: standardQty } = useGetProductionStandards({
    startDate: dayjs().startOf('month').format(DATE_FORMAT.YEAR_DATE),
    endDate: dayjs().endOf('month').format(DATE_FORMAT.YEAR_DATE),
    statuses: [PRODUCTION_STANDARD_STATUS.NEW],
  });
  const { data: countStandardQty } =
    useGetProductionCommandCountStandardsQuery(standardId);

  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IProductionCommandParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const styleDefault = {
    fontSize: '12px',
    color: '#667085',
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
  };

  const getStandardQty = () => {
    if (standardQty?.data?.length > 0)
      return standardQty?.data?.[0]?.quantity ?? 0;
    return 0;
  };

  useEffect(() => {
    if (standardQty?.data?.length > 0)
      setStandardId(standardQty?.data?.[0]?.id);
  }, [standardQty]);

  const icon_path = 'content/images/vuesax/linear/';

  const { trigger, data: dataFile } = useGeProductionCommandExportReportExcel(
    PRODUCTION_COMMAND_TYPE?.MANUFACTURE_ORDER_BY_STANDARD,
  );

  const onExportRequestPaymentData = () => trigger();

  // hook download xlsx
  useDownloadXlsx(dataFile?.data, `manufacture-order`, 'xlsx');

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Flex gap={40}>
              <Typography
                level={4}
                style={{ minWidth: '200px', borderRight: '1px solid #d3d5d7' }}
              >
                Hằng ngày
              </Typography>
              <Flex direction="row" justify="center" gap={40}>
                <span style={{ ...styleDefault }}>
                  Kế hoạch thu mua (Kg):{' '}
                  <b>{convertCurrency(getStandardQty())}</b>
                </span>
                <span style={{ ...styleDefault }}>
                  Thực tế mua (Kg):{' '}
                  <b>{convertCurrency(countStandardQty ?? 0)}</b>
                </span>
              </Flex>
            </Flex>
            <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER_STANDARD.CREATE">
              <ButtonAdd
                variant="primary"
                text="Thêm"
                onClick={() =>
                  navigate(PATH.PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD_CREATE)
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
              <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER_STANDARD.EXPORT">
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

          <ProductionDashboardByStandardTable
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

export default ProductionDashboardByStandard;
