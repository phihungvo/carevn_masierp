import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Supplies = Loadable({
  loader: () => import(/* webpackChunkName: "" */ 'app/modules/supplies'),
  loading: () => loading,
});

const SuppliesCreate = Loadable({
  loader: () =>
    import(/* webpackChunkName: "" */ 'app/modules/supplies/supplies-create'),
  loading: () => loading,
});

const SuppliesUpdate = Loadable({
  loader: () =>
    import(/* webpackChunkName: "" */ 'app/modules/supplies/supplies-update'),
  loading: () => loading,
});

const SuppliesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.SUPPLIES}>
      <Route index path={PATH.SUPPLIES} element={<Supplies />} />
      <Route path={PATH.SUPPLIES_CREATE} element={<SuppliesCreate />} />
      <Route path={PATH.SUPPLIES_UPDATE} element={<SuppliesUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SuppliesRoutes;
