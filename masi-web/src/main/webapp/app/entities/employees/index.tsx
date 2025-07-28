import Loadable from 'react-loadable';
import React from 'react';
import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Employees = Loadable({
  loader: () => import(/* webpackChunkName: "employees" */ 'app/modules/employees/employees'),
  loading: () => loading,
});

const EmployeesCreate = Loadable({
  loader: () => import(/* webpackChunkName: "employees-create" */ 'app/modules/employees/components/employees-create'),
  loading: () => loading,
});

const EmployeesUpdate = Loadable({
  loader: () => import(/* webpackChunkName: "employees-update" */ 'app/modules/employees/components/employees-update'),
  loading: () => loading,
});

const EmployeesChangeLogs = Loadable({
  loader: () => import(/* webpackChunkName: "employees-changelogs" */ 'app/modules/employees/employees-change-logs'),
  loading: () => loading,
});

const EmployeesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.EMPLOYEES}>
      <Route index path="" element={<Employees />} />
      <Route path={PATH.EMPLOYEES_CREATE} element={<EmployeesCreate />} />
      <Route path={PATH.EMPLOYEES_UPDATE} element={<EmployeesUpdate />} />
      <Route path={PATH.EMPLOYEES_CHANGE_LOGS} element={<EmployeesChangeLogs />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmployeesRoutes;
