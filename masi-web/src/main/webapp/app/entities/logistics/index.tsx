import Loadable from 'react-loadable';

import { PATH } from 'app/constants/path';
import InventoriesStorageExport from 'app/modules/inventories-storage-export';
import InventoriesExportCreate from 'app/modules/inventories-storage-export/inventories-export-create';
import InventoriesExportUpdate from 'app/modules/inventories-storage-export/inventories-update-export';
import SupplierContract from 'app/modules/supplier-contracts';
import SupplierContractsCreate from 'app/modules/supplier-contracts/supplier-contracts-create';
import SupplierContractsUpdate from 'app/modules/supplier-contracts/supplier-contracts-update';
import SuppliersUpdate from 'app/modules/suppliers/suppliers-update';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router';
import Stocktaking from 'app/modules/stocktaking';
import StocktakingCreate from 'app/modules/stocktaking/stocktaking-create';
import StocktakingUpdate from 'app/modules/stocktaking/stocktaking-update';

const loading = <div>loading ...</div>;

const MaterialProposal = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "logistic-material-proposal-list" */ 'app/modules/logistics-material-proposal/logistics-material-proposal'
    ),
  loading: () => loading,
});

const MaterialProposalChangeLogs = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "logistic-material-proposal-change-logs-list" */ 'app/modules/logistics-material-proposal/logistics-material-proposal-change-logs'
    ),
  loading: () => loading,
});

const Suppliers = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "logistic-suppliers" */ 'app/modules/suppliers/suppliers'
    ),
  loading: () => loading,
});

const InventoriesStorage = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "logistic-inventories-storage" */ 'app/modules/inventories-storage'
    ),
  loading: () => loading,
});

const InventoriesCreate = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "logistic-inventories-create" */ 'app/modules/inventories-storage/inventories-create'
    ),
  loading: () => loading,
});

const InventoriesUpdate = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "logistic-inventories-update" */ 'app/modules/inventories-storage/inventories-update'
    ),
  loading: () => loading,
});

const SuppliersCreate = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "logistic-suppliers-create" */ 'app/modules/suppliers/suppliers-create'
    ),
  loading: () => loading,
});

const LogisticsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.LOGISTICS}>
      <Route path={PATH.MATERIAL_PROPOSAL} element={<MaterialProposal />} />
      <Route
        path={PATH.MATERIAL_PROPOSAL_CHANGE_LOGS}
        element={<MaterialProposalChangeLogs />}
      />
      <Route path={PATH.SUPPLIERS} element={<Suppliers />} />
      <Route path={PATH.SUPPLIERS_CREATE} element={<SuppliersCreate />} />
      <Route path={PATH.SUPPLIERS_UPDATE} element={<SuppliersUpdate />} />
      <Route path={PATH.INVENTORIES_STORAGE} element={<InventoriesStorage />} />
      <Route
        path={PATH.INVENTORIES_STORAGE_CREATE}
        element={<InventoriesCreate />}
      />
      <Route
        path={PATH.INVENTORIES_STORAGE_UPDATE}
        element={<InventoriesUpdate />}
      />

      <Route
        path={PATH.INVENTORIES_STORAGE_EXPORT}
        element={<InventoriesStorageExport />}
      />
      <Route
        path={PATH.INVENTORIES_STORAGE_EXPORT_CREATE}
        element={<InventoriesExportCreate />}
      />
      <Route
        path={PATH.INVENTORIES_STORAGE_EXPORT_UPDATE}
        element={<InventoriesExportUpdate />}
      />

      <Route path={PATH.SUPPLIER_CONTRACTS} element={<SupplierContract />} />
      <Route
        path={PATH.SUPPLIER_CONTRACTS_CREATE}
        element={<SupplierContractsCreate />}
      />
      <Route
        path={PATH.SUPPLIER_CONTRACTS_UPDATE}
        element={<SupplierContractsUpdate />}
      />

      <Route path={PATH.STOCKTAKING} element={<Stocktaking />} />
      <Route path={PATH.STOCKTAKING_CREATE} element={<StocktakingCreate />} />
      <Route path={PATH.STOCKTAKING_UPDATE} element={<StocktakingUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LogisticsRoutes;
