import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const Orders = Loadable({
  loader: () => import(/* webpackChunkName: "orders" */ 'app/modules/orders/orders'),
  loading: () => loading,
});

const OrdersRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.ORDERS}>
      <Route index element={<Orders />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrdersRoutes;
