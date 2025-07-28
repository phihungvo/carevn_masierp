import dayjs from 'dayjs';
import { useMemo } from 'react';

import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { ICON_PATH } from 'app/constants/common';
import { productionStandardStatusBadgeMapping } from 'app/modules/production-standard/production-standard-mapping';
import { PRODUCTION_STANDARD_STATUS } from 'app/shared/model/enumerations/production-standard.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IProductionStandard } from 'app/shared/model/production-standard.model';
import { IUom } from 'app/shared/model/uom.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

export const generateColumns = (
  handleEditRecord: (id: string) => void,
  handleDisposeRecord: (id: string) => void,
  uomData: IUom[],
): TableColumns<IProductionStandard> => {
  const columns: TableColumns<IProductionStandard> = useMemo(() => {
    return [
      {
        header: { render: 'Mã định mức' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.code} target={`code-${data?.id}`}>
              <EllipsisParagraph
                text={data?.code}
                width={150}
                id={`code-${data?.id}`}
                className="attachment-link"
                onClick={() => handleEditRecord(data?.id)}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Tên định mức' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data.name} target={`name-${data.id}`}>
              <EllipsisParagraph text={data.name} id={`name-${data.id}`} />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Khối lượng (Kg)' },
        body: {
          render: ({ data }) =>
            formatDecimalPrecision(data?.productionPowderQty),
        },
      },
      {
        header: { render: 'Tháng/Năm' },
        body: {
          render: ({ data }) => dayjs(data?.dueDate).format('MM/YYYY'),
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
        header: { render: 'Trạng thái' },
        body: {
          render: ({ data }) =>
            productionStandardStatusBadgeMapping(data?.status),
        },
      },
      {
        header: { render: 'Thao tác' },
        body: {
          render: ({ data }) => (
            <Flex gap={16}>
              <AuthGuard permissionKey='PRODUCTION_STANDARD.EDIT'>
                {data?.status !== PRODUCTION_STANDARD_STATUS?.CANCELED && (
                    <ButtonDelete onClick={() => handleDisposeRecord(data.id)} />
                )}
              </AuthGuard>
            </Flex>
          ),
        },
      },
    ];
  }, [uomData]);

  return columns;
};
