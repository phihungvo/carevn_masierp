import { Flex } from 'antd';
import AcceptActionBtn from 'app/components/accept-action/AcceptActionBtn';
import BadgeV2 from 'app/components/badge/badge-v2';
import ButtonAccept from 'app/components/ButtonV2/ButtonAccept';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonCancel from 'app/components/ButtonV2/ButtonCancel';
import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import ButtonEdit from 'app/components/ButtonV2/ButtonEdit';
import ButtonFilter from 'app/components/ButtonV2/ButtonFilter';
import DownloadExcelBtn from 'app/components/ButtonV2/DownloadExcelBtn';
import ModalWrapper from 'app/components/ButtonV2/ModalWrapper';
import Card from 'app/components/card/card';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import RejectActionBtn from 'app/components/reject-action/RejectActionBtn';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useGoTo from 'app/hooks/use-go-to';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { iconPath } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { Link } from 'react-router-dom';
import DepreciationFilter from '../depreciation/modals/depreciation-filter';
import { Depreciation } from '../depreciation/types/list';
import './allocation.scss';
import { allocationApi } from './apis/api';
import { allocationList, useAllocationList } from './apis/hook';
import { allocationMappingStatusColor, allocationMappingStatusText } from './utils/allocation';
import Accept from './component/actions/Accept';
import Reject from './component/actions/Reject';
import useAction from 'app/hooks/user-action';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import ButtonReject from 'app/components/ButtonV2/ButtonReject';
import { RequestPaymentStatusBadgeMapping } from '../request-payment/request-payment-mapping';
import Cancel from './component/actions/Cancel';
import RequestAccept from './component/actions/RequestAccept';
import { isEnableApprovalOrReject, isEnableCancel, isEnableRequestApproval } from 'app/shared/util/logicActions';
import useAccountApp from 'app/hooks/use-account-app';
import { Allociation } from './types/list';

function Allocation() {

  const { employeeId } = useAccountApp()
  const { goTo } = useGoTo()
  const { actions, handleToggleAction } = useAction()

  const {
    query,
    handleQuery,
    allocationRes,
    handleSearch,
    setQuery
  } = useAllocationList()

  const columns: TableColumns<Allociation> = [
    {
      header: { render: 'Kỳ phân bổ' },
      body: {
        render: ({ data }) => (
          <Link to={PATH.ALLOCATION_CREATE + '/' + data?.id}>
            <p className="attachment-link">
              <EllipsisParagraph
                text={data?.code}
                width={100}
                id={`depreciationPeriod-${data?.id}`}
              />
            </p>
          </Link>
        ),
      },
    },
    {
      header: { render: 'Ngày tính phân bổ' },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data.depreciationDate}
            target={`depreciationDate-${data.id}`}
          >
            <EllipsisParagraph
              text={`${dayjs(data.depreciationDate).format(
                DATE_FORMAT.DATE,
              )}`}
              width={100}
              id={`depreciationDate-${data.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Diễn giải' },
      body: {
        render: ({ data }) => (
          <EllipsisParagraph
            text={data?.description}
            width={200}
            id={`description-${data.id}`}
          />
        ),
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) =>
          RequestPaymentStatusBadgeMapping(data?.status as any),
      },
    },
    {
      header: { render: 'Thao tác' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <ButtonEdit
              onClick={goTo(PATH.ALLOCATION_CREATE + '/' + data?.id)}
            />
            <ButtonDropDown
              items={[
                {
                  children: (
                    <ButtonAccept
                      onClick={handleToggleAction({
                        key: 'accept',
                        id: data?.id,
                      })}
                      disabled={
                        !isEnableApprovalOrReject<Allociation>({
                          employeeId,
                          requestApproval: 'requestApprovals',
                          statusKey: 'status',
                          data,
                        })
                      }
                    />
                  ),
                },
                {
                  children: (
                    <ButtonReject
                      onClick={handleToggleAction({
                        key: 'reject',
                        id: data?.id,
                      })}
                      disabled={
                        !isEnableApprovalOrReject<Allociation>({
                          employeeId,
                          requestApproval: 'requestApprovals',
                          statusKey: 'status',
                          data,
                        })
                      }
                    />
                  ),
                },
                {
                  children: (
                    <ButtonV2
                      onClick={handleToggleAction({
                        key: 'request_accept',
                        id: data?.id,
                      })}
                      variant="text"
                      left_section={
                        <img
                          src={iconPath('request-accept.svg')}
                          alt="reqest accept"
                        />
                      }
                      disabled={
                        !isEnableRequestApproval<Allociation>({
                          employeeId,
                          createdBy: 'createdBy',
                          statusKey: 'status',
                          data,
                        })
                      }
                    >
                      Trình Duyệt
                    </ButtonV2>
                  ),
                },
                {
                  children: (
                    <ButtonCancel
                      onClick={handleToggleAction({
                        key: 'cancel',
                        id: data?.id,
                      })}
                      disabled={
                        !isEnableCancel<Allociation>({
                          employeeId,
                          createdBy: 'createdBy',
                          statusKey: 'status',
                          data,
                        })
                      }
                    />
                  ),
                },
              ]}
            >
              <img src={iconPath('more-v2.svg')} alt="more" />
            </ButtonDropDown>
          </Flex>
        ),
      },
    },
  ];

  return (
    <div className="page_container">
      <Flex
        style={{
          borderBottom: '1px solid #E4E7EC',
          paddingBottom: 16,
          marginBottom: 16,
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <Typography level={4} style={{ marginBottom: 0, height: 32 }}>
          Phân bổ công cụ dụng cụ
        </Typography>
          <AuthGuard permissionKey='ASSET_ALLOCATION.CREATE'>
            <ButtonAdd text="Thêm" onClick={goTo(PATH.ALLOCATION_CREATE)} />
          </AuthGuard>
      </Flex>
      <Card
        header={
          <div className="card-header-container">
            <InputSearch
              className="card-header-extra"
              onChange={handleSearch('code.contains')}
            />
            <div className="card-header-extra">
              <ModalWrapper
                renderTarget={({ onToggle }) => (
                  <ButtonFilter className="btn-filter" onClick={onToggle} />
                )}
                renderModal={({ onToggle }) => (
                  <DepreciationFilter handleQuery={handleQuery} query={query} />
                )}
                onCancel={() => {
                  setQuery(pre => {
                    delete pre?.['status.equals']
                    delete pre?.['employeeId.equals']
                    return { ...pre }
                  })
                }}
                okText='Áp dụng'
                cancelText='Hủy'
              />
              <AuthGuard permissionKey='ASSET_ALLOCATION.EXPORT'>
                <DownloadExcelBtn axiosFn={allocationApi.exportExcel} fileName='PhanBoCongCuDungCu' />
              </AuthGuard>
            </div>
          </div>
        }
      >
        <TablePagination<any> 
          table_id='allocation-table'
          columns={columns}  
          data={allocationRes?.data?.data?.data}
          total_pages={allocationRes?.data?.data?.totalRecord}
          itemsPerPage={query?.size}
          handlePageClick={handleQuery('page')}
          handlePageSizeChange={handleQuery('size')}
        />
      </Card>
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
    </div>
  );
}

export default Allocation;
