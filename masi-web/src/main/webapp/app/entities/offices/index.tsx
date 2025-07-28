import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const Offices = Loadable({
  loader: () => import(/* webpackChunkName: "offices" */ 'app/modules/offices/offices'),
  loading: () => loading,
});

const OfficesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.OFFICES}>
      <Route index element={<Offices />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OfficesRoutes;
