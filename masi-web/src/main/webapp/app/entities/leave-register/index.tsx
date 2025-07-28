import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const LeaveRegister = Loadable({
  loader: () => import(/* webpackChunkName: "leave-register" */ 'app/modules/leave-register/leave-register'),
  loading: () => loading,
});

const LeaveRegisterRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.LEAVE_REGISTER}>
      <Route index element={<LeaveRegister />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LeaveRegisterRoutes;
