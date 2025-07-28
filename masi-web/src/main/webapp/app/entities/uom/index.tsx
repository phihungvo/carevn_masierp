import Loadable from 'react-loadable';
import React from 'react';
import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Uom = Loadable({
  loader: () => import(/* webpackChunkName: "uom" */ 'app/modules/uom/uom'),
  loading: () => loading,
});

const UomGroup = Loadable({
  loader: () => import(/* webpackChunkName: "uom-group" */ 'app/modules/uom-group/uom-group'),
  loading: () => loading,
});

const UomRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.UOM}>
      <Route index path={PATH.UOM} element={<Uom />} />
      <Route path={PATH.UOM_GROUP} element={<UomGroup />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UomRoutes;
