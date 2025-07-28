import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const Contracts = Loadable({
  loader: () => import(/* webpackChunkName: "contracts" */ 'app/modules/contracts/contracts'),
  loading: () => loading,
});

const ContractsDeleted = Loadable({
  loader: () => import(/* webpackChunkName: "contracts-deleted" */ 'app/modules/contracts-deleted/contracts-deleted'),
  loading: () => loading,
});

const ContractsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.CONTRACTS}>
      <Route index element={<Contracts />} />
      <Route path={PATH.CONTRACTS_DELETED} element={<ContractsDeleted />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContractsRoutes;
