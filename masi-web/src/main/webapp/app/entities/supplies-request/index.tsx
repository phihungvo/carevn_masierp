import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const PurchaseProposal = Loadable({
  loader: () => import(/* webpackChunkName: "supplies-request" */ 'app/modules/PurchaseProposal/PurchaseProposal'),
  loading: () => loading,
});

const PurchaseProposalForm = Loadable({
  loader: () => import(/* webpackChunkName: "supplies-request" */ 'app/modules/PurchaseProposal/Form/Form'),
  loading: () => loading,
});

const SuppliesRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.SUPPLIES_REQUESTS}>
      <Route index path={PATH.SUPPLIES_REQUESTS} element={<PurchaseProposal />} />
      <Route index path={PATH.SUPPLIES_REQUESTS_FORM} element={<PurchaseProposalForm />} />
      <Route index path={PATH.SUPPLIES_REQUESTS_FORM_DETAIL} element={<PurchaseProposalForm />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SuppliesRequestRoutes;
