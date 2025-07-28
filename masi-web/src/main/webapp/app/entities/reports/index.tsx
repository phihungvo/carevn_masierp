import Loadable from 'react-loadable';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const ReportUniformsExpired = Loadable({
  loader: () => import(/* webpackChunkName: "report-uniforms-expired" */ 'app/modules/report-uniforms-expired/report-uniforms-expired'),
  loading: () => loading,
});

const ReportUniformsExports = Loadable({
  loader: () => import(/* webpackChunkName: "report-uniforms-exports" */ 'app/modules/report-uniforms-exports/report-uniforms-exports'),
  loading: () => loading,
});

const ReportUniformsReimbursement = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "report-uniforms-reimbursement" */ 'app/modules/report-uniforms-reimbursement/report-uniforms-reimbursement'
    ),
  loading: () => loading,
});

const ReportEmployeeExpiringContract = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "report-employee-expiring-contract" */ 'app/modules/report-employee-expiring-contract/report-employee-expiring-contract'
    ),
  loading: () => loading,
});

const ReportLeaveRegime = Loadable({
  loader: () => import(/* webpackChunkName: "report-leave-regime" */ 'app/modules/report-leave-regime/report-leave-regime'),
  loading: () => loading,
});

const ReportRecruitment = Loadable({
  loader: () => import(/* webpackChunkName: "report-recruitment" */ 'app/modules/report-recruitment/report-recruitment'),
  loading: () => loading,
});

const ReportHrChange = Loadable({
  loader: () => import(/* webpackChunkName: "report-hr-change" */ 'app/modules/report-hr-change/report-hr-change'),
  loading: () => loading,
});

const ReportRoutes = () => {
  return (
    <ErrorBoundaryRoutes>
      <Route path={PATH.REPORT}>
        <Route index path={PATH.REPORT_UNIFORMS_EXPIRED} element={<ReportUniformsExpired />} />
        <Route path={PATH.REPORT_UNIFORMS_EXPORTS} element={<ReportUniformsExports />} />
        <Route path={PATH.REPORT_UNIFORMS_REIMBURSEMENT} element={<ReportUniformsReimbursement />} />
        <Route path={PATH.REPORT_EMPLOYEE_EXPIRING_CONTRACT} element={<ReportEmployeeExpiringContract />} />
        <Route path={PATH.REPORT_LEAVE_REGIME} element={<ReportLeaveRegime />} />
        <Route path={PATH.REPORT_RECRUITMENT} element={<ReportRecruitment />} />
        <Route path={PATH.REPORT_HUMAN_RESOURCE_CHANGE} element={<ReportHrChange />} />
      </Route>
    </ErrorBoundaryRoutes>
  );
};

export default ReportRoutes;
