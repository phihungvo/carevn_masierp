import Loadable from 'react-loadable';
import { Route } from 'react-router';

import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

const loading = <div>loading ...</div>;

const RequestPayment = Loadable({
  loader: () => import('app/modules/request-payment/request-payment'),
  loading: () => loading,
});

const RequestPaymentCreateUpdate = Loadable({
  loader: () =>
    import(
      'app/modules/request-payment/payment-request/request-payment-create-modals'
    ),
  loading: () => loading,
});

const AdvanceRequestCreateUpdate = Loadable({
  loader: () =>
    import(
      'app/modules/request-payment/advance-request/advance-request-create-modals'
    ),
  loading: () => loading,
});

const RefundRequestCreateUpdate = Loadable({
  loader: () =>
    import(
      'app/modules/request-payment/refund-request/refund-request-create-modal'
    ),
  loading: () => loading,
});

const RequestPaymentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.REQUEST}>
      <Route path={PATH.REQUEST_PAYMENT} element={<RequestPayment />} />
      <Route path={PATH.PAYMENT_REQUEST} element={<RequestPaymentCreateUpdate />} />
      <Route path={PATH.PAYMENT_REQUEST_DETAIL} element={<RequestPaymentCreateUpdate />} />
      <Route path={PATH.ADVANCE_REQUEST} element={<AdvanceRequestCreateUpdate />} />
      <Route path={PATH.ADVANCE_REQUEST_DETAIL} element={<AdvanceRequestCreateUpdate />} />
      <Route path={PATH.REFUND_REQUEST} element={<RefundRequestCreateUpdate />} />
      <Route path={PATH.REFUND_REQUEST_DETAIL} element={<RefundRequestCreateUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RequestPaymentRoutes;
