import Loadable from 'react-loadable';
import React from 'react';
import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Uniform = Loadable({
  loader: () => import(/* webpackChunkName: "uniform" */ 'app/modules/uniform/uniform'),
  loading: () => loading,
});

const UniformOrders = Loadable({
  loader: () => import(/* webpackChunkName: "uniform-orders" */ 'app/modules/uniform-orders/uniform-orders'),
  loading: () => loading,
});

const UniformExports = Loadable({
  loader: () => import(/* webpackChunkName: "uniform-exports" */ 'app/modules/uniform-exports/uniform-exports'),
  loading: () => loading,
});

const UniformSettings = Loadable({
  loader: () => import(/* webpackChunkName: "uniform-settings" */ 'app/modules/uniform-settings/uniform-settings'),
  loading: () => loading,
});

const UniformRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.UNIFORM}>
      <Route index path={PATH.UNIFORM_ORDERS} element={<UniformOrders />} />
      <Route path="" element={<Uniform />} />
      <Route path={PATH.UNIFORM_EXPORTS} element={<UniformExports />} />
      <Route path={PATH.UNIFORM_SETTINGS} element={<UniformSettings />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UniformRoutes;
