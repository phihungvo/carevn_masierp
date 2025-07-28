import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const Purchase = Loadable({
  loader: () => import(/* webpackChunkName: "purchase" */ 'app/modules/purchase/purchase'),
  loading: () => loading,
});

const PurchaseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.PURCHASE}>
      <Route index element={<Purchase />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PurchaseRoutes;
