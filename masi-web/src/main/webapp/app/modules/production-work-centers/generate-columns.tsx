import ButtonIcon from 'app/components/button-icon/button-icon';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, ICON_PATH } from 'app/constants/common';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IWorkCenter } from 'app/shared/model/work-center.model';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import { prodWorkCenterStatusBadgeMapping } from './production-work-centers-mapping';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';

export const generateColumns = (
  handleUpdate: (id: string) => void,
  handleDelete: (id: string) => void,
): TableColumns<IWorkCenter> => {
  const columns: TableColumns<IWorkCenter> = useMemo(() => {
    return [
      {
        header: { render: 'Mã' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.code} target={`code-${data.id}`}>
              <EllipsisParagraph
                text={data?.code}
                id={`code-${data.id}`}
                className="attachment-link"
                onClick={() => handleUpdate(data?.id)}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Tên' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.name} target={`name-${data.id}`}>
              <EllipsisParagraph text={data?.name} id={`name-${data.id}`} />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Ngày kiểm tra gần nhất' },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={dayjs(data?.lastCheckedAt).format(DATE_FORMAT.DATE)}
              target={`lastCheckedAt-${data.id}`}
            >
              <EllipsisParagraph
                text={dayjs(data?.lastCheckedAt).format(DATE_FORMAT.DATE)}
                id={`lastCheckedAt-${data.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Ghi chú' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.note} target={`note-${data.id}`}>
              <EllipsisParagraph text={data?.note} id={`note-${data.id}`} />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Trạng thái' },
        body: {
          render: ({ data }) => prodWorkCenterStatusBadgeMapping(data?.status),
        },
      },
      {
        header: { render: 'Thao tác' },
        body: {
          render: ({ data }) => (
            <Flex gap={12}>
                <AuthGuard permissionKey='PRODUCTION_WORK_CENTERS.EDIT'>
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
