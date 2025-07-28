import Loadable from 'react-loadable';
import { Route } from 'react-router';

import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

const loading = <div>loading ...</div>;

const CallCenter = Loadable({
  loader: () => import('app/modules/call-center'),
  loading: () => loading,
});

const CallCenterCreate = Loadable({
  loader: () => import('app/modules/call-center/call-center-create'),
  loading: () => loading,
});

const CallCenterUpdate = Loadable({
  loader: () => import('app/modules/call-center/call-center-update'),
  loading: () => loading,
});

const Complain = Loadable({
  loader: () => import('app/modules/complain'),
  loading: () => loading,
});

const ComplainCreate = Loadable({
  loader: () => import('app/modules/complain/complain-create'),
  loading: () => loading,
});

const ComplainUpdate = Loadable({
  loader: () => import('app/modules/complain/complain-update'),
  loading: () => loading,
});

const CustomerServicesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.CUSTOMER_SERVICES}>
      <Route path={PATH.CALL_CENTER} element={<CallCenter />} />
      <Route path={PATH.CALL_CENTER_CREATE} element={<CallCenterCreate />} />
      <Route path={PATH.CALL_CENTER_UPDATE} element={<CallCenterUpdate />} />
      {/** COMPLAIN */}
      <Route path={PATH.COMPLAIN} element={<Complain />} />
      <Route path={PATH.COMPLAIN_CREATE} element={<ComplainCreate />} />
      <Route path={PATH.COMPLAIN_UPDATE} element={<ComplainUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CustomerServicesRoutes;
