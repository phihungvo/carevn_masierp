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
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { iconPath } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { RequestPaymentStatusBadgeMapping } from '../request-payment/request-payment-mapping';
import { liquidationApi } from './apis/axios';
import { useLiquidationList } from './apis/hook';
import Accept from './component/actions/Accept';
import Cancel from './component/actions/Cancel';
import Reject from './component/actions/Reject';
import RequestAccept from './component/actions/RequestAccept';
import LiquidationFilter from './liquidation-filter';
import './liquidation.scss';
import { Liquidation } from './types/list';
import { statusData } from './constants/status';
import { isEnableApprovalOrReject, isEnableCancel, isEnableRequestApproval } from 'app/shared/util/logicActions';
import useAccountApp from 'app/hooks/use-account-app';
import { Link } from 'react-router-dom';

function Liquidation() {
  const { employeeId } = useAccountApp()
  const { goTo } = useGoTo()

  const {
    query,
    liquidationRes,
    handleQuery,
    handleSearch
  } = useLiquidationList()
  const { actions, handleToggleAction } = useAction()

  const columns: TableColumns<Liquidation> = [
    {
      header: {
        render: 'Số tham chiếu',
      },
      body: {
        render: ({ data }) => (
          <Link to={PATH.LIQUIDATION_CREATE + '/' + data?.id}>
            <EllipsisParagraph
              text={data?.['code']}
              width={150}
              id={`referenceNumber-${data.id}`}
            />
          </Link>
        ),
      },
    },
    {
      header: {
        render: 'Ngày thanh lý',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={data?.liquidationDate}
            target={`liquidationDate-${data.id}`}
          >
            <EllipsisParagraph
              text={`${dayjs(data.liquidationDate).format(
                DATE_FORMAT.DATE,
              )}`}
              width={100}
              id={`liquidationDate-${data.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Lý do thanh lý',
      },
      body: {
        render: ({ data }) => (
          <Tooltip
            label={statusData.obj?.[data?.reason]?.label}
            target={`liquidationReason-${data.id}`}
          >
            <EllipsisParagraph
              text={statusData.obj?.[data?.reason]?.label}
              width={100}
              id={`liquidationReason-${data.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Diễn giải',
      },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.description} target={`description-${data?.id}`}>
            <EllipsisParagraph
              text={data?.description}
              width={100}
              id={`description-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: {
        render: 'Trạng thái',
      },
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
              onClick={goTo(PATH.LIQUIDATION_CREATE + '/' + data?.id)}
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
                        !isEnableApprovalOrReject<Liquidation>({
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
                        !isEnableApprovalOrReject<Liquidation>({
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
                        !isEnableRequestApproval<Liquidation>({
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
                        !isEnableCancel<Liquidation>({
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
          Thanh lý tài sản
        </Typography>
        <AuthGuard permissionKey="ASSET_LIQUIDATION.CREATE">
          <ButtonAdd text="Thêm" onClick={goTo(PATH.LIQUIDATION_CREATE)} />
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
                  <ButtonFilter onClick={onToggle} />
                )}
                renderModal={() => (
                  <LiquidationFilter handleQuery={handleQuery} />
                )}
                okText="Áp dụng"
                cancelText="Đặt lại"
              />
              <AuthGuard permissionKey="ASSET_LIQUIDATION.EXPORT">
                <DownloadExcelBtn
                  axiosFn={liquidationApi.export}
                  fileName="ThanhLyTaiSan"
                />
              </AuthGuard>
            </div>
          </div>
        }
      >
        <TablePagination<Liquidation>
          table_id="liquidation"
          columns={columns}
          data={liquidationRes?.data?.data?.data}
          total_pages={liquidationRes?.data?.data?.totalRecord}
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

export default Liquidation;
