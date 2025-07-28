import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Loadable from 'react-loadable';
import { Route } from 'react-router';
const loading = <div>loading ...</div>;

const DeliverySchedule = Loadable({
  loader: () => import('app/modules/delivery-schedule/DeliverySchedule'),
  loading: () => loading,
});

const IncomingInvoicesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.DELIVERY_SCHEDULE}>
      <Route index element={<DeliverySchedule />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IncomingInvoicesRoutes;
