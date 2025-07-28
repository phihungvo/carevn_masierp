import { zodResolver } from '@hookform/resolvers/zod';
import { DATE_FORMAT } from 'app/constants/common';
import useOrders from 'app/hooks/use-orders';
import useProductionCommand from 'app/hooks/use-production-command';
import useProductionStandard from 'app/hooks/use-production-standard';
import {
  MANUFACTURE_ORDER_STATUS,
  MANUFACTURE_ORDER_TYPE,
} from 'app/shared/model/enumerations/production-command.model';
import { IPatchProductionCommandDto } from 'app/shared/model/production-command.model';
import { handleMergeTime } from 'app/shared/util/date-utils';
import { convertCurrency } from 'app/shared/util/format';
import {
  manufactureOrderSchema,
  ManufactureOrderSchema,
} from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import ProductionCard from './components/ProductionCard/ProductionCard';
import { TabAdditives } from './components/tabs/TabAdditives';
import { TabAdditives2 } from './components/tabs/TabAdditives2';
import { TabBatch } from './components/tabs/TabBatch';
import { TabDashboard } from './components/tabs/TabDashboard';
import { TabImport } from './components/tabs/TabImport';
import { TabPackaging } from './components/tabs/TabPackaging';
import { TabProduction } from './components/tabs/TabProduction';
import { TabProduction2 } from './components/tabs/TabProduction2';
import { TabQualityControl } from './components/tabs/TabQualityControl';
import { TabMaterial } from './components/tabs/TabRawMaterial';
import { TabMaterial2 } from './components/tabs/TabRawMaterial2';
import ManufactureOrderCompleteSuccessModal from './modals/manufacture-order-complete-success-modal';
import ManufactureOrderUpdateSuccessModal from './modals/manufacture-order-update-success-modal';
import ProductionManagementProvider from './production-provider';
import {
  enable,
  enableDirectAdditives,
  enableDirectImport,
  enableDirectPackaging,
  enableDirectProduction,
  enableDirectShipment,
} from './production-ultis';

const {
  usePatchProductionCommand,
  useGetProductionCommandById,
  useCompleteStateProductionCommand,
} = useProductionCommand;
const { useGetOrderByIdQuery } = useOrders;
const { useGetProductionStandardById } = useProductionStandard;

const ProductionManagement = () => {
  const [tab, setTab] = useState<string>('');
  const { id } = useParams();

  const [isOpenModalUpdateSuccess, setIsOpenUpdateSuccess] =
    useState<boolean>(false);
  const [isOpenModalCompleteSuccess, setIsOpenCompleteSuccess] =
    useState<boolean>(false);
  const [completeStateWithOutCall, setCompleteStateWithOutCall] =
    useState<boolean>(false);

  const [error, setError] = useState<{ key: string; message: string }>(null);

  const methods = useForm<ManufactureOrderSchema>({
    resolver: zodResolver(manufactureOrderSchema),
  });
  const { setValue, watch, getValues } = methods;

  const statusWatch = watch('status');
  const orderIdWatch = watch('orderId');
  const standardIdWatch = watch('productionStandardId');
  const itemIdWatch = watch('itemId');
  const typeWatch = watch('typePage');
  const detailWatch = watch();

  const { data: orderDetail } = useGetOrderByIdQuery(orderIdWatch);
  const { data: standardDetail } =
    useGetProductionStandardById(standardIdWatch);

  const { data: detail } = useGetProductionCommandById(id);
  const { mutate: update } = usePatchProductionCommand(id);
  const { mutate: complete } = useCompleteStateProductionCommand(id);

  const toggleCompleteSuccess = () => {
    setIsOpenCompleteSuccess(prev => !prev);
    setCompleteStateWithOutCall(false);
  };

  const toggleUpdateSuccess = (isSkip: number, isComplete: boolean) => {
    if (completeStateWithOutCall) {
      toggleCompleteSuccess();
      return;
    }
    if (!isComplete) setIsOpenUpdateSuccess(prev => !prev);
    else complete(isSkip || 1, { onSuccess: toggleCompleteSuccess });
  };

  const onSubmit = (stepData, key, complete?: boolean) => {
    let isSkip = 1;

    const values = watch();
    delete values?.productionQuality?.['disposal'];

    const submitValues: IPatchProductionCommandDto = {
      orderId: values?.orderId,
      productionStandardId: values?.productionStandardId,
      code: values?.name,
      name: values?.name,
      fromDate: dayjs(values?.fromDate.toDate()).toISOString(),
      toDate: dayjs(values?.toDate.toDate()).toISOString(),
      productionQuantity: values?.productionQuantity ?? 0,
      additives: {
        ...values.additives,
        inspectionDate: values?.additives?.inspectionDate
          ? new Date(values?.additives?.inspectionDate)
          : undefined,
        inspectionTime: values?.additives?.inspectionTime
          ? new Date(values?.additives?.inspectionTime)
          : undefined,
      },
      productMaintainDTO: values?.productionBatch
        ? {
            ...values?.productionBatch,
            expiredDate: values?.productionBatch?.expiredDate
              ? new Date(values?.productionBatch?.expiredDate.toDate())
              : undefined,
            manufactureDate: values?.productionBatch?.manufactureDate
              ? new Date(values?.productionBatch?.manufactureDate.toDate())
              : undefined,
            productPackageId:
              key == 'productionBatch'
                ? values?.productionPackaging?.id
                : undefined,
          }
        : {},
      productPackageDTO: values?.productionPackaging
        ? {
            ...values?.productionPackaging,
            packageAt: values?.productionPackaging?.packageAt
              ? new Date(values?.productionPackaging?.packageAt.toDate())
              : undefined,
            quantity: Number(values?.productionPackaging?.quantity ?? 0),
            weight: Number(values?.productionPackaging?.quantity ?? 0) * 50,
          }
        : {},
      productRoutingDTO: values?.productionSaveInventory
        ? {
            ...values?.productionSaveInventory,
            warehouseDate: values?.productionSaveInventory?.warehouseDate
              ? new Date(
                  values?.productionSaveInventory?.warehouseDate.toDate(),
                )
              : undefined,
            productMaintainId: values?.productionBatch?.id,
          }
        : {},
      productionManufacture: {
        ...values?.production2,
        attributes: { ...values?.production },
      },
      qualityCheckSampleDTO: values?.productionQuality?.id
        ? {
            ...values?.productionQuality,
            samplingDate: values?.productionQuality?.samplingDate
              ? new Date(values?.productionQuality?.samplingDate.toDate())
              : undefined,
            sampleReleaseDate: values?.productionQuality?.samplingDate
              ? new Date(values?.productionQuality?.samplingDate.toDate())
              : undefined,
            sampleWeight: values?.productionQuality?.sampleWeight
              ? Number(values?.productionQuality?.sampleWeight ?? 0)
              : undefined,
            proteinPercentageApply: values?.productionQuality
              ?.proteinPercentageApply
              ? Number(values?.productionQuality?.proteinPercentageApply ?? 0)
              : undefined,
          }
        : undefined,
      attributes: {
        percentProtein: values?.percentProtein,
        note: values?.note,
        rawMaterial: {
          ...values?.rawMaterial,
          inspectionDate: values?.rawMaterial?.inspectionDate
            ? new Date(values?.rawMaterial?.inspectionDate)
            : undefined,
          inspectionTime: values?.rawMaterial?.inspectionTime
            ? new Date(values?.rawMaterial?.inspectionTime)
            : undefined,
          volume: Number(values?.rawMaterial?.volume ?? 0),
          weight: Number(values?.rawMaterial?.weight ?? 0),
        },
        // manufacture: { ...values?.production },
        rawMaterial2: {
          ...values?.rawMaterial2,
          mixingDate: values?.rawMaterial2?.mixingDate,
          manufactureDate: values?.rawMaterial2?.manufactureDate,
          items: [...(values?.rawMaterial2?.items ?? [])],
        },
      },
    };

    if (key === 'rawMaterial') {
      submitValues.attributes = {
        percentProtein: values?.percentProtein,
        note: values?.note,
        rawMaterial: {
          ...stepData,
          inspectionDate: stepData?.inspectionDate
            ? new Date(stepData?.inspectionDate)
            : undefined,
          inspectionTime: stepData?.inspectionTime
            ? new Date(stepData?.inspectionTime)
            : undefined,
          volume: Number(stepData?.volume ?? 0),
          weight: Number(stepData?.weight ?? 0),
        },
        rawMaterial2: {
          ...values?.rawMaterial2,
          mixingDate: values?.rawMaterial2?.mixingDate,
          manufactureDate: values?.rawMaterial2?.manufactureDate,
          items: [...(values?.rawMaterial2?.items ?? [])],
        },
      };

      submitValues.productPackageDTO = null;
    } else if (key === 'rawMaterial2') {
      submitValues.attributes = {
        percentProtein: values?.percentProtein,
        note: values?.note,
        rawMaterial: {
          ...values?.rawMaterial,
          inspectionDate: values?.rawMaterial?.inspectionDate
            ? new Date(values?.rawMaterial?.inspectionDate)
            : undefined,
          inspectionTime: values?.rawMaterial?.inspectionTime
            ? new Date(values?.rawMaterial?.inspectionTime)
            : undefined,
          volume: Number(values?.rawMaterial?.volume ?? 0),
          weight: Number(values?.rawMaterial?.weight ?? 0),
        },
        rawMaterial2: {
          ...stepData,
          mixingDate: stepData?.mixingDate
            ? new Date(stepData?.mixingDate)
            : undefined,
          manufactureDate: stepData?.manufactureDate
            ? new Date(stepData?.manufactureDate)
            : undefined,
          items: [...(stepData?.items ?? [])],
        },
      };

      submitValues.productPackageDTO = null;
    } else if (key == 'additives') {
      submitValues.additives = {
        ...stepData,
        inspectionDate: stepData?.inspectionDate
          ? new Date(stepData?.inspectionDate)
          : undefined,
        inspectionTime: stepData?.inspectionTime
          ? new Date(stepData?.inspectionTime)
          : undefined,
      };

      submitValues.productPackageDTO = null;
    } else if (key == 'production') {
      if (statusWatch === MANUFACTURE_ORDER_STATUS.ADDITIVES) {
        if (complete) submitValues.additives = null;
        isSkip = 2;
      }
      submitValues.productionManufacture = {
        ...stepData,
        attributes: { ...values?.production2 },
      };

      submitValues.productPackageDTO = null;
    } else if (key == 'production2') {
      if (statusWatch === MANUFACTURE_ORDER_STATUS.ADDITIVES) {
        if (complete) submitValues.additives = null;
        isSkip = 2;
      }
      submitValues.productionManufacture = {
        ...values?.production,
        attributes: { ...stepData },
      };

      submitValues.productPackageDTO = null;
    } else if (key == 'productionPackaging') {
      if (statusWatch === MANUFACTURE_ORDER_STATUS.ADDITIVES) {
        if (complete) {
          submitValues.additives = null;
          submitValues.productionManufacture = null;
        }
        isSkip = 3;
      }

      submitValues.productPackageDTO = {
        ...stepData,
        packageAt: stepData?.packageAt
          ? new Date(stepData?.packageAt.toDate())
          : undefined,
        quantity: Number(stepData?.quantity ?? 0),
        weight: Number(stepData?.weight ?? 0),
        manufactureOrderId: id,
      };
    } else if (key == 'productionBatch') {
      submitValues.productMaintainDTO = {
        ...stepData,
        expiredDate: stepData?.expiredDate
          ? new Date(stepData?.expiredDate.toDate())
          : undefined,
        manufactureDate: stepData?.manufactureDate
          ? new Date(stepData?.manufactureDate.toDate())
          : undefined,
        productPackageId: values?.productionPackaging?.id,
      };
    } else if (key === 'qualityControl') {
      submitValues.qualityCheckSampleDTO = {
        ...stepData,
        samplingDate: stepData?.samplingDate
          ? new Date(stepData?.samplingDate.toDate())
          : undefined,
        sampleReleaseDate: stepData?.samplingDate
          ? new Date(stepData?.samplingDate.toDate())
          : undefined,
        sampleWeight: Number(stepData?.sampleWeight ?? 0),
        proteinPercentageApply: Number(stepData?.proteinPercentageApply ?? 0),
        manufactureOrderId: id,
        packageId: values.productionPackaging.id,
        itemId: '00000000-0000-0000-0000-000000000000',
        isDone: stepData?.attributes?.isDone ?? false,
      };
    } else if (key === 'productionSaveInventory') {
      submitValues.productRoutingDTO = {
        ...stepData,
        warehouseDate: stepData?.warehouseDate
          ? new Date(stepData?.warehouseDate.toDate())
          : undefined,
        quantity: Number(values?.productionPackaging.quantity ?? 0) * 50,
        productMaintainId: values?.productionBatch?.id,
      };
    }

    const toggleError = error => {
      const errorCode = error.response.data?.message;
      if (key == 'productionPackaging') {
        if (errorCode === 'error.packageCodeExists') {
          setError({ key: 'packageCode', message: 'Mã đóng gói đã tồn tại' });
        }
      } else if (key == 'productionBatch') {
        if (errorCode === 'error.CODE_EXISTS') {
          setError({
            key: 'productBatchCode',
            message: 'Mã lô hàng đã tồn tại',
          });
        }
      } else if (key === 'productionSaveInventory') {
        if (errorCode === 'error.CODE_EXISTS') {
          setError({
            key: 'productBatchCode',
            message: 'Mã lô hàng đã tồn tại',
          });
        }
      }
    };

    update(submitValues, {
      onSuccess: () => toggleUpdateSuccess(isSkip, complete),
      onError: toggleError,
    });
  };

  useEffect(() => {
    if (orderDetail) {
      const contract = orderDetail?.contract;
      setValue(
        'orderDeliveryDate',
        dayjs(orderDetail?.deliveryTermTo).toDate(),
      );
      setValue('orderCode', orderDetail.orderCode);

      const item = contract?.contractMaterialDTOS?.find(
        x => x.itemId === itemIdWatch,
      );
      if (item) {
        setValue('itemId', item.itemId);
        setValue('itemName', `${item.materialName}`);
        setValue('itemPercentProtein', item.itemDTO?.percentProtein);
      }
    }
  }, [orderDetail]);

  useEffect(() => {
    if (standardDetail) {
      setValue('productionStandardName', standardDetail?.data?.name);
      setValue(
        'productionStandardDueDate',
        dayjs(standardDetail?.data?.dueDate).format('MM/YYYY'),
      );
    }
  }, [standardDetail]);

  useEffect(() => {
    if (detail) {
      setValue('name', detail?.name);
      setValue('orderId', detail?.orderId);

      setValue(
        'updatedAt',
        detail?.lastUpdated ? new Date(detail?.lastUpdated) : null,
      );

      setValue('itemId', detail?.orderItemId);

      setValue('note', detail?.attributes?.note ?? '');
      setValue('percentProtein', detail?.attributes?.percentProtein);
      setValue('status', detail?.status);
      setValue('typePage', detail?.typePage);

      setValue(
        'fromDate',
        new DateObject(handleMergeTime(dayjs(detail?.fromDate)).toDate()),
      );
      setValue(
        'toDate',
        new DateObject(handleMergeTime(dayjs(detail?.toDate)).toDate()),
      );

      setValue('productionStandardId', detail?.productionStandardId);
      setValue('productionQuantity', detail?.productionQuantity);

      setValue('rawMaterial', {
        ...detail?.attributes?.rawMaterial,
        volume: detail?.attributes?.rawMaterial?.volume
          ? `${detail?.attributes?.rawMaterial?.volume ?? 0}`
          : null,
        weight: detail?.attributes?.rawMaterial?.weight
          ? `${detail?.attributes?.rawMaterial?.weight ?? 0}`
          : null,
        inspectionTime: detail?.attributes?.rawMaterial?.inspectionTime,
      });

      setValue('rawMaterial2', {
        ...detail?.attributes?.rawMaterial2,
        mixingDate: detail?.attributes?.rawMaterial2?.mixingDate
          ? new DateObject(
              handleMergeTime(
                dayjs(detail?.attributes?.rawMaterial2?.mixingDate),
              ).toDate(),
            )
          : null,
        manufactureDate: detail?.attributes?.rawMaterial2?.manufactureDate
          ? new DateObject(
              handleMergeTime(
                dayjs(detail?.attributes?.rawMaterial2?.manufactureDate),
              ).toDate(),
            )
          : null,
        totalQuantity: detail?.attributes?.rawMaterial2?.items
          ? detail?.attributes?.rawMaterial2?.items.reduce(
              (acc, obj) => acc + (Number(obj?.quantity ?? 0) ?? 0),
              0,
            )
          : 0,
      });

      setValue('additives', { ...detail?.additives });

      const manufacture = detail?.productionManufacture?.attributes;
      setValue('production', {
        ...manufacture,
        monitorMachineOperation: {
          ...manufacture?.monitorMachineOperation,
          manufactureDate: manufacture?.monitorMachineOperation?.manufactureDate
            ? new DateObject(
                manufacture?.monitorMachineOperation?.manufactureDate,
              )
            : null,
        },
        steamDryingMonitoring: {
          ...manufacture?.steamDryingMonitoring,
          manufactureDate: manufacture?.steamDryingMonitoring?.manufactureDate
            ? new DateObject(
                manufacture?.steamDryingMonitoring?.manufactureDate,
              )
            : null,
        },
        checkMagnetGrid: {
          ...manufacture?.checkMagnetGrid,
          inspectionTime: manufacture?.checkMagnetGrid?.inspectionTime
            ? new DateObject(manufacture?.checkMagnetGrid?.inspectionTime)
            : null,
          manufactureDate: manufacture?.checkMagnetGrid?.manufactureDate
            ? new DateObject(manufacture?.checkMagnetGrid?.manufactureDate)
            : null,
        },
        attributes: {
          step: manufacture?.attributes?.step ?? 1,
          stepDone: manufacture?.attributes?.stepDone ?? 0,
        },
      });

      setValue('production2', { ...detail?.productionManufacture });

      setValue('productionPackaging', {
        ...detail?.productPackageDTO,
        packageAt: new DateObject(detail?.productPackageDTO?.packageAt),
        quantity: detail?.productPackageDTO?.quantity?.toString(),
        weight: detail?.productPackageDTO?.weight?.toString(),
      });

      setValue('productionBatch', {
        ...detail?.productMaintainDTO,
        manufactureDate: detail?.productMaintainDTO?.manufactureDate
          ? new DateObject(
              handleMergeTime(
                dayjs(detail?.productMaintainDTO?.manufactureDate),
              ).toDate(),
            )
          : null,
        expiredDate: detail?.productMaintainDTO?.expiredDate
          ? new DateObject(
              handleMergeTime(
                dayjs(detail?.productMaintainDTO?.expiredDate),
              ).toDate(),
            )
          : null,
      });

      setValue('productionSaveInventory', {
        ...detail?.productRoutingDTO,
        warehouseDate: detail?.productRoutingDTO?.warehouseDate
          ? new DateObject(detail?.productRoutingDTO?.warehouseDate)
          : null,
      });

      const quality = detail?.qualityCheckSampleDTO;
      setValue('productionQuality', {
        ...quality,
        samplingDate: quality?.samplingDate
          ? new DateObject(
              handleMergeTime(dayjs(quality?.samplingDate)).toDate(),
            )
          : null,
        sampleReleaseDate: quality?.sampleReleaseDate
          ? new DateObject(quality?.sampleReleaseDate)
          : null,
        sampleWeight: quality?.sampleWeight?.toString(),
        proteinPercentageApply: quality?.proteinPercentageApply?.toString(),
        itemId: quality?.itemId,
      });
    }
  }, [detail]);

  useEffect(() => {
    const hash = location.hash?.replace('#', '');
    if (hash) setTab(hash);
  }, []);

  const summariesOrder = [
    { title: 'Mã đơn hàng', value: watch('orderCode') },
    {
      title: 'Ngày bắt đầu',
      value: dayjs(getValues('fromDate')?.toDate()).format(DATE_FORMAT.DATE),
    },
    {
      title: 'Khối lượng (Kg)',
      value: enableDirectShipment(statusWatch)
        ? convertCurrency(Number(getValues('productionPackaging.weight')))
        : '--',
    },
    {
      title: 'Thành phẩm SX (Kg)',
      value: `${getValues('itemName') ?? ''} - ${convertCurrency(
        getValues('productionQuantity'),
        false,
      )}`,
    },
    {
      title: 'Ngày hoàn thành',
      value:
        statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) &&
        watch('updatedAt')
          ? dayjs(watch('updatedAt')).format(DATE_FORMAT.DATE)
          : '',
    },
    {
      title: 'Độ đạm (%)',
      value: `${getValues('itemPercentProtein') ?? 0}`,
    },
  ];

  const sumRawMaterial = () => {
    return (
      Number(detailWatch?.rawMaterial?.fishHead) +
      Number(detailWatch?.rawMaterial?.freshFish)
    );
  };

  const sumAdditivesW = () => {
    return (
      Number(detailWatch?.additives?.sodiumMaterial) *
        Number(detailWatch?.additives?.sodiumMaterialUom ?? 1) +
      Number(detailWatch?.additives?.sodiumCarbonateWeight) *
        Number(detailWatch?.additives?.sodiumCarbonateUom ?? 1) +
      Number(detailWatch?.additives?.sodiumBicarbonateWeight) *
        Number(detailWatch?.additives?.sodiumBicarbonateBatchUom ?? 1) +
      Number(detailWatch?.additives?.bhtWeight) *
        Number(detailWatch?.additives?.bhtUom ?? 1)
    );
  };

  const summariesStandard = [
    { title: 'Định mức', value: watch('productionStandardName') },
    {
      title: 'Nguyên liệu',
      value: enableDirectAdditives(statusWatch)
        ? `${convertCurrency(sumRawMaterial())} Kg`
        : '-- Kg',
    },
    {
      title: 'Khối lượng (Kg)',
      value: enableDirectShipment(statusWatch)
        ? convertCurrency(Number(getValues('productionPackaging.weight')))
        : '--',
    },
    {
      title: 'Khối lượng định mức (Kg)',
      value: convertCurrency(getValues('productionQuantity'), false),
    },
    {
      title: 'Phụ gia',
      value: enableDirectProduction(statusWatch)
        ? `${convertCurrency(sumAdditivesW())} Kg`
        : '-- Kg',
    },
  ];

  return (
    <ProductionManagementProvider>
      <FormProvider {...methods}>
        <ProductionCard
          headerTitle={watch('name')}
          summaries={orderIdWatch ? summariesOrder : summariesStandard}
          buttons={[
            {
              content: 'Tổng quan',
              hash: 'dashboard',
              onClick: () => setTab('dashboard'),
            },
            {
              content: 'Nguyên liệu',
              hash: 'rawMaterial',
              onClick: () => setTab('rawMaterial'),
              complete: enableDirectAdditives(statusWatch),
              warning: statusWatch === (MANUFACTURE_ORDER_STATUS.NEW as string),
            },
            {
              content: 'Phụ gia',
              hash: 'additives',
              onClick: () => setTab('additives'),
              allowClick: Boolean(detailWatch?.additives?.id),
              complete:
                detailWatch?.additives?.inspectorId &&
                enableDirectProduction(statusWatch),
              warning:
                statusWatch === (MANUFACTURE_ORDER_STATUS.ADDITIVES as string),
            },
            {
              content: 'Sản xuất',
              hash: 'production',
              onClick: () => setTab('production'),
              allowClick: detailWatch?.rawMaterial2?.items?.length > 1,
              complete:
                (typeWatch ===
                (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string)
                  ? true
                  : detailWatch?.production2?.toDate) &&
                enableDirectPackaging(statusWatch),
              warning:
                statusWatch ===
                  (MANUFACTURE_ORDER_STATUS.ADDITIVES as string) ||
                statusWatch === (MANUFACTURE_ORDER_STATUS.PRODUCTION as string),
            },
            {
              content: 'Đóng gói',
              hash: 'packaging',
              onClick: () => setTab('packaging'),
              complete: enableDirectShipment(statusWatch),
              warning:
                (detailWatch?.rawMaterial2?.items?.length === 1 &&
                  (statusWatch ===
                    (MANUFACTURE_ORDER_STATUS.ADDITIVES as string) ||
                    statusWatch ===
                      (MANUFACTURE_ORDER_STATUS.PRODUCTION as string))) ||
                statusWatch === (MANUFACTURE_ORDER_STATUS.PACKAGING as string),
            },
            {
              content: 'Kiểm định',
              hash: 'qualityControl',
              onClick: () => setTab('qualityControl'),
              complete:
                detailWatch?.productionQuality?.attributes?.isDone ||
                statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string),
              warning:
                detailWatch?.productionPackaging?.id &&
                !detailWatch?.productionQuality?.attributes?.isDone &&
                (statusWatch ===
                  (MANUFACTURE_ORDER_STATUS.PACKAGING as string) ||
                  statusWatch ===
                    (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
                  statusWatch ===
                    (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
                  statusWatch ===
                    (MANUFACTURE_ORDER_STATUS.COMPLETED as string)),
            },
            {
              content: 'Lô hàng',
              hash: 'shipment',
              onClick: () => setTab('shipment'),
              complete: enableDirectImport(statusWatch),
              warning:
                statusWatch ===
                (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string),
            },
            {
              content: 'Nhập kho',
              hash: 'import',
              onClick: () => setTab('import'),
              complete: enable(statusWatch),
              warning:
                statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string),
            },
          ]}
        >
          {(tab === 'dashboard' || tab === '') && (
            <TabDashboard onClick={e => setTab(e)} />
          )}
          {/** TAB NGUYÊN LIỆU */}
          {tab === 'rawMaterial' &&
            typeWatch ===
              (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string) && (
              <TabMaterial
                onSubmit={(values, complete) =>
                  onSubmit(values, 'rawMaterial', complete)
                }
              />
            )}
          {tab === 'rawMaterial' &&
            typeWatch ===
              (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string) && (
              <TabMaterial2
                onSubmit={(values, complete) =>
                  onSubmit(values, 'rawMaterial2', complete)
                }
              />
            )}
          {/** TAB PHỤ GIA */}
          {tab === 'additives' &&
            typeWatch ===
              (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string) && (
              <TabAdditives
                onSubmit={(values, complete) =>
                  onSubmit(values, 'additives', complete)
                }
              />
            )}
          {tab === 'additives' &&
            typeWatch ===
              (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string) && (
              <TabAdditives2
                onSubmit={(values, complete) =>
                  onSubmit(values, 'additives', complete)
                }
              />
            )}
          {/** TAB SẢN XUẤT */}
          {tab === 'production' &&
            typeWatch ===
              (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string) && (
              <TabProduction2
                onSubmit={(values, complete) =>
                  onSubmit(values, 'production', complete)
                }
              />
            )}
          {tab === 'production' &&
            typeWatch ===
              (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string) && (
              <TabProduction
                onSubmit={(values, complete) =>
                  onSubmit(values, 'production2', complete)
                }
                handleCompleteWithoutCall={res =>
                  setCompleteStateWithOutCall(res)
                }
              />
            )}
          {/** TAB ĐÓNG GÓI */}
          {tab === 'packaging' && (
            <TabPackaging
              onSubmit={(values, complete) =>
                onSubmit(values, 'productionPackaging', complete)
              }
              type={typeWatch}
              error={error}
            />
          )}
          {/** TAB KIỂM ĐỊNH */}
          {tab === 'qualityControl' && (
            <TabQualityControl
              onSubmit={(values, complete) =>
                onSubmit(values, 'qualityControl', complete)
              }
              type={typeWatch}
              handleCompleteWithoutCall={res =>
                setCompleteStateWithOutCall(res)
              }
            />
          )}
          {/** TAB LÔ HÀNG */}
          {tab === 'shipment' && (
            <TabBatch
              type={typeWatch}
              onSubmit={(values, complete) =>
                onSubmit(values, 'productionBatch', complete)
              }
              error={error}
            />
          )}
          {/** TAB NHẬP KHO */}
          {tab === 'import' && (
            <TabImport
              type={typeWatch}
              onSubmit={(values, complete) =>
                onSubmit(values, 'productionSaveInventory', complete)
              }
            />
          )}
        </ProductionCard>
      </FormProvider>

      <ManufactureOrderUpdateSuccessModal
        isOpen={isOpenModalUpdateSuccess}
        toggle={() => toggleUpdateSuccess(1, false)}
      />
      <ManufactureOrderCompleteSuccessModal
        isOpen={isOpenModalCompleteSuccess}
        toggle={toggleCompleteSuccess}
      />
    </ProductionManagementProvider>
  );
};

export default ProductionManagement;
