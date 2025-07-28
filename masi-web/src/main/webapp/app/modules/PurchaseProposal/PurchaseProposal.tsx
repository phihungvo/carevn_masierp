import { Spin } from 'antd';
import ButtonAccept from 'app/components/ButtonV2/ButtonAccept';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonCancel from 'app/components/ButtonV2/ButtonCancel';
import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import ButtonEdit from 'app/components/ButtonV2/ButtonEdit';
import ButtonExcel from 'app/components/ButtonV2/ButtonExcel';
import ButtonFilter from 'app/components/ButtonV2/ButtonFilter';
import ButtonReject from 'app/components/ButtonV2/ButtonReject';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import InputSearch from 'app/components/input/input-search';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useAccountApp from 'app/hooks/use-account-app';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useEmployeesObj from 'app/hooks/use-employees-object';
import useGoTo from 'app/hooks/use-go-to';
import useSearchQuery from 'app/hooks/use-search-query';
import useSuppliesRequest from 'app/hooks/use-supplies-request';
import useAction from 'app/hooks/user-action';
import { SuppliesRequestStatus } from 'app/shared/model/enumerations/supplies-request';
import {
  ISuppliesRequest,
  ISuppliesRequestParams,
} from 'app/shared/model/supplies-request.model';
import { convertCurrency, iconPath } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { RequestPaymentStatusBadgeMapping } from '../request-payment/request-payment-mapping';
import Accept from './Actions/Accept';
import Cancel from './Actions/Cancel';
import Reject from './Actions/Reject';
import RequestAccept from './Actions/RequestAccept';
import Filter from './Filter';
import { Link } from 'react-router-dom';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetSuppliesRequests, useExportSuppliesRequest } = useSuppliesRequest;

type Props = {};

const PurchaseProposal = (props: Props) => {
  const account = useAccountApp();
  const { handleQuery, handleSearch, search, query, setQuery } =
    useSearchQuery<ISuppliesRequestParams>({
      defaultValue: {
        query: { page: 0, size: 10 },
      },
      howToResolveData: (key, value) => {
        switch (key) {
          default:
            return value;
        }
      },
    });
  const { goTo } = useGoTo();
  const { actions, handleToggleAction } = useAction();
  const { employeesObj, handleEmployeeChange } = useEmployeesObj();
  const [isOpenFilter, setIsOpenFilter] = useState<boolean>(false);

  const supplies_query = useGetSuppliesRequests(query);
  const { trigger, data: dataFile } = useExportSuppliesRequest();
  // hook download xlsx

  useDownloadXlsx(dataFile?.data, `DeXuatMuaHang`, 'xlsx');

  const columns: TableColumns<ISuppliesRequest> = [
    {
      header: {
        render: 'STT',
      },
      body: {
        render: ({ index }) => index + 1,
      },
    },
    {
      header: {
        render: 'Mã đề xuất',
      },
      body: {
        render: ({ data }) => (
          <Link to={`form/${data?.id}`} className="attachment-link">
            {data?.requestNumber}
          </Link>
        ),
      },
    },
    {
      header: {
        render: 'Loại đề xuất',
      },
      body: {
        render: ({ data }) => data?.requestType?.name,
      },
    },
    {
      header: {
        render: 'Ngày lập',
      },
      body: {
        render: ({ data }) => dayjs(data?.requestDate).format(DATE_FORMAT.DATE),
      },
    },
    {
      header: {
        render: 'Người lập',
      },
      body: {
        render: ({ data }) => {
          if (!employeesObj) return <Spin />;
          return employeesObj?.[data?.createdBy]?.fullName;
        },
      },
    },
    {
      header: {
        render: 'Số lượng',
      },
      body: {
        render: ({ data }) => data?.totalQuantity,
      },
    },
    {
      header: {
        render: 'Số lượng đã giao',
      },
      body: {
        render: ({ data }) => data?.deliveredQuantity,
      },
    },
    {
      header: {
        render: 'Số lượng còn nợ',
      },
      body: {
        render: ({ data }) => data?.remainingQuantity,
      },
    },
    {
      header: {
        render: 'Thành tiền',
      },
      body: {
        render: ({ data }) => convertCurrency(data?.totalAmount),
      },
    },
    {
      header: {
        render: 'Nội dung',
      },
      body: {
        render: ({ data }) => data?.note,
      },
    },
    {
      header: {
        render: 'Trạng thái',
      },
      body: {
        render: ({ data }) =>
          RequestPaymentStatusBadgeMapping(data?.requestStatus as any),
      },
    },
    {
      header: {
        render: <></>,
      },
      body: {
        render: ({ data }) => {
          let item = data?.requestApprovals?.find(
            item => item?.employeeId === account?.employeeId,
          );
          let isApprovalPerson = item?.employeeId === account?.employeeId;
          let isContainSignal = !!item?.approvedSign;
          let isCreatedByYourSelf = data?.createdBy === account?.employeeId;
          let isNew = data?.requestStatus === SuppliesRequestStatus.NEW;
          let isWaiting =
            data?.requestStatus === SuppliesRequestStatus.WAITING_APPROVE;
          let isRejected =
            data?.requestStatus === SuppliesRequestStatus.REJECTED;

          let isCanRejectOrApprove =
            isApprovalPerson && isWaiting && !isContainSignal;
          let isCanRequestAccept = isCreatedByYourSelf && (isNew || isRejected);
          let isCanCanceled = isCreatedByYourSelf && isNew;

          return (
            <Flex align="center">
              <AuthGuard permissionKey='SUPPLIES_REQUESTS.EDIT'>
                <ButtonEdit
                  onClick={goTo(PATH.SUPPLIES_REQUESTS_FORM + '/' + data?.id)}
                />
              </AuthGuard>
              <ButtonDropDown
                items={[
                  {
                    children: (
                      <ButtonCancel
                        disabled={!isCanCanceled}
                        onClick={
                          isCanCanceled &&
                          handleToggleAction({ key: 'cancel', id: data?.id })
                        }
                      />
                    ),
                  },
                  {
                    children: (
                      <ButtonV2
                        disabled={!isCanRequestAccept}
                        onClick={
                          isCanRequestAccept &&
                          handleToggleAction({
                            key: 'request_accept',
                            id: data?.id,
                          })
                        }
                        variant="text"
                        left_section={
                          <img
                            src={iconPath('request-accept.svg')}
                            alt="reqest accept"
                          />
                        }
                      >
                        Trình Duyệt
                      </ButtonV2>
                    ),
                  },
                  {
                    children: (
                      <ButtonAccept
                        disabled={!isCanRejectOrApprove}
                        onClick={
                          isCanRejectOrApprove &&
                          handleToggleAction({ key: 'accept', id: data?.id })
                        }
                      />
                    ),
                  },
                  {
                    children: (
                      <ButtonReject
                        disabled={!isCanRejectOrApprove}
                        onClick={
                          isCanRejectOrApprove &&
                          handleToggleAction({ key: 'reject', id: data?.id })
                        }
                      />
                    ),
                  },
                ]}
              >
                <img src={iconPath('more-v2.svg')} alt="more" />
              </ButtonDropDown>
            </Flex>
          );
        },
      },
    },
  ];

  const toggleFilter = () => setIsOpenFilter(!isOpenFilter);

  useEffect(() => {
    supplies_query?.data &&
      handleEmployeeChange(
        supplies_query?.data?.data?.map(item => item?.createdBy),
      );
  }, [supplies_query?.data]);

  return (
    <CardV2
      header={
        <Flex justify="space-between">
          <Typography level={4}>Đề xuất mua hàng</Typography>
          <AuthGuard permissionKey="SUPPLIES_REQUESTS.CREATE">
            <ButtonAdd
              text="Thêm"
              onClick={goTo(PATH.SUPPLIES_REQUESTS_FORM)}
            />
          </AuthGuard>
        </Flex>
      }
    >
      <div>
        <Flex
          justify="space-between"
          align="center"
          style={{ marginBottom: '8px' }}
        >
          <InputSearch
            value={search.search}
            onChange={handleSearch('search')}
          />
          <Flex gap={16}>
            <ButtonFilter onClick={toggleFilter} />
            <AuthGuard permissionKey="SUPPLIES_REQUESTS.EXPORT">
              <ButtonExcel onClick={trigger} />
            </AuthGuard>
          </Flex>
        </Flex>
        <TablePagination<ISuppliesRequest>
          table_id="purchase_proposal"
          columns={columns}
          data={supplies_query?.data?.data}
          isLoading={supplies_query?.isLoading}
          total_pages={supplies_query?.data?.totalRecord}
          itemsPerPage={query.size}
          handlePageClick={handleQuery('page')}
          handlePageSizeChange={handleQuery('size')}
        />
      </div>
      <Accept
        id_detail={actions?.id}
        isOpen={actions?.accept}
        toggle={handleToggleAction({ key: 'accept' })}
      />
      <Reject
        id_detail={actions?.id}
        isOpen={actions?.reject}
        toggle={handleToggleAction({ key: 'reject' })}
      />
      <Cancel
        id_detail={actions?.id}
        isOpen={actions?.cancel}
        toggle={handleToggleAction({ key: 'cancel' })}
      />
      <RequestAccept
        id_detail={actions?.id}
        isOpen={actions?.request_accept}
        toggle={handleToggleAction({ key: 'request_accept' })}
      />
      <Filter
        query={query}
        setQuery={setQuery}
        isOpen={isOpenFilter}
        toggle={toggleFilter}
      />
    </CardV2>
  );
};

export default PurchaseProposal;
