import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, ICON_PATH } from 'app/constants/common';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IProductionRouting } from 'app/shared/model/production-routing.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useMemo } from 'react';

export const generateColumns = (
  handleUpdate: (id: string) => void,
  handleDelete: (id: string) => void,
): TableColumns<IProductionRouting> => {
  const columns: TableColumns<IProductionRouting> = useMemo(() => {
    return [
      {
        header: { render: 'Mã lưu kho' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.name} target={`name-${data.id}`}>
              <EllipsisParagraph
                text={data?.name}
                id={`name-${data.id}`}
                width={150}
                className="attachment-link"
                onClick={() => handleUpdate(data?.id)}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Ngày lưu kho' },
        body: {
          render: ({ data }) => {
            const label = dayjs(data?.warehouseDate).format(DATE_FORMAT.DATE);
            return (
              <Tooltip label={label} target={`date-${data.id}`}>
                <EllipsisParagraph text={label} id={`date-${data.id}`} />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Nơi lưu trữ' },
        body: {
          render: ({ data }) => {
            const label = data?.warehouseDTO?.name;
            return (
              <Tooltip label={label} target={`storage-${data.id}`}>
                <EllipsisParagraph text={label} id={`storage-${data.id}`} />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Lô hàng' },
        body: {
          render: ({ data }) => {
            const label = `${data?.productMaintainDTO?.productBatchCode} - ${data?.productMaintainDTO?.productBatchName}`;
            return (
              <Tooltip label={label} target={`storage-${data.id}`}>
                <EllipsisParagraph text={label} id={`storage-${data.id}`} />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Mã đóng gói' },
        body: {
          render: ({ data }) => {
            const label = `${data?.productMaintainDTO?.productPackageDTO?.packageCode}`;
            return (
              <Tooltip label={label} target={`storage-${data.id}`}>
                <EllipsisParagraph text={label} id={`storage-${data.id}`} />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Khối lượng (Kg)' },
        body: {
          render: ({ data }) => {
            const qty =
              data?.productMaintainDTO?.productPackageDTO?.quantity ?? 0;
            return (
              <Tooltip
                label={convertCurrency(qty * 50, false)}
                target={`storage-${data.id}`}
              >
                <EllipsisParagraph
                  text={convertCurrency(qty * 50, false)}
                  id={`storage-${data.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: '' },
        body: {
          render: ({ data }) => (
            <Flex gap={12}>
                <AuthGuard permissionKey='PRODUCTION_ROUTINGS.EDIT'>
                  <ButtonV2 variant="text" onClick={() => handleUpdate(data.id)}>
                    <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
                  </ButtonV2>
                  <ButtonDelete onClick={() => handleDelete(data.id)} />
                </AuthGuard>
            </Flex>
          ),
        },
      },
    ];
  }, []);

  return columns;
};
