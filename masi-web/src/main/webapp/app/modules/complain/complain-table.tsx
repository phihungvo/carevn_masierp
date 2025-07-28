import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useCallCenter from 'app/hooks/use-call-center';
import { ICallCenter } from 'app/shared/model/call-center.model';
import {
  CALL_CENTER_STATUS,
  CALL_CENTER_TYPE_PAGE,
} from 'app/shared/model/enumerations/call-center';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import {
  complainMappingGroup,
  complainMappingSource,
  complainStatusBadge,
} from './complain-mapping';
import { ComplainContext } from './complain-provider';
import { useAppSelector } from 'app/config/store';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetCallCentersQuery } = useCallCenter;

const ComplainTable = () => {
  const { filter, setFilter, toggleConfirmDelete, setSelectedRecord } =
    useContext(ComplainContext);

    const authorities = useAppSelector(
      state => state.authentication.account.authorities,
    );

  const navigate = useNavigate();

  const { data } = useGetCallCentersQuery({
    ...filter,
    'typePageCs.contains': CALL_CENTER_TYPE_PAGE.COMPLAINT,
    sort: 'createdAt,desc',
  });

  const toUpdate = (id: string) => {
    navigate(PATH.COMPLAIN_UPDATE.replace(':id', id));
  };

  const columns: TableColumns<ICallCenter> = [
    {
      header: { render: 'Ngày tiếp nhận' },
      body: {
        render: ({ data }) => {
          const value = `${dayjs(data?.receptionDate).format(
            DATE_FORMAT.DATE_TIME_ONLY,
          )}`;
          return (
            <p className="attachment-link" onClick={() => {
              if (!isHasPermission(authorities, 'CUSTOMER_SERVICES_COMPLAIN.EDIT')) return;
              toUpdate(data.id)
            }}>
              <EllipsisParagraph
                text={value}
                width={160}
                id={`dateRecord-${data.id}`}
              />
            </p>
          );
        },
      },
    },
    {
      header: { render: 'Nhóm' },
      body: {
        render: ({ data }) => {
          const value = complainMappingGroup(data?.groupCS);
          return (
            <Tooltip label={value} target={`groupCS-${data?.id}`}>
              <EllipsisParagraph
                text={value}
                width={150}
                id={`groupCS-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Nguồn' },
      body: {
        render: ({ data }) => {
          const value = complainMappingSource(data?.sourceCs);
          return (
            <Tooltip label={value} target={`sourceCs-${data?.id}`}>
              <EllipsisParagraph
                text={value}
                width={100}
                id={`sourceCs-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Nội dung' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.problemContent}
            target={`problemContent-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.problemContent}
              width={200}
              id={`problemContent-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Hướng giải quyết' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.attribute?.[0]?.resolutionContent}
            target={`content-${data?.id}`}
          >
            <EllipsisParagraph
              text={data?.attribute?.[0]?.resolutionContent}
              width={200}
              id={`content-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Ngày giải quyết' },
      body: {
        render: ({ data }) => {
          const date =
            data?.status === CALL_CENTER_STATUS.CLOSED
              ? data?.updatedAt
                ? dayjs(data?.updatedAt).format(DATE_FORMAT.DATE_TIME_2)
                : ''
              : '';
          return (
            <Tooltip label={date} target={`updatedAt-${data?.id}`}>
              <EllipsisParagraph
                text={date}
                width={200}
                id={`updatedAt-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Quá hạn' },
      body: {
        render: ({ data }) => {
          const label = data?.employeeAssignDate
            ? dayjs(new Date()).diff(
                dayjs(data?.employeeAssignDate).add(60, 'minutes'),
                'minute',
              )
            : 0;
          return (
            <Tooltip
              label={label <= 0 ? '' : `${label} phút`}
              target={`endDate-${data?.id}`}
            >
              <EllipsisParagraph
                text={label <= 0 ? '' : `${label} phút`}
                width={200}
                id={`endDate-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) => complainStatusBadge(data?.status as any),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <AuthGuard permissionKey='CUSTOMER_SERVICES_COMPLAIN.VIEW'>
              <ButtonV2 variant="text" onClick={() => toUpdate(data?.id)}>
                <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
              </ButtonV2>
            </AuthGuard>

            <AuthGuard permissionKey='CUSTOMER_SERVICES_COMPLAIN.EDIT'>
              <ButtonDelete
                onClick={() => {
                  setSelectedRecord(data?.id);
                  toggleConfirmDelete();
                }}
                disabled={data?.status === CALL_CENTER_STATUS.COMPLETED}
              />
            </AuthGuard>
          </Flex>
        ),
      },
    },
  ];

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<ICallCenter>
      table_id="complains"
      columns={columns}
      data={data?.data || []}
      total_pages={data?.totalRecord || 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
};

export default ComplainTable;
