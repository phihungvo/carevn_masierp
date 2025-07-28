import { PATH } from 'app/constants/path';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Loadable from 'react-loadable';
import { Route } from 'react-router';

const loading = <div>loading ...</div>;

const Company = Loadable({
  loader: () => import('app/modules/company'),
  loading: () => loading,
});

const CompanyCreate = Loadable({
  loader: () => import('app/modules/company/company-create'),
  loading: () => loading,
});

const CompanyUpdate = Loadable({
  loader: () => import('app/modules/company/company-update'),
  loading: () => loading,
});

const CompanyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path={PATH.COMPANY} element={<Company />} />
    <Route path={PATH.COMPANY_CREATE} element={<CompanyCreate />} />
    <Route path={PATH.COMPANY_UPDATE} element={<CompanyUpdate />} />
  </ErrorBoundaryRoutes>
);

export default CompanyRoutes;
