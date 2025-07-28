import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Factories = Loadable({
  loader: () => import(/* webpackChunkName: "" */ 'app/modules/factories'),
  loading: () => loading,
});

const FactoriesCreate = Loadable({
  loader: () =>
    import(/* webpackChunkName: "" */ 'app/modules/factories/factories-create'),
  loading: () => loading,
});

const FactoriesUpdate = Loadable({
  loader: () =>
    import(/* webpackChunkName: "" */ 'app/modules/factories/factories-update'),
  loading: () => loading,
});

const FactoriesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.FACTORIES}>
      <Route index path={PATH.FACTORIES} element={<Factories />} />
      <Route path={PATH.FACTORIES_CREATE} element={<FactoriesCreate />} />
      <Route path={PATH.FACTORIES_UPDATE} element={<FactoriesUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FactoriesRoutes;
