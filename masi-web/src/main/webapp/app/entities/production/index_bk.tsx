import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Production = Loadable({
  loader: () =>
    import('app/modules/production-management/production-dashboard-by-order'),
  loading: () => loading,
});

const ProductionCreate = Loadable({
  loader: () => import('app/modules/production-management/production'),
  loading: () => loading,
});

const ProductionManufactureOrderByOrderCreate = Loadable({
  loader: () =>
    import(
      'app/modules/production-management/createManufactureOrderByOrder/manufacture-order-by-order-create'
    ),
  loading: () => loading,
});

// const ProductionCommand = Loadable({
//   loader: () => import('app/modules/production-command/production-command'),
//   loading: () => loading,
// });

const ProductionProcessDetail = Loadable({
  loader: () =>
    import('app/modules/production-process-detail/production-process-detail'),
  loading: () => loading,
});

const ProductionProcessTemplate = Loadable({
  loader: () =>
    import(
      'app/modules/production-process-template/production-process-template'
    ),
  loading: () => loading,
});

const ProductionQuality = Loadable({
  loader: () => import('app/modules/production-quality/production-quality'),
  loading: () => loading,
});

const CancelTestingTemplate = Loadable({
  loader: () =>
    import('app/modules/production-quality/components/cancel-testing-template'),
  loading: () => loading,
});

const ProductionQualityCreate = Loadable({
  loader: () =>
    import(
      'app/modules/production-quality/components/production-quality-create'
    ),
  loading: () => loading,
});

const ProductionQualityUpdate = Loadable({
  loader: () =>
    import(
      'app/modules/production-quality/components/production-quality-create'
    ),
  loading: () => loading,
});

const ProductionStandard = Loadable({
  loader: () => import('app/modules/production-standard/production-standard'),
  loading: () => loading,
});

const ProductionWorkCenters = Loadable({
  loader: () =>
    import('app/modules/production-work-centers/production-work-centers'),
  loading: () => loading,
});

const ProductionRoutings = Loadable({
  loader: () => import('app/modules/production-routings/production-routings'),
  loading: () => loading,
});

const ProductionMaintenance = Loadable({
  loader: () =>
    import('app/modules/production-maintenance/production-maintenance'),
  loading: () => loading,
});

const ProductionPackages = Loadable({
  loader: () => import('app/modules/production-packages/production-packages'),
  loading: () => loading,
});

const ProductionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.PRODUCTION}>
      <Route
        index
        path={PATH.PRODUCTION_STANDARD}
        element={<ProductionStandard />}
      />
      <Route path={PATH.PRODUCTION_PROCESS} element={<Production />} />
      <Route
        path={PATH.PRODUCTION_PROCESS_UPDATE}
        element={<ProductionCreate />}
      />
      <Route
        path={PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER_CREATE}
        element={<ProductionManufactureOrderByOrderCreate />}
      />
      <Route
        path={PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER_UPDATE}
        element={<ProductionManufactureOrderByOrderCreate />}
      />

      {/* <Route path={PATH.PRODUCTION_COMMAND} element={<ProductionCommand />} /> */}
      <Route path={PATH.PRODUCTION_PROCESS_DETAIL}>
        <Route index element={<ProductionProcessDetail />} />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_UPDATE}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_DETAIL}
          element={<ProductionProcessTemplate />}
        />

        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_UPDATE}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_DETAIL}
          element={<ProductionProcessTemplate />}
        />

        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_UPDATE}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_DETAIL}
          element={<ProductionProcessTemplate />}
        />

        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_UPDATE}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_DETAIL}
          element={<ProductionProcessTemplate />}
        />

        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_UPDATE}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_DETAIL}
          element={<ProductionProcessTemplate />}
        />

        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_UPDATE}
          element={<ProductionProcessTemplate />}
        />
        <Route
          path={PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_DETAIL}
          element={<ProductionProcessTemplate />}
        />
      </Route>
      <Route path={PATH.PRODUCTION_QUALITY}>
        <Route index element={<ProductionQuality />} />
        <Route
          path={PATH.PRODUCTION_QUALITY_CREATE}
          element={<ProductionQualityCreate />}
        />
        <Route
          path={PATH.PRODUCTION_QUALITY_UPDATE}
          element={<ProductionQualityUpdate />}
        />
      </Route>

      <Route
        path={PATH.PRODUCTION_WORK_CENTERS}
        element={<ProductionWorkCenters />}
      />
      <Route path={PATH.PRODUCTION_ROUTINGS} element={<ProductionRoutings />} />
      <Route
        path={PATH.PRODUCTION_MAINTENANCE}
        element={<ProductionMaintenance />}
      />
      <Route path={PATH.PRODUCTION_PACKAGES} element={<ProductionPackages />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductionRoutes;
