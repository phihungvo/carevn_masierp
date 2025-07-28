import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const Recruitment = Loadable({
  loader: () => import(/* webpackChunkName: "recruitment" */ 'app/modules/recruitment/recruitment'),
  loading: () => loading,
});

const RecruitmentCandidates = Loadable({
  loader: () => import(/* webpackChunkName: "recruitment" */ 'app/modules/recruitment/recruitment-candidates'),
  loading: () => loading,
});

const RecruitmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.RECRUITMENT}>
      <Route index element={<Recruitment />} />
      <Route path={PATH.RECRUITMENT_CANDIDATES} element={<RecruitmentCandidates />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RecruitmentRoutes;
