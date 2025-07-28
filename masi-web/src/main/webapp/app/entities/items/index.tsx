import Loadable from 'react-loadable';
import React from 'react';
import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Items = Loadable({
  loader: () => import(/* webpackChunkName: "items-settings" */ 'app/modules/items/item'),
  loading: () => loading,
});

const ItemsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.ITEMS}>
      <Route index path={PATH.ITEMS} element={<Items />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ItemsRoutes;
