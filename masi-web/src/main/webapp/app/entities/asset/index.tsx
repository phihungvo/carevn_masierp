import Loadable from 'react-loadable';

import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Asset = Loadable({
  loader: () => import('app/modules/asset/asset'),
  loading: () => loading,
});


const CreateAsset = Loadable({
  loader: () => import('app/modules/asset/asset-form'),
  loading: () => loading,
});


const Allocation = Loadable({
  loader: () => import('app/modules/allocation/allocation'),
  loading: () => loading,
});

const AllocationForm = Loadable({
  loader: () => import('app/modules/allocation/allocation-form'),
  loading: () => loading,
});

const Liquidation = Loadable({
  loader: () => import('app/modules/liquidation/liquidation'),
  loading: () => loading,
});

const LiquidationCreate = Loadable({
  loader: () => import('app/modules/liquidation/liquidation-form'),
  loading: () => loading,
});

const Depreciation = Loadable({
  loader: () => import(/* webpackChunkName: "contracts-deleted" */ 'app/modules/depreciation/depreciation'),
  loading: () => loading,
});

const DepreciationCreate = Loadable({
  loader: () => import(/* webpackChunkName: "contracts-deleted" */ 'app/modules/depreciation/depreciation-form'),
  loading: () => loading,
});



const TransferAssets = Loadable({
  loader: () => import(/* webpackChunkName: "" */ 'app/modules/transfer-assets'),
  loading: () => loading,
});

const TransferAssetsCreate = Loadable({
  loader: () =>
    import(/* webpackChunkName: "" */ 'app/modules/transfer-assets/transfer-assets-create'),
  loading: () => loading,
});

const TransferAssetsDetail = Loadable({
  loader: () =>
    import(/* webpackChunkName: "" */ 'app/modules/transfer-assets/transfer-assets-detail'),
  loading: () => loading,
});


const AssetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.ASSET}>
      <Route index path={PATH.ASSET} element={<Asset />} />
      <Route index path={PATH.ASSET_CREATE} element={<CreateAsset />} />
      <Route index path={PATH.ASSET_CREATE_DETAIL} element={<CreateAsset />} />
      <Route path={PATH.ALLOCATION} element={<Allocation />} />
      <Route path={PATH.ALLOCATION_CREATE} element={<AllocationForm />} />
      <Route path={PATH.ALLOCATION_CREATE_DETAIL} element={<AllocationForm />} />
      <Route path={PATH.LIQUIDATION} element={<Liquidation />} />
      <Route path={PATH.LIQUIDATION_CREATE} element={<LiquidationCreate />} />
      <Route path={PATH.LIQUIDATION_CREATE_DETAIL} element={<LiquidationCreate />} />
      <Route path={PATH.DEPRECIATION} element={<Depreciation />} />
      <Route path={PATH.DEPRECIATION_CREATE} element={<DepreciationCreate />} />
      <Route path={PATH.DEPRECIATION_CREATE_DETAIL} element={<DepreciationCreate />} />
      <Route path={PATH.TRANSFER_ASSETS}>
        <Route index path={PATH.TRANSFER_ASSETS} element={<TransferAssets />} />
        <Route path={PATH.TRANSFER_ASSETS_CREATE} element={<TransferAssetsCreate />} />
        <Route path={PATH.TRANSFER_ASSETS_DETAIL} element={<TransferAssetsDetail />} />
      </Route>
    </Route>
  </ErrorBoundaryRoutes>
);

export default AssetRoutes;
