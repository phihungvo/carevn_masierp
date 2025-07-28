import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Customers = Loadable({
  loader: () => import(/* webpackChunkName: "customers" */ 'app/modules/customers/customers'),
  loading: () => loading,
});

const CustomersRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.DELIVERY_HISTORY }>
      <Route index element={<Customers />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CustomersRoutes;
