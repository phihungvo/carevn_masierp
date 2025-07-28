import Flex from 'app/components/flex/flex';
import { DATE_FORMAT } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useWarehouse from 'app/hooks/use-warehouse';
import { mapProductionPackagesStatusText } from 'app/modules/production-packages/production-packages-mapping';
import { MANUFACTURE_ORDER_TYPE } from 'app/shared/model/enumerations/production-command.model';
import { PRODUCTION_PACKAGES_STATUS } from 'app/shared/model/enumerations/production-packages.model';
import { convertCurrency } from 'app/shared/util/format';
import { ManufactureOrderSchema } from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import { useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';
import {
  enable,
  enableDirectAdditives,
  enableDirectImport,
  enableDirectMaterial,
  enableDirectPackaging,
  enableDirectProduction,
  enableDirectShipment,
} from '../../production-ultis';
import { TabDashboardItem } from './TabDashboardItem';
import { TabDashboardItem2 } from './TabDashboardItem2';
import { TabDashboardItem3 } from './TabDashboardItem3';
import { TabDashboardItem4 } from './TabDashboardItem4';
import { TabDashboardItem5 } from './TabDashboardItem5';

const { useGetWarehouseById } = useWarehouse;

export const TabDashboard = ({ onClick }: { onClick: (e) => void }) => {
  const { id } = useParams();

  const methods = useFormContext<ManufactureOrderSchema>();
  const { watch } = methods;
  const warehouseId = watch('productionSaveInventory.storageId');

  const { data: warehouseDetail } = useGetWarehouseById(warehouseId);

  const typeWatch = watch('typePage');
  const statusWatch = watch('status');

  const detail = watch();

  const path =
    typeWatch === (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string)
      ? PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER_UPDATE
      : PATH.PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD_UPDATE;

  const sumRawMaterial = () => {
    return (
      Number(detail?.rawMaterial?.fishHead) +
      Number(detail?.rawMaterial?.freshFish)
    );
  };

  const sumAdditivesW = () => {
    return (
      Number(detail?.additives?.sodiumMaterial) *
        Number(detail?.additives?.sodiumMaterialUom ?? 1) +
      Number(detail?.additives?.sodiumCarbonateWeight) *
        Number(detail?.additives?.sodiumCarbonateUom ?? 1) +
      Number(detail?.additives?.sodiumBicarbonateWeight) *
        Number(detail?.additives?.sodiumBicarbonateBatchUom ?? 1) +
      Number(detail?.additives?.bhtWeight) *
        Number(detail?.additives?.bhtUom ?? 1)
    );
  };

  const enableDirectQualityControl =
    detail?.productionQuality?.attributes?.isDone &&
    enableDirectPackaging(statusWatch);

  const renderInfoMaterial = () => {
    if (!enableDirectAdditives(statusWatch))
      return { title: '-- Kg', rightText: '' };

    const calcTotalProteinWeight = () => {
      return detail?.rawMaterial2?.items?.reduce(
        (acc, e) => (acc += Number(e.quantityUse) * Number(e.percentProtein)),
        0,
      );
    };

    const calcTotalWeight = () => {
      return detail?.rawMaterial2?.items?.reduce(
        (acc, e) => (acc += Number(e.quantityUse)),
        0,
      );
    };

    const amount = calcTotalProteinWeight() / calcTotalWeight() || 0;

    const amountW =
      typeWatch ===
      (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string)
        ? calcTotalWeight()
        : sumRawMaterial();

    return {
      title: `${amountW > 0 ? convertCurrency(amountW, false) : '--'} Kg`,
      rightText:
        typeWatch ===
        (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string)
          ? `% đạm dự kiến: ` + amount
          : '',
    };
  };

  const renderInfoAdditives = () => {
    if (!enableDirectProduction(statusWatch))
      return { title: '-- Kg', rightText: '' };

    const attrs = detail?.additives?.attributes;
    return {
      title: `${
        sumAdditivesW() > 0 ? convertCurrency(sumAdditivesW(), false) : '--'
      } Kg`,
      rightText: attrs ? (attrs?.[0]?.pass === true ? 'Đạt' : 'Không dạt') : '',
      status: (attrs ? (attrs?.[0]?.pass === true ? 'PASS' : 'FAIL') : '') as
        | 'PASS'
        | 'FAIL'
        | '',
    };
  };

  const renderInfoPackaging = () => {
    if (!enableDirectShipment(statusWatch))
      return { weight: '-- Kg', quantity: '-- Bao' };

    const packaging = detail?.productionPackaging;
    const weight = Number(packaging?.weight ?? 0);
    const quantity = Number(packaging?.quantity ?? 0);

    return {
      weight: `${weight > 0 ? convertCurrency(weight) : '--'} Kg`,
      quantity: `${quantity > 0 ? convertCurrency(quantity) : '--'} Bao`,
      statusText: mapProductionPackagesStatusText(
        packaging?.status as PRODUCTION_PACKAGES_STATUS,
      ),
      status: (packaging?.status ===
      (PRODUCTION_PACKAGES_STATUS.COMPLETED as string)
        ? 'PASS'
        : packaging?.status === (PRODUCTION_PACKAGES_STATUS.WAITING as string)
          ? 'FAIL'
          : '') as 'PASS' | 'FAIL' | '',
    };
  };

  return (
    <>
      <Flex direction="column" gap={16}>
        <Row>
          <Col md={12}>
            <TabDashboardItem5 directUrl={path.replace(':id', id)} />
          </Col>
        </Row>
        <Row>
          <Col md={4}>
            <TabDashboardItem
              styleIcon="1"
              disabledDirect={!enableDirectMaterial(statusWatch)}
              directUrl="#rawMaterial"
              onClickTab={() => onClick('rawMaterial')}
              title="Nguyên liệu sử dụng"
              valueTitle={renderInfoMaterial().title}
              topRightChildren={renderInfoMaterial().rightText}
            />
          </Col>
          <Col md={4}>
            <TabDashboardItem
              styleIcon="1"
              disabledDirect={!enableDirectAdditives}
              directUrl="#additives"
              onClickTab={() => onClick('additives')}
              title="Phụ gia sử dụng"
              valueTitle={renderInfoAdditives().title}
              topRightChildren={renderInfoAdditives().rightText}
              status={renderInfoAdditives().status}
            />
          </Col>
          <Col md={4}>
            <TabDashboardItem
              styleIcon="2"
              disabledDirect={!enableDirectPackaging}
              directUrl="#packaging"
              onClickTab={() => onClick('packaging')}
              title="Đóng gói thành phầm"
              valueTitle={`${renderInfoPackaging().weight} / ${
                renderInfoPackaging().quantity
              }`}
              topRightChildren={renderInfoPackaging().statusText}
              status={renderInfoPackaging().status}
            />
          </Col>
        </Row>
        <Row>
          <Col md={8}>
            <Row style={{ rowGap: '20px' }}>
              {typeWatch ===
                (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string) && (
                <Col md={12}>
                  <TabDashboardItem4
                    disabledDirect={!enableDirectAdditives}
                    directUrl="#production"
                    onClickTab={() => onClick('production')}
                    titleHeader="Sản xuất"
                  />
                </Col>
              )}
              <Col md={6}>
                <TabDashboardItem2
                  styleIcon="3"
                  disabledDirect={!enableDirectShipment}
                  directUrl="#shipment"
                  onClickTab={() => onClick('shipment')}
                  titleHeader="Lô hàng"
                  title="Mã lô hàng"
                  valueTitle={
                    enableDirectImport
                      ? detail?.productionBatch?.productBatchCode ?? ''
                      : ''
                  }
                  title2="Ngày hết hạn"
                  valueTitle2={
                    enableDirectImport && detail?.productionBatch?.expiredDate
                      ? dayjs(
                          detail?.productionBatch?.expiredDate.toDate(),
                        ).format(DATE_FORMAT.DATE)
                      : '--/--/----'
                  }
                />
              </Col>
              <Col md={6}>
                <TabDashboardItem2
                  styleIcon="4"
                  disabledDirect={!enableDirectImport(statusWatch)}
                  directUrl="#import"
                  onClickTab={() => onClick('import')}
                  titleHeader="Kho"
                  title="Mã - Tên kho"
                  valueTitle={
                    enable(statusWatch)
                      ? warehouseDetail
                        ? `${warehouseDetail?.code} - ${warehouseDetail?.name}`
                        : ''
                      : ''
                  }
                  title2="Khối lượng"
                  valueTitle2={`${
                    enable(statusWatch)
                      ? Number(detail?.productionPackaging?.weight)
                        ? convertCurrency(
                            Number(detail?.productionPackaging?.weight),
                            false,
                          )
                        : '--'
                      : '--'
                  } Kg`}
                />
              </Col>
            </Row>
          </Col>
          <Col md={4}>
            <TabDashboardItem3
              disabledDirect={!enableDirectQualityControl}
              directUrl="#qualityControl"
              onClickTab={() => onClick('qualityControl')}
              title="Kiểm định"
              valueTitle={`${
                detail?.productionQuality?.attributes?.isDone
                  ? detail?.productionQuality?.itemId &&
                    detail?.productionQuality?.proteinPercentageApply
                    ? detail?.productionQuality?.proteinPercentageApply ?? 0
                    : ''
                  : ''
              }`}
              internal={
                detail?.productionQuality?.attributes?.isDone &&
                detail?.productionQuality?.itemId &&
                detail?.productionQuality?.proteinPercentageApply
                  ? [
                      Number(detail?.productionQuality?.internalHum ?? 0),
                      Number(detail?.productionQuality?.internalTvn ?? 0),
                      Number(detail?.productionQuality?.internalAsh ?? 0),
                      Number(detail?.productionQuality?.internalProtein ?? 0),
                    ]
                  : Array(4).fill(0)
              }
              external={
                detail?.productionQuality?.attributes?.isDone &&
                detail?.productionQuality?.itemId &&
                detail?.productionQuality?.proteinPercentageApply
                  ? [
                      Number(detail?.productionQuality?.externalHum ?? 0),
                      Number(detail?.productionQuality?.externalTvn ?? 0),
                      Number(detail?.productionQuality?.externalAsh ?? 0),
                      Number(detail?.productionQuality?.externalProtein ?? 0),
                    ]
                  : Array(4).fill(0)
              }
            />
          </Col>
        </Row>
      </Flex>
    </>
  );
};
