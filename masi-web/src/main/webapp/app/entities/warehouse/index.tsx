import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';
import WarehouseType from 'app/modules/warehouse-type/warehouse-type';

const loading = <div>loading ...</div>;

const Warehouse = Loadable({
  loader: () => import(/* webpackChunkName: "warehouse" */ 'app/modules/warehouse/warehouse'),
  loading: () => loading,
});

const WarehouseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.WAREHOUSES}>
      <Route index path={PATH.WAREHOUSES} element={<Warehouse />} />
      <Route path={PATH.WAREHOUSE_TYPES} element={<WarehouseType />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WarehouseRoutes;
