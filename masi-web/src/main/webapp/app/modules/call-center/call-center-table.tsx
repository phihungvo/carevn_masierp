import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import {
  DATE_FORMAT,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE_NAX,
  ICON_PATH,
  isHasPermission,
} from 'app/constants/common';
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
  callCenterMappingGroup,
  callCenterStatusBadge,
} from './call-center-mapping';
import { CallCenterContext } from './call-center-provider';
import useCustomers from 'app/hooks/use-customers';
import { useAppSelector } from 'app/config/store';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetEnabledCustomers } = useCustomers;
const { useGetCallCentersQuery } = useCallCenter;

const CallCenterTable = () => {
  const { filter, setFilter, toggleConfirmDelete, setSelectedRecord } =
    useContext(CallCenterContext);

    const authorities = useAppSelector(
      state => state.authentication.account.authorities,
    );

  const navigate = useNavigate();

  const { data: customers } = useGetEnabledCustomers({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const { data } = useGetCallCentersQuery({
    ...filter,
    'typePageCs.contains': CALL_CENTER_TYPE_PAGE.CALL_CENTER,
    sort: 'createdAt,desc',
  });

  const toUpdate = (id: string) => {
    navigate(PATH.CALL_CENTER_UPDATE.replace(':id', id));
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
              if (!isHasPermission(authorities, 'CUSTOMER_SERVICES_CALL_CENTER.VIEW')) return;
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
          const value = callCenterMappingGroup(data?.groupCS);
          return (
            <Tooltip label={value} target={`groupCS-${data?.id}`}>
              <EllipsisParagraph
                text={value}
                width={200}
                id={`groupCS-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Thông tin' },
      body: {
        render: ({ data }) => {
          const selected = customers?.data?.find(
            x => x.id === data?.customerId,
          );
          if (selected) {
            return (
              <Flex direction="column" gap={8}>
                <EllipsisParagraph
                  text={`${selected?.customerCode}`}
                  width={200}
                />
                <Tooltip
                  label={`${selected?.companyName}`}
                  target={`companyName-${data?.id}`}
                >
                  <EllipsisParagraph
                    text={`${selected?.companyName}`}
                    width={300}
                    id={`companyName-${data?.id}`}
                  />
                </Tooltip>
                <Tooltip
                  label={`${selected?.address}`}
                  target={`companyName-${data?.id}`}
                >
                  <EllipsisParagraph
                    text={`${selected?.address}`}
                    width={300}
                    id={`companyName-${data?.id}`}
                  />
                </Tooltip>
                <EllipsisParagraph
                  text={`${selected?.phoneNumber}`}
                  width={200}
                />
                <Tooltip label={`Người liên lạc: `} target={`note-${data?.id}`}>
                  <EllipsisParagraph
                    text={`Người liên lạc: `}
                    width={300}
                    id={`note-${data?.id}`}
                  />
                </Tooltip>
              </Flex>
            );
          } else <></>;
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
        render: ({ data }) => callCenterStatusBadge(data?.status as any),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <AuthGuard permissionKey='CUSTOMER_SERVICES_CALL_CENTER.VIEW'>
              <ButtonV2
                variant="text"
                isBoxShadow={false}
                onClick={() => toUpdate(data?.id)}
              >
                <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
              </ButtonV2>
            </AuthGuard>

            <AuthGuard permissionKey='CUSTOMER_SERVICES_CALL_CENTER.EDIT'>
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
      table_id="call-centers"
      columns={columns}
      data={data?.data || []}
      total_pages={data?.totalRecord || 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
};

export default CallCenterTable;
