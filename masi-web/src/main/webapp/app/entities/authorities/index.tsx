import React from 'react';
import Loadable from 'react-loadable';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const Authorities = Loadable({
  loader: () => import(/* webpackChunkName: "authorities" */ 'app/modules/authorities/authorities'),
  loading: () => loading,
});

const AuthoritiesGroup = Loadable({
  loader: () => import(/* webpackChunkName: "authorities-group" */ 'app/modules/authorities-group/authorities-group'),
  loading: () => loading,
});

const AuthoritiesUsers = Loadable({
  loader: () => import(/* webpackChunkName: "authorities-group-detail" */ 'app/modules/authorities-users/authorities-users'),
  loading: () => loading,
});

const AuthoritiesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.AUTHORITIES}>
      <Route index path={PATH.AUTHORITIES} element={<Authorities />} />
      <Route path={PATH.AUTHORITIES_GROUPS} element={<AuthoritiesGroup />} />
      <Route path={PATH.AUTHORITIES_USERS} element={<AuthoritiesUsers />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AuthoritiesRoutes;
