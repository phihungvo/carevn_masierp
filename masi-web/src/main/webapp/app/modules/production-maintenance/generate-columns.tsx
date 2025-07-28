import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, ICON_PATH } from 'app/constants/common';
import { PRODUCTION_MAINTENANCE_STATUS } from 'app/shared/model/enumerations/production-maintenance.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IProductionMaintain } from 'app/shared/model/production-maintain.model';
import dayjs from 'dayjs';

export const generateColumns = (
  handleUpdate: (id: string) => void,
  handleDelete: (id: string) => void,
): TableColumns<IProductionMaintain> => {
  const columns: TableColumns<IProductionMaintain> = [
    {
      header: { render: 'Mã lô hàng' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data.productBatchCode}
            target={`productBatchCode-${data.id}`}
          >
            <EllipsisParagraph
              text={data.productBatchCode}
              id={`productBatchCode-${data.id}`}
              width={150}
              className="attachment-link"
              onClick={() => handleUpdate(data?.id)}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Tên lô hàng' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data.productBatchName}
            target={`productBatchName-${data.id}`}
          >
            <EllipsisParagraph
              text={data.productBatchName}
              id={`productBatchName-${data.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Mã đóng gói' },
      body: { render: ({ data }) => data?.productPackageDTO?.packageCode },
    },
    {
      header: { render: 'Ngày SX' },
      body: {
        render: ({ data }) =>
          dayjs(data?.manufactureDate).format(DATE_FORMAT.DATE),
      },
    },
    {
      header: { render: 'Sử dụng đến' },
      body: {
        render: ({ data }) => dayjs(data?.expiredDate).format(DATE_FORMAT.DATE),
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data.note} target={`note-${data.id}`}>
            <EllipsisParagraph text={data.note} id={`note-${data.id}`} />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Thao tác' },
      body: {
        render: ({ data }) => (
          <Flex gap={16}>
            <AuthGuard permissionKey='PRODUCTION_MAINTENANCE.EDIT'>
              {data?.status !== PRODUCTION_MAINTENANCE_STATUS?.CANCELED && (
                  <ButtonV2 variant="text" onClick={() => handleUpdate(data.id)}>
                    <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
                  </ButtonV2>
              )}
              {data?.status !== PRODUCTION_MAINTENANCE_STATUS?.CANCELED && (
                  <ButtonDelete onClick={() => handleDelete(data.id)} />
              )}
            </AuthGuard>
          </Flex>
        ),
      },
    },
  ];

  return columns;
};
