import { Divider } from 'antd';
import ButtonAccept from 'app/components/ButtonV2/ButtonAccept';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonCancel from 'app/components/ButtonV2/ButtonCancel';
import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import ButtonEdit from 'app/components/ButtonV2/ButtonEdit';
import ButtonFilter from 'app/components/ButtonV2/ButtonFilter';
import ButtonReject from 'app/components/ButtonV2/ButtonReject';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import DownloadExcelBtn from 'app/components/ButtonV2/DownloadExcelBtn';
import ModalWrapper from 'app/components/ButtonV2/ModalWrapper';
import Card from 'app/components/card/card';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useGoTo from 'app/hooks/use-go-to';
import useAction from 'app/hooks/user-action';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { iconPath } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { Link } from 'react-router-dom';
import { RequestPaymentStatusBadgeMapping } from '../request-payment/request-payment-mapping';
import { DepreciationApi } from './apis/axios';
import { useDepriciationList } from './apis/hook';
import Accept from './component/actions/Accept';
import Cancel from './component/actions/Cancel';
import Reject from './component/actions/Reject';
import RequestAccept from './component/actions/RequestAccept';
import './depreciation.scss';
import DepreciationFilter from './modals/depreciation-filter';
import { Depreciation } from './types/list';
import { isEnableApprovalOrReject, isEnableCancel, isEnableRequestApproval } from 'app/shared/util/logicActions';
import useAccountApp from 'app/hooks/use-account-app';

function Depreciation() {

    const { employeeId } = useAccountApp()
    const { goTo } = useGoTo()
    
    const {
        query,
        handleQuery,
        handleSearch,
        depriciationApi,
        setQuery,
        employeesObj
    } = useDepriciationList()
    const { actions, handleToggleAction } = useAction()

    const columns: TableColumns<Depreciation> = [
      {
        header: { render: 'Kỳ khấu hao' },
        body: {
          render: ({ data }) => (
            <Link to={PATH.DEPRECIATION_CREATE + '/' + data?.id}>
              <p className="attachment-link">
                <EllipsisParagraph
                  text={data?.code}
                  width={150}
                  id={`depreciationPeriod-${data?.id}`}
                />
              </p>
            </Link>
          ),
        },
      },
      {
        header: { render: 'Ngày tính KH' },
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
        header: { render: 'Người tính' },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={employeesObj?.[data?.employeeId]?.fullName}
              target={`calculatedBy-${data.id}`}
            >
              <EllipsisParagraph
                text={employeesObj?.[data?.employeeId]?.fullName}
                width={100}
                id={`calculatedBy-${data.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Diễn giải' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.['description']} target={`description-${data.id}`}>
              <EllipsisParagraph
                text={data?.['description']}
                width={300}
                id={`description-${data.id}`}
              />
            </Tooltip>
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
                onClick={goTo(PATH.DEPRECIATION_CREATE + '/' + data?.id)}
              />
              <ButtonDropDown
                items={[
                  {
                    children: (
                      <ButtonAccept
                        onClick={
                          handleToggleAction({ key: 'accept', id: data?.id })
                        }
                        disabled={!isEnableApprovalOrReject<Depreciation>({
                          employeeId,
                          requestApproval: 'requestApprovals',
                          statusKey: 'status',
                          data
                        })}
                      />
                    ),
                  },
                  {
                    children: (
                      <ButtonReject
                        onClick={
                          handleToggleAction({ key: 'reject', id: data?.id })
                        }
                        disabled={!isEnableApprovalOrReject<Depreciation>({
                          employeeId,
                          requestApproval: 'requestApprovals',
                          statusKey: 'status',
                          data
                        })}
                      />
                    ),
                  },
                  {
                    children: (
                      <ButtonV2
                        onClick={handleToggleAction({ key: 'request_accept', id: data?.id })}
                        variant='text'
                        left_section={<img src={iconPath('request-accept.svg')} alt="reqest accept" />}
                        disabled={!isEnableRequestApproval<Depreciation>({
                          employeeId,
                          createdBy: 'createdBy',
                          statusKey: 'status',
                          data
                        })}
                      >
                        Trình Duyệt
                      </ButtonV2>
                    ),
                  },
                  {
                    children: (
                      <ButtonCancel
                        onClick={
                          handleToggleAction({ key: 'cancel', id: data?.id })
                        }
                        disabled={!isEnableCancel<Depreciation>({
                          employeeId,
                          createdBy: 'createdBy',
                          statusKey: 'status',
                          data,
                        })}
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
      <div className="page_container page_container_depreciation">
        <Card
          header={
            <Flex className="card-header-container" direction="column">
              <Flex justify="space-between" align="center">
                <Typography level={4}>Khấu hao tài sản</Typography>
                  <AuthGuard permissionKey='ASSET_DEPRECIATION.CREATE'>
                    <ButtonAdd
                      text="Thêm"
                      onClick={goTo(PATH.DEPRECIATION_CREATE)}
                    />
                  </AuthGuard>
              </Flex>
              <Divider
                style={{
                  border: '1px solid #E4E7EC',
                  marginTop: 20,
                  marginBottom: 16,
                }}
              />
              <Flex justify="space-between">
                <InputSearch
                  className="card-header-extra"
                  onChange={handleSearch('code.contains')}
                />
                <div className="card-header-extra">
                  <ModalWrapper
                    renderTarget={({ onToggle }) => (
                      <ButtonFilter onClick={onToggle} />
                    )}
                    renderModal={() => (
                      <DepreciationFilter
                        handleQuery={handleQuery}
                        query={query}
                      />
                    )}
                    onCancel={() => {
                      setQuery(pre => {
                        delete pre?.['status.equals'];
                        delete pre?.['employeeId.equals'];
                        return { ...pre };
                      });
                    }}
                    okText='Áp dụng'
                    cancelText='Đặt lại'
                  />
                  <AuthGuard permissionKey='ASSET_DEPRECIATION.EXPORT'>
                    <DownloadExcelBtn
                      axiosFn={DepreciationApi.export}
                      fileName="KhauHaoTaiSan"
                    />
                  </AuthGuard>
                </div>
              </Flex>
            </Flex>
          }
        >
          <TablePagination<Depreciation>
            table_id={FORM.DEPRECIATION}
            columns={columns}
            data={depriciationApi?.data?.data?.data}
            total_pages={depriciationApi?.data?.data?.totalRecord}
            itemsPerPage={query?.size}
            handlePageClick={handleQuery('page')}
            handlePageSizeChange={handleQuery('size')}
            isLoading={depriciationApi?.isLoading}
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

export default Depreciation