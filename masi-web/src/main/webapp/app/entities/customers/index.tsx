import Loadable from 'react-loadable';
import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Customers = Loadable({
  loader: () => import(/* webpackChunkName: "customers" */ 'app/modules/customers/customers'),
  loading: () => loading,
});

const CustomersDisposed = Loadable({
  loader: () => import(/* webpackChunkName: "customers-disposed" */ 'app/modules/customers-disposed/customers-disposed'),
  loading: () => loading,
});

const CustomersTransfer = Loadable({
  loader: () => import(/* webpackChunkName: "customers-transfer" */ 'app/modules/customers-transfer/customers-transfer'),
  loading: () => loading,
});

const CustomersRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.CUSTOMERS}>
      <Route index element={<Customers />} />
      <Route path={PATH.CUSTOMERS_DISPOSED} element={<CustomersDisposed />} />
      <Route path={PATH.CUSTOMERS_TRANSFER} element={<CustomersTransfer />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CustomersRoutes;
