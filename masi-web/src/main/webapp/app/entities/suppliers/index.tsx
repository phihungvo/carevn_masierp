import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Suppliers = Loadable({
  loader: () => import(/* webpackChunkName: "" */ 'app/modules/suppliers/suppliers'),
  loading: () => loading,
});

const SupplierRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.SUPPLIERS}>
      <Route index path={PATH.SUPPLIERS} element={<Suppliers />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SupplierRoutes;
