import Loadable from 'react-loadable';
import React from 'react';
import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Documentary = Loadable({
  loader: () => import(/* webpackChunkName: "documentary" */ 'app/modules/documentary/documentary'),
  loading: () => loading,
});

const DocumentaryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.DOCUMENTARY}>
      <Route index path="" element={<Documentary />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocumentaryRoutes;
