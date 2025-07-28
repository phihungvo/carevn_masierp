import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const TimeSheet = Loadable({
  loader: () => import(/* webpackChunkName: "time-sheet" */ 'app/modules/time-sheet/time-sheet'),
  loading: () => loading,
});

const TimeSheetExplanation = Loadable({
  loader: () => import(/* webpackChunkName: "time-sheet-explanation" */ 'app/modules/time-sheet-explanation/time-sheet-explanation'),
  loading: () => loading,
});

const TimeSheetBulkApproval = Loadable({
  loader: () => import(/* webpackChunkName: "time-sheet-bulk-approval" */ 'app/modules/time-sheet-bulk-approval/time-sheet-bulk-approval'),
  loading: () => loading,
});

const TimeSheetViolation = Loadable({
  loader: () => import(/* webpackChunkName: "time-sheet-violation" */ 'app/modules/time-sheet-violation/time-sheet-violation'),
  loading: () => loading,
});

const TimeSheetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.TIME_SHEET}>
      <Route index element={<TimeSheet />} />
      <Route path={PATH.TIME_SHEET_BULK} element={<TimeSheet />} />
      <Route path={PATH.TIME_SHEET_EXPLANATION} element={<TimeSheetExplanation />} />
      <Route path={PATH.TIME_SHEET_BULK_APPROVAL} element={<TimeSheetBulkApproval />} />
      <Route path={PATH.TIME_SHEET_VIOLATION} element={<TimeSheetViolation />} />
      <Route path={PATH.TIME_SHEET_EXPLANATION_VIOLATION} element={<TimeSheetViolation />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimeSheetRoutes;
