import { useMemo } from 'react';

import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PRODUCTION_PACKAGES_STATUS } from 'app/shared/model/enumerations/production-packages.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IProductionCommand } from 'app/shared/model/production-command.model';
import { IProductionPackage } from 'app/shared/model/production-package.model';
import dayjs from 'dayjs';
import { productionPackagesStatusBadgeMapping } from './production-packages-mapping';
import { convertCurrency } from 'app/shared/util/format';

export const generateColumns = (
  handleUpdate: (id: string) => void,
  handleDelete: (id: string) => void,
  productionCommands: IProductionCommand[],
): TableColumns<IProductionPackage> => {
  const columns: TableColumns<IProductionPackage> = useMemo(() => {
    return [
      {
        header: { render: 'Mã đóng gói' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data.packageCode} target={`packageCode-${data.id}`}>
              <EllipsisParagraph
                text={data.packageCode}
                id={`packageCode-${data.id}`}
                className="attachment-link"
                onClick={() => handleUpdate(data?.id)}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Lệnh SX' },
        body: {
          render: ({ data }) => {
            const label = `${data.manufactureOrder?.name}`;
            return (
              <Tooltip label={label} target={`manufactureOrderCode-${data.id}`}>
                <EllipsisParagraph
                  text={label}
                  id={`manufactureOrderCode-${data.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Khối lượng Kg' },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={convertCurrency(Number(data.quantity) * 50, false)}
              target={`quantity-${data.id}`}
            >
              <EllipsisParagraph
                text={convertCurrency(Number(data.quantity) * 50, false)}
                id={`quantity-${data.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Số lượng bao' },
        body: { render: ({ data }) => convertCurrency(data?.quantity, false) },
      },
      {
        header: { render: 'Người đóng gói' },
        body: {
          render: ({ data }) => {
            const label = `${data?.packageByEmployee?.employeeCode} - ${data?.packageByEmployee?.fullName}`;
            return (
              <Tooltip
                label={`${label}`}
                target={`productionQuantity-${data.id}`}
              >
                <EllipsisParagraph
                  text={`${label}`}
                  id={`productionQuantity-${data.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Ngày đóng gói' },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={dayjs(data?.packageAt).format(DATE_FORMAT.DATE)}
              target={`packageAt-${data.id}`}
            >
              <EllipsisParagraph
                text={dayjs(data?.packageAt).format(DATE_FORMAT.DATE)}
                id={`packageAt-${data.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Trạng thái' },
        body: {
          render: ({ data }) =>
            productionPackagesStatusBadgeMapping(data?.status),
        },
      },
      {
        header: { render: 'Thao tác' },
        body: {
          render: ({ data }) => (
            <Flex gap={12}>
              <AuthGuard permissionKey="PRODUCTION_PACKAGES.EDIT">
                <ButtonV2 variant="text" onClick={() => handleUpdate(data.id)}>
                  <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
                </ButtonV2>
              </AuthGuard>
              <AuthGuard permissionKey="PRODUCTION_PACKAGES.EDIT">
                <ButtonDelete onClick={() => handleDelete(data.id)} />
              </AuthGuard>
            </Flex>
          ),
        },
      },
    ];
  }, [productionCommands]);

  return columns;
};
