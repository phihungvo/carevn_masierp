import dayjs from 'dayjs';
import { useMemo } from 'react';

import BadgeV2 from 'app/components/badge/badge-v2';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, ICON_PATH } from 'app/constants/common';
import {
  MANUFACTURE_ORDER_STATUS,
  MANUFACTURE_ORDER_TYPE,
} from 'app/shared/model/enumerations/production-command.model';
import { IProductionCommand } from 'app/shared/model/production-command.model';
import { convertCurrency } from 'app/shared/util/format';
import { manufactureOrderBadgeMapping } from './production-mapping';
import {
  enableDirectAdditives,
  enableDirectShipment,
} from './production-ultis';

export const generateColumns = (
  handleUpdate: (id: string) => void,
  handleDelete: (id: string) => void,
  type: MANUFACTURE_ORDER_TYPE,
  dataList?: { name: string; id: string }[],
  handleDetailOrder?: (id: string) => void,
): TableColumns<IProductionCommand> => {
  const columnsByStandard: TableColumns<IProductionCommand> = useMemo(() => {
    return [
      {
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.code} target={`code-${data?.id}`}>
              <EllipsisParagraph
                text={data?.code}
                width={150}
                id={`code-${data?.id}`}
                className="attachment-link"
                onClick={() => handleUpdate(data?.id)}
              />
            </Tooltip>
          ),
        },
      },
      {
        body: {
          render: ({ data }) =>
            enableDirectAdditives(data?.status) &&
            data?.attributes?.rawMaterial?.fishHead
              ? convertCurrency(Number(data?.attributes?.rawMaterial?.fishHead))
              : '-',
        },
      },
      {
        body: {
          render: ({ data }) =>
            enableDirectAdditives(data?.status) &&
            data?.attributes?.rawMaterial?.freshFish
              ? convertCurrency(
                  Number(data?.attributes?.rawMaterial?.freshFish),
                )
              : '-',
        },
      },
      {
        body: {
          render: ({ data }) => {
            const rawMaterial = data?.attributes?.rawMaterial;
            return enableDirectAdditives(data?.status) &&
              rawMaterial?.fishHead &&
              rawMaterial?.freshFish
              ? convertCurrency(
                  Number(data?.attributes?.rawMaterial?.fishHead ?? 0) +
                    Number(data?.attributes?.rawMaterial?.freshFish ?? 0),
                )
              : '-';
          },
        },
      },
      {
        body: {
          render: ({ data }) =>
            enableDirectShipment(data?.status) &&
            data?.productPackageDTO?.quantity
              ? convertCurrency(Number(data?.productPackageDTO?.quantity * 50))
              : '-',
        },
      },
      {
        body: {
          render: ({ data }) =>
            data?.qualityCheckSampleDTO?.attributes?.isDone &&
            data?.qualityCheckSampleDTO?.itemId &&
            data?.qualityCheckSampleDTO?.proteinPercentageApply
              ? data?.qualityCheckSampleDTO?.proteinPercentageApply ?? 0
              : '-',
        },
      },
      {
        body: {
          render: ({ data }) => {
            const quantity = Number(data?.productPackageDTO?.quantity ?? 0);
            const amount =
              (Number(data?.attributes?.rawMaterial?.fishHead ?? 0) +
                Number(data?.attributes?.rawMaterial?.freshFish ?? 0)) /
              (quantity === 0 ? 1 : quantity * 50);
            return enableDirectShipment(data.status) ? (
              <Tooltip
                label={`${Number(`${amount}`)}`}
                target={`name-${data.id}`}
              >
                <EllipsisParagraph
                  text={`${Number(`${amount}`)}`}
                  id={`name-${data.id}`}
                />
              </Tooltip>
            ) : (
              '-'
            );
          },
        },
      },
      {
        body: {
          render: ({ data }) => {
            return data?.qualityCheckSampleDTO?.itemId &&
              data?.qualityCheckSampleDTO?.proteinPercentageApply ? (
              <BadgeV2 className="bv2 pr-approved">Đã có kết quả</BadgeV2>
            ) : (
              <BadgeV2 className="bv2 pr-new">Chờ kết quả</BadgeV2>
            );
          },
        },
      },
      {
        body: {
          render: ({ data }) => manufactureOrderBadgeMapping(data?.status),
        },
      },
      {
        body: {
          render: ({ data }) => (
            <Flex gap={16}>
              <ButtonV2 variant="text" onClick={() => handleUpdate(data.id)}>
                <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
              </ButtonV2>
              <ButtonDelete
                onClick={() => handleDelete(data.id)}
                disabled={
                  data?.status ===
                    (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
                  data?.status ===
                    (MANUFACTURE_ORDER_STATUS.CANCELLED as string)
                }
              />
            </Flex>
          ),
        },
      },
    ];
  }, [dataList]);

  const columnsByOrder: TableColumns<IProductionCommand> = useMemo(() => {
    return [
      {
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.code} target={`code-${data?.id}`}>
              <EllipsisParagraph
                text={data?.code}
                width={150}
                id={`code-${data?.id}`}
                className="attachment-link"
                onClick={() => handleUpdate(data?.id)}
              />
            </Tooltip>
          ),
        },
      },
      {
        body: {
          render: ({ data }) => {
            const orderName = dataList?.find(x => x.id === data?.orderId)?.name;
            return orderName ? (
              <Tooltip label={orderName} target={`name-${data.id}`}>
                <EllipsisParagraph
                  text={orderName}
                  id={`name-${data.id}`}
                  className="attachment-link"
                  onClick={() => handleDetailOrder(data?.orderId)}
                />
              </Tooltip>
            ) : (
              '-'
            );
          },
        },
      },
      {
        body: {
          render: ({ data }) => {
            const label = `${
              data?.attributes?.percentProtein ?? 0
            } - ${convertCurrency(data?.productionQuantity)} Kg`;
            return (
              <Tooltip label={label} target={`req-${data.id}`}>
                <EllipsisParagraph
                  text={label}
                  id={`req-${data.id}`}
                  width={150}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        body: {
          render: ({ data }) =>
            enableDirectAdditives(data?.status) &&
            data?.attributes?.rawMaterial2?.items?.[0]?.quantityUse
              ? convertCurrency(
                  Number(
                    data?.attributes?.rawMaterial2?.items?.[0]?.quantityUse ??
                      0,
                  ),
                )
              : '-',
        },
      },
      {
        body: {
          render: ({ data }) =>
            enableDirectAdditives(data?.status) &&
            data?.attributes?.rawMaterial2?.items?.[1]?.quantityUse
              ? convertCurrency(
                  Number(
                    data?.attributes?.rawMaterial2?.items?.[1]?.quantityUse ??
                      0,
                  ),
                )
              : '-',
        },
      },
      {
        body: {
          render: ({ data }) =>
            enableDirectShipment(data?.status) &&
            data?.productPackageDTO?.quantity
              ? convertCurrency(
                  Number((data?.productPackageDTO?.quantity ?? 0) * 50),
                )
              : '-',
        },
      },
      {
        body: {
          render: ({ data }) =>
            (data?.qualityCheckSampleDTO?.attributes?.isDone &&
              data?.qualityCheckSampleDTO?.itemId &&
              data?.qualityCheckSampleDTO?.proteinPercentageApply) ??
            '-',
        },
      },
      {
        body: {
          render: ({ data }) => {
            const date = data?.fromDate
              ? dayjs(data?.fromDate).format(DATE_FORMAT.DATE)
              : '-';
            return (
              <Tooltip label={date} target={`note-${data.id}`}>
                <EllipsisParagraph text={date} id={`note-${data.id}`} />
              </Tooltip>
            );
          },
        },
      },
      {
        body: {
          render: ({ data }) => {
            const date = data?.toDate
              ? dayjs(data?.toDate).format(DATE_FORMAT.DATE)
              : '-';
            return (
              <Tooltip label={date} target={`note-${data.id}`}>
                <EllipsisParagraph text={date} id={`note-${data.id}`} />
              </Tooltip>
            );
          },
        },
      },
      {
        body: {
          render: ({ data }) => {
            return data?.qualityCheckSampleDTO?.itemId &&
              data?.qualityCheckSampleDTO?.proteinPercentageApply ? (
              <BadgeV2 className="bv2 pr-approved">Đã có kết quả</BadgeV2>
            ) : (
              <BadgeV2 className="bv2 pr-new">Chờ kết quả</BadgeV2>
            );
          },
        },
      },
      {
        body: {
          render: ({ data }) => manufactureOrderBadgeMapping(data?.status),
        },
      },
      {
        body: {
          render: ({ data }) => (
            <Flex gap={16}>
              <AuthGuard
                permissionKey={
                  type === MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER
                    ? 'PRODUCTION_MANUFACTURE_ORDER.EDIT'
                    : 'PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT'
                }
              >
                <ButtonV2 variant="text" onClick={() => handleUpdate(data.id)}>
                  <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
                </ButtonV2>
              </AuthGuard>
              <AuthGuard
                permissionKey={
                  type === MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER
                    ? 'PRODUCTION_MANUFACTURE_ORDER.EDIT'
                    : 'PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT'
                }
              >
                <ButtonDelete
                  onClick={() => handleDelete(data.id)}
                  disabled={
                    data?.status ===
                      (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
                    data?.status ===
                      (MANUFACTURE_ORDER_STATUS.CANCELLED as string)
                  }
                />
              </AuthGuard>
            </Flex>
          ),
        },
      },
    ];
  }, [dataList]);

  return type === MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER
    ? columnsByOrder
    : columnsByStandard;
};
