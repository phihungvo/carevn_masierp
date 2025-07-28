import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Loadable from 'react-loadable';
import { Route } from 'react-router';
const loading = <div>loading ...</div>;

const IncomingInvoicesV2 = Loadable({
  loader: () => import('app/modules/incoming-invoice-v2/IncomingInvoiceV2'),
  loading: () => loading,
});

const Form = Loadable({
  loader: () => import('app/modules/incoming-invoice-v2/Form/Form'),
  loading: () => loading,
});

const IncomingInvoicesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.INCOMING_INVOICE}>
      <Route index element={<IncomingInvoicesV2 />} />
      <Route path={PATH.INCOMING_INVOICE_FORM} element={<Form />} />
      <Route path={PATH.INCOMING_INVOICE_FORM_DETAIL} element={<Form />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IncomingInvoicesRoutes;
