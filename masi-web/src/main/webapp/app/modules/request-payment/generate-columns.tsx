import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import { DATE_FORMAT, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import {
  PAYMENT_REQUEST_STATUS,
  PAYMENT_REQUEST_TYPE,
} from 'app/shared/model/enumerations/payment-request';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IPaymentRequest } from 'app/shared/model/payment-request.model';
import { convertCurrency } from 'app/shared/util/format';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import { useNavigate } from 'react-router';
import { RequestPaymentStatusBadgeMapping } from './request-payment-mapping';
import { useSearchParams } from 'react-router-dom';
import AuthGuard from 'app/components/guards/auth-guard';

const icon_path = 'content/images/vuesax/linear/';

export const generateColumns = (
  data: PaginationResponse<IPaymentRequest>,
  setSelectedRecord: (id: string) => void,
  toggleCancel: () => void,
  toggleApprovalSign: () => void,
  toggleReject: () => void,
  toggleCancelRepayment: () => void,
  toggleApprovalSignRepayment: () => void,
  toggleRejectRepayment: () => void,
  toggleCancelAdvance: () => void,
  toggleApprovalSignAdvance: () => void,
  toggleRejectAdvance: () => void,
): TableColumns<IPaymentRequest> => {
  const navigate = useNavigate();
  const [_searchParams, setSearchParams] = useSearchParams();

  const account = useAppSelector(state => state.authentication.account);

  const handleEdit = (type: string, id: string) => () => {
    const redirect_to =
      {
        [PAYMENT_REQUEST_TYPE.PAYMENT]: PATH.PAYMENT_REQUEST,
        [PAYMENT_REQUEST_TYPE.ADVANCEMENT]: PATH.ADVANCE_REQUEST,
        [PAYMENT_REQUEST_TYPE.REIMBURSEMENT]: PATH.REFUND_REQUEST,
      }[type] +
      '/' +
      id;
      navigate(redirect_to + `?printId=${id}&printType=${type}`);      navigate(redirect_to + `?printId=${id}&printType=${type}`);
  };

  const onActionCancel = (type: string) => {
    switch (type as PAYMENT_REQUEST_TYPE) {
      case PAYMENT_REQUEST_TYPE.PAYMENT:
        toggleCancel();
        break;
      case PAYMENT_REQUEST_TYPE.ADVANCEMENT:
        toggleCancelRepayment();
        break;
      case PAYMENT_REQUEST_TYPE.REIMBURSEMENT:
        toggleCancelAdvance();
        break;
      default:
        break;
    }
  };

  const onActionApprove = (type: string) => {
    switch (type as PAYMENT_REQUEST_TYPE) {
      case PAYMENT_REQUEST_TYPE.PAYMENT:
        toggleApprovalSign();
        break;
      case PAYMENT_REQUEST_TYPE.ADVANCEMENT:
        toggleApprovalSignRepayment();
        break;
      case PAYMENT_REQUEST_TYPE.REIMBURSEMENT:
        toggleApprovalSignAdvance();
        break;
      default:
        break;
    }
  };

  const onActionReject = (type: string) => {
    switch (type as PAYMENT_REQUEST_TYPE) {
      case PAYMENT_REQUEST_TYPE.PAYMENT:
        toggleReject();
        break;
      case PAYMENT_REQUEST_TYPE.ADVANCEMENT:
        toggleRejectRepayment();
        break;
      case PAYMENT_REQUEST_TYPE.REIMBURSEMENT:
        toggleRejectAdvance();
        break;
      default:
        break;
    }
  };

  const disableBtnCancel = (data: IPaymentRequest) =>
    account &&
    data?.status === PAYMENT_REQUEST_STATUS.NEW &&
    data?.createdBy === account.id;

  const disableBtnApprove = (data: IPaymentRequest) =>
    account &&
    data?.status === PAYMENT_REQUEST_STATUS.WAITING_APPROVE &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const disableBtnReject = (data: IPaymentRequest) =>
    account &&
    data?.status === PAYMENT_REQUEST_STATUS.WAITING_APPROVE &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: TableColumns<IPaymentRequest> = useMemo(() => {
    return [
      {
        header: { render: 'Số ĐN' },
        body: {
          render: ({ data }) => (
            <p
              className="attachment-link"
              onClick={handleEdit(data?.type, data?.id)}
            >
              {data?.code}
            </p>
          ),
        },
      },
      {
        header: { render: 'Ngày ĐN' },
        body: {
          render: ({ data }) =>
            dayjs(data?.paymentDate).format(DATE_FORMAT.DATE),
        },
      },
      {
        header: { render: 'Người ĐN' },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={[
                data?.employee?.employeeCode,
                data?.employee?.fullName,
              ].join(' - ')}
              target={`employee-${data?.id}`}
            >
              <EllipsisParagraph
                text={[
                  data?.employee?.employeeCode,
                  data?.employee?.fullName,
                ].join(' - ')}
                width={200}
                id={`employee-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Nội dung' },
        body: {
          render: ({ data }) => (
            <Tooltip label={data?.content} target={`content-${data?.id}`}>
              <EllipsisParagraph
                text={data?.content}
                width={200}
                id={`content-${data?.id}`}
              />
            </Tooltip>
          ),
        },
      },
      {
        header: { render: 'Số tiền' },
        body: {
          render: ({ data }) => {
            const amount =
              data.type === (PAYMENT_REQUEST_TYPE.ADVANCEMENT as string)
                ? data?.totalAmount ?? 0
                : data.type === (PAYMENT_REQUEST_TYPE.REIMBURSEMENT as string)
                  ? data?.paymentVoucherAmount ?? 0
                  : data?.totalAmount ?? 0;
            return (
              <Tooltip
                label={!amount ? '' : `${convertCurrency(amount ?? 0, false)}`}
                target={`totalAmount-${data?.id}`}
              >
                <EllipsisParagraph
                  text={!amount ? '' : `${convertCurrency(amount ?? 0, false)}`}
                  width={200}
                  id={`totalAmount-${data?.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Số tiền đã chi' },
        body: {
          render: ({ data }) => {
            const amount =
              data.type === (PAYMENT_REQUEST_TYPE.ADVANCEMENT as string)
                ? data?.paymentVoucherAmount ?? 0
                : data.paidAmount ?? 0;
            return (
              <Tooltip
                label={!amount ? '' : `${convertCurrency(amount, false)}`}
                target={`paidAmount-${data?.id}`}
              >
                <EllipsisParagraph
                  text={!amount ? '' : convertCurrency(amount, false)}
                  width={200}
                  id={`paidAmount-${data?.id}`}
                />
              </Tooltip>
            );
          },
        },
      },
      {
        header: { render: 'Số tiền còn lại' },
        body: {
          render: ({ data }) => (
            <Tooltip
              label={
                !data?.remainingAmount
                  ? ''
                  : `${convertCurrency(data?.remainingAmount ?? 0, false)}`
              }
              target={`remainingAmount-${data?.id}`}
            >
              <EllipsisParagraph
                text={
                  !data?.remainingAmount
                    ? ''
                    : convertCurrency(data?.remainingAmount ?? 0, false)
                }
                width={200}
                id={`remainingAmount-${data?.id}`}
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
        header: { render: <></> },
        body: {
          render: ({ data }) => (
            <Flex align="center">
              <AuthGuard permissionKey='REQUEST_PAYMENT.EDIT'>
                <Tooltip label={'Cập nhật'} target={`btn-update`} >
                    <ButtonV2
                      id="btn-update"
                      variant="text"
                      isBoxShadow={false}
                      onClick={handleEdit(data?.type, data?.id)}
                    >
                      <img src={icon_path + 'edit-3.svg'} alt="edit" />
                    </ButtonV2>
                </Tooltip>
              </AuthGuard>

              <ButtonDropDown
                items={[
                  {
                    children: (
                        <Tooltip label={'In phiếu'} target={`btn-print`}>
                          <ButtonV2
                            id="btn-print"
                            variant="text"
                            left_section={
                              <img
                                src="content/images/vuesax/linear/printer.svg"
                                alt="print"
                              />
                            }
                          >
                            In phiếu
                          </ButtonV2>
                        </Tooltip>
                    ),
                    onClick: () => {
                      if (!isHasPermission(authorities, 'REQUEST_PAYMENT.EXPORT')) return;
                      setSearchParams({ printId: data?.id, printType: data?.type });
                      setTimeout(() => window.print(), 500);
                    },
                    hidden: !isHasPermission(authorities, 'REQUEST_PAYMENT.EXPORT'),
                  },
                  {
                    children: (
                      <Tooltip label={'Hủy'} target={`btn-cancel`} >
                        <ButtonV2
                          id="btn-cancel"
                          variant="text"
                          left_section={
                            <img
                              src="content/images/vuesax/linear/x-circle.svg"
                              alt="cancel"
                            />
                          }
                          disabled={!disableBtnCancel(data)}
                        >
                          Hủy
                        </ButtonV2>
                      </Tooltip>
                    ),
                    disabled: !disableBtnCancel(data),
                    onClick: () => {
                      setSelectedRecord(data?.id);
                      onActionCancel(data?.type);
                    },
                  },
                  {
                    children: (
                      <Tooltip label={'Duyệt'} target={`btn-approve`} >
                        <ButtonV2
                          id="btn-approve"
                          variant="text"
                          left_section={
                            <img
                              src="content/images/vuesax/linear/check_green.svg"
                              alt="cancel"
                            />
                          }
                          disabled={!disableBtnApprove(data)}
                        >
                          Duyệt
                        </ButtonV2>
                      </Tooltip>
                    ),
                    disable: !disableBtnApprove(data),
                    onClick: () => {
                      setSelectedRecord(data?.id);
                      onActionApprove(data?.type);
                    },
                  },
                  {
                    children: (
                      <Tooltip label={'Từ chối'} target={`btn-reject`} >
                        <ButtonV2
                          id="btn-reject"
                          variant="text"
                          left_section={
                            <img
                              src="content/images/vuesax/linear/x.svg"
                              alt="reject"
                            />
                          }
                          disabled={!disableBtnReject(data)}
                        >
                          Từ chối
                        </ButtonV2>
                      </Tooltip>
                    ),
                    disabled: !disableBtnReject(data),
                    onClick: () => {
                      setSelectedRecord(data?.id);
                      onActionReject(data?.type);
                    },
                  },
                ]}
              >
                <img src={icon_path + 'more-v2.svg'} alt="more" />
              </ButtonDropDown>
            </Flex>
          ),
        },
      },
    ];
  }, [data]);

  return columns;
};
