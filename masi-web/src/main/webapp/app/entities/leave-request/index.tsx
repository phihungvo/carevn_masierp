import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const LeaveRequest = Loadable({
  loader: () => import(/* webpackChunkName: "leave-request" */ 'app/modules/leave-request/leave-request'),
  loading: () => loading,
});

const LeaveRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.LEAVE_REQUEST}>
      <Route index element={<LeaveRequest />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LeaveRequestRoutes;
