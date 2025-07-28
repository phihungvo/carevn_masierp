import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import usePaymentRequest from 'app/hooks/use-payment-request';
import { PAYMENT_REQUEST_TYPE } from 'app/shared/model/enumerations/payment-request';
import {
  IPaymentRequest,
  IPaymentRequestParams,
} from 'app/shared/model/payment-request.model';
import { convertCurrency } from 'app/shared/util/format';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import AdvanceRequestApproveSignModals from './advance-request/advance-request-approve-sign-modals';
import AdvanceRequestApproveSignSuccessModals from './advance-request/advance-request-approve-sign-success-modals';
import AdvanceRequestCancelModals from './advance-request/advance-request-cancel-modals';
import AdvanceRequestCancelSuccessModals from './advance-request/advance-request-cancel-success-modals';
import AdvanceRequestRejectModals from './advance-request/advance-request-reject-modals';
import AdvanceRequestRejectSuccessModals from './advance-request/advance-request-reject-success-modals';
import { generateColumns } from './generate-columns';
import RequestPaymentApproveSignModals from './payment-request/request-payment-approve-sign-modals';
import RequestPaymentApproveSignSuccessModals from './payment-request/request-payment-approve-sign-success-modals';
import RequestPaymentCancelModals from './payment-request/request-payment-cancel-modals';
import RequestPaymentCancelSuccessModals from './payment-request/request-payment-cancel-success-modals';
import RequestPaymentRejectModals from './payment-request/request-payment-reject-modals';
import RequestPaymentRejectSuccessModals from './payment-request/request-payment-reject-success-modals';
import RefundRequestApproveSignModals from './refund-request/refund-request-approve-sign-modals';
import RefundRequestApproveSignSuccessModals from './refund-request/refund-request-approve-sign-success-modals';
import RefundRequestCancelModals from './refund-request/refund-request-cancel-modals';
import RefundRequestCancelSuccessModals from './refund-request/refund-request-cancel-success-modals';
import RefundRequestRejectModals from './refund-request/refund-request-reject-modals';
import RefundRequestRejectSuccessModals from './refund-request/refund-request-reject-success-modals';
import { useModalsRequestPayment } from 'app/hooks/use-request-payment';
import { useModalsAdvanceRequest } from 'app/hooks/use-advance-request';
import { useModalsRefundRequest } from 'app/hooks/use-refund-request';

const icon_path = 'content/images/vuesax/linear/';

const { useGetPaymentRequests } = usePaymentRequest;

interface IRequestPaymentTableProps {
  searchText: string;
  filter: IPaymentRequestParams;
  setFilter: React.Dispatch<React.SetStateAction<IPaymentRequestParams>>;
}

function RequestPaymentTable({
  searchText,
  filter,
  setFilter,
}: IRequestPaymentTableProps) {
  const search = useDebounce(searchText, 500);
  const navigate = useNavigate();

  const { data: paymentRequests } = useGetPaymentRequests(filter);

  const [selectedRecord, setSelectedRecord] = useState<string>('');

  const [
    { openApprovalSign, toggleApprovalSign },
    { openApprovalSignSuccess, toggleApprovalSignSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
  ] = useModalsRequestPayment();

  const [
    {
      openApprovalSign: openApprovalSignRepayment,
      toggleApprovalSign: toggleApprovalSignRepayment,
    },
    {
      openApprovalSignSuccess: openApprovalSignSuccessRepayment,
      toggleApprovalSignSuccess: toggleApprovalSignSuccessRepayment,
    },
    { openCancel: openCancelRepayment, toggleCancel: toggleCancelRepayment },
    {
      openCancelSuccess: openCancelSuccessRepayment,
      toggleCancelSuccess: toggleCancelSuccessRepayment,
    },
    { openReject: openRejectRepayment, toggleReject: toggleRejectRepayment },
    {
      openRejectSuccess: openRejectSuccessRepayment,
      toggleRejectSuccess: toggleRejectSuccessRepayment,
    },
  ] = useModalsAdvanceRequest();

  const [
    {
      openApprovalSign: openApprovalSignAdvance,
      toggleApprovalSign: toggleApprovalSignAdvance,
    },
    {
      openApprovalSignSuccess: openApprovalSignSuccessAdvance,
      toggleApprovalSignSuccess: toggleApprovalSignSuccessAdvance,
    },
    { openReject: openRejectAdvance, toggleReject: toggleRejectAdvance },
    {
      openRejectSuccess: openRejectSuccessAdvance,
      toggleRejectSuccess: toggleRejectSuccessAdvance,
    },
    { openCancel: openCancelAdvance, toggleCancel: toggleCancelAdvance },
    {
      openCancelSuccess: openCancelSuccessAdvance,
      toggleCancelSuccess: toggleCancelSuccessAdvance,
    },
  ] = useModalsRefundRequest();

  const columns = generateColumns(
    paymentRequests,
    setSelectedRecord,
    toggleCancel,
    toggleApprovalSign,
    toggleReject,
    toggleCancelRepayment,
    toggleApprovalSignRepayment,
    toggleRejectRepayment,
    toggleCancelAdvance,
    toggleApprovalSignAdvance,
    toggleRejectAdvance,
  );

  useEffect(() => {
    setFilter(prev => ({ ...prev, page: DEFAULT_PAGE, search }));
  }, [search]);

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page, search });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize, search });
  };

  const calcTotalAmount = () => {
    return paymentRequests?.data.reduce(
      (acc, obj) => acc + (Number(obj?.totalAmount ?? 0) ?? 0),
      0,
    );
  };

  const calcPaidAmount = () => {
    return paymentRequests?.data.reduce((acc, obj) => {
      const amount =
        obj?.type === (PAYMENT_REQUEST_TYPE.ADVANCEMENT as string)
          ? obj.paymentVoucherAmount ?? 0
          : obj.paidAmount ?? 0;
      return acc + (Number(amount) ?? 0);
    }, 0);
  };

  const calcRemainAmount = () => {
    return paymentRequests?.data.reduce(
      (acc, obj) => acc + (Number(obj?.remainingAmount ?? 0) ?? 0),
      0,
    );
  };

  return (
    <>
      <TablePagination<IPaymentRequest>
        table_id="request-payment"
        columns={columns}
        data={paymentRequests?.data || []}
        total_pages={paymentRequests?.totalRecord}
        itemsPerPage={filter.size}
        handlePageClick={handlePageChange}
        handlePageSizeChange={handlePageSizeChange}
        isStickyLastRow
        custom_body_row={() => (
          <tr className="table-footer">
            <td colSpan={3} />
            <td>Tổng tiền</td>
            <td>{convertCurrency(calcTotalAmount() ?? 0, false)}</td>
            <td>{convertCurrency(calcPaidAmount() ?? 0, false)}</td>
            <td>{convertCurrency(calcRemainAmount() ?? 0, false)}</td>
            <td colSpan={2} />
          </tr>
        )}
      />

      {/* RequestPayment DNTT */}
      <RequestPaymentCancelModals
        isOpen={openCancel}
        toggle={toggleCancel}
        toggleSuccess={toggleCancelSuccess}
        selectedRecord={selectedRecord}
      />

      <RequestPaymentCancelSuccessModals
        isOpen={openCancelSuccess}
        toggle={() => {
          toggleCancelSuccess();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      <RequestPaymentApproveSignModals
        isOpen={openApprovalSign}
        toggle={toggleApprovalSign}
        toggleSuccess={toggleApprovalSignSuccess}
        selectedRecord={selectedRecord}
      />

      <RequestPaymentApproveSignSuccessModals
        isOpen={openApprovalSignSuccess}
        toggle={() => {
          toggleApprovalSignSuccess();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      <RequestPaymentRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleRejectSuccess}
        selectedRecord={selectedRecord}
      />

      <RequestPaymentRejectSuccessModals
        isOpen={openRejectSuccess}
        toggle={() => {
          toggleRejectSuccess();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      {/* AdvanceRePayment DNTU */}
      <AdvanceRequestCancelModals
        isOpen={openCancelRepayment}
        toggle={toggleCancelRepayment}
        toggleSuccess={toggleCancelSuccessRepayment}
        selectedRecord={selectedRecord}
      />

      <AdvanceRequestCancelSuccessModals
        isOpen={openCancelSuccessRepayment}
        toggle={() => {
          toggleCancelSuccessRepayment();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      <AdvanceRequestApproveSignModals
        isOpen={openApprovalSignRepayment}
        toggle={toggleApprovalSignRepayment}
        toggleSuccess={toggleApprovalSignSuccessRepayment}
        selectedRecord={selectedRecord}
      />

      <AdvanceRequestApproveSignSuccessModals
        isOpen={openApprovalSignSuccessRepayment}
        toggle={() => {
          toggleApprovalSignSuccessRepayment();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      <AdvanceRequestRejectModals
        isOpen={openRejectRepayment}
        toggle={toggleRejectRepayment}
        toggleSuccess={toggleRejectSuccessRepayment}
        selectedRecord={selectedRecord}
      />

      <AdvanceRequestRejectSuccessModals
        isOpen={openRejectSuccessRepayment}
        toggle={() => {
          toggleRejectSuccessRepayment();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      {/* RequestOFAdvance HTU */}
      <RefundRequestCancelModals
        isOpen={openCancelAdvance}
        toggle={toggleCancelAdvance}
        toggleSuccess={toggleCancelSuccessAdvance}
        selectedRecord={selectedRecord}
      />

      <RefundRequestCancelSuccessModals
        isOpen={openCancelSuccessAdvance}
        toggle={() => {
          toggleCancelSuccessAdvance();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      <RefundRequestApproveSignModals
        isOpen={openApprovalSignAdvance}
        toggle={toggleApprovalSignAdvance}
        toggleSuccess={toggleApprovalSignSuccessAdvance}
        selectedRecord={selectedRecord}
      />

      <RefundRequestApproveSignSuccessModals
        isOpen={openApprovalSignSuccessAdvance}
        toggle={() => {
          toggleApprovalSignSuccessAdvance();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />

      <RefundRequestRejectModals
        isOpen={openRejectAdvance}
        toggle={toggleRejectAdvance}
        toggleSuccess={toggleRejectSuccessAdvance}
        selectedRecord={selectedRecord}
      />

      <RefundRequestRejectSuccessModals
        isOpen={openRejectSuccessAdvance}
        toggle={() => {
          toggleRejectSuccessAdvance();
          navigate(PATH.REQUEST_PAYMENT);
        }}
      />
    </>
  );
}

export default RequestPaymentTable;
