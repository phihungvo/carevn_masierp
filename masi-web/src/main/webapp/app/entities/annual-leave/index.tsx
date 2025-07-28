import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const AnnualLeave = Loadable({
  loader: () => import(/* webpackChunkName: "annual-leave" */ 'app/modules/annual-leave/annual-leave'),
  loading: () => loading,
});

const AnnualLeaveRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.ANNUAL_LEAVE}>
      <Route index element={<AnnualLeave />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AnnualLeaveRoutes;
