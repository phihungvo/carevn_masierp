import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const MachineryEquipment = Loadable({
  loader: () => import(/* webpackChunkName: "" */ 'app/modules/machinery-equipment'),
  loading: () => loading,
});

const MachineryEquipmentUpdate = Loadable({
  loader: () => import(/* webpackChunkName: "" */ 'app/modules/machinery-equipment/machinery-equipment-update'),
  loading: () => loading,
});

const TechnicalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.TECHNICAL}>
    <Route path={PATH.MACHINERY_EQUIPMENT}>
        <Route index path={PATH.MACHINERY_EQUIPMENT} element={<MachineryEquipment />} />
        <Route path={PATH.MACHINERY_EQUIPMENT_UPDATE} element={<MachineryEquipmentUpdate />} />
      </Route>
    </Route>
  </ErrorBoundaryRoutes>
);

export default TechnicalRoutes;
