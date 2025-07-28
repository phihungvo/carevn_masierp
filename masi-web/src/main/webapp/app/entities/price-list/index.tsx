import Loadable from 'react-loadable';
import React from 'react';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import { PATH } from 'app/constants/path';

const loading = <div>loading ...</div>;

const PriceList = Loadable({
  loader: () => import(/* webpackChunkName: "price-list" */ 'app/modules/price-list/price-list'),
  loading: () => loading,
});

const PriceListCreateKimLong = Loadable({
  loader: () => import(/* webpackChunkName: "price-list-create-kim-long" */ 'app/modules/price-list/components/price-list-create-kim-long'),
  loading: () => loading,
});

const PriceListUpdateKimLong = Loadable({
  loader: () => import(/* webpackChunkName: "price-list-update-kim-long" */ 'app/modules/price-list/components/price-list-update-kim-long'),
  loading: () => loading,
});

const PriceListCreateMMS = Loadable({
  loader: () => import(/* webpackChunkName: "price-list-create-mms" */ 'app/modules/price-list/components/price-list-create-mms'),
  loading: () => loading,
});

const PriceListUpdateMMS = Loadable({
  loader: () => import(/* webpackChunkName: "price-list-update-mms" */ 'app/modules/price-list/components/price-list-update-mms'),
  loading: () => loading,
});

const PriceListDetailKimLong = Loadable({
  loader: () => import(/* webpackChunkName: "price-list-detail-kim-long" */ 'app/modules/price-list/price-list-detail-kim-long'),
  loading: () => loading,
});

const PriceListDetailMMS = Loadable({
  loader: () => import(/* webpackChunkName: "price-list-detail-mms" */ 'app/modules/price-list/price-list-detail-mms'),
  loading: () => loading,
});

const PriceListCreateKimLongDetail = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "price-list-create-kim-long-detail" */ 'app/modules/price-list/components/price-list-create-kim-long-detail'
    ),
  loading: () => loading,
});

const PriceListUpdateKimLongDetail = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "price-list-update-kim-long-detail" */ 'app/modules/price-list/components/price-list-update-kim-long-detail'
    ),
  loading: () => loading,
});

const PriceListCreateMMSDetail = Loadable({
  loader: () =>
    import(/* webpackChunkName: "price-list-create-MMS-detail" */ 'app/modules/price-list/components/price-list-create-mms-detail'),
  loading: () => loading,
});

const PriceListUpdateMMSDetail = Loadable({
  loader: () =>
    import(/* webpackChunkName: "price-list-create-MMS-detail" */ 'app/modules/price-list/components/price-list-update-mms-detail'),
  loading: () => loading,
});

const PriceListRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.PRICE_LIST}>
      <Route index element={<PriceList />} />
      <Route path={PATH.PRICE_LIST_KIM_LONG} element={<PriceListCreateKimLong />} />
      <Route path={PATH.PRICE_LIST_KIM_LONG_UPDATE} element={<PriceListUpdateKimLong />} />
      <Route path={PATH.PRICE_LIST_MMS} element={<PriceListCreateMMS />} />
      <Route path={PATH.PRICE_LIST_MMS_UPDATE} element={<PriceListUpdateMMS />} />
      <Route path={PATH.PRICE_LIST_DETAIL_KIM_LONG} element={<PriceListDetailKimLong />} />
      <Route path={PATH.PRICE_LIST_DETAIL_MMS} element={<PriceListDetailMMS />} />
      {/* <Route path={PATH.PRICE_LIST_CREATE_KIM_LONG} element={<PriceListCreateKimLongDetail />} /> */}
      {/* <Route path={PATH.PRICE_LIST_UPDATE_KIM_LONG} element={<PriceListUpdateKimLongDetail />} /> */}
      {/* <Route path={PATH.PRICE_LIST_CREATE_MMS} element={<PriceListCreateMMSDetail />} /> */}
      {/* <Route path={PATH.PRICE_LIST_UPDATE_MMS} element={<PriceListUpdateMMSDetail />} /> */}
    </Route>
  </ErrorBoundaryRoutes>
);

export default PriceListRoutes;
