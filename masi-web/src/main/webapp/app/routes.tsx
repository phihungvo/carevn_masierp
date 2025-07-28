import { AUTHORITIES } from 'app/config/constants';
import EntitiesRoutes from 'app/entities/routes';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import Register from 'app/modules/account/register/register';
import Login from 'app/modules/login/login';
import Logout from 'app/modules/login/logout';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import Loadable from 'react-loadable';
import { Navigate, Route } from 'react-router-dom';
import { PATH } from './constants/path';
import AnnualLeaveRoutes from './entities/annual-leave';
import AuthoritiesRoutes from './entities/authorities';
import ContractsRoutes from './entities/contracts';
import CustomerServicesRoutes from './entities/customer-services';
import CustomersRoutes from './entities/customers';
import DocumentaryRoutes from './entities/documentary';
import EmployeesRoutes from './entities/employees';
import FactoriesRoutes from './entities/factories';
import IncomingInvoicesRoutes from './entities/incoming-invoice';
import ItemsRoutes from './entities/items';
import LeaveRegisterRoutes from './entities/leave-register';
import LeaveRequestRoutes from './entities/leave-request';
import LogisticsRoutes from './entities/logistics';
import OfficesRoutes from './entities/offices';
import OrdersRoutes from './entities/orders';
import PriceListRoutes from './entities/price-list';
import ProductionRoutes from './entities/production';
import PurchaseRoutes from './entities/purchase';
import RecruitmentRoutes from './entities/recruitment';
import ReportRoutes from './entities/reports';
import RequestPaymentRoutes from './entities/request-payment';
import SuppliesRoutes from './entities/supplies';
import SuppliesRequestRoutes from './entities/supplies-request';
import TimeSheetRoutes from './entities/time-sheet';
import UniformRoutes from './entities/uniform';
import UomRoutes from './entities/uom';
import WarehouseRoutes from './entities/warehouse';
import DeliverySchedule from './entities/delivery-schedule';
import RegisterEmail from './modules/account/register/register-email';
import AssetRoutes from './entities/asset';
import TechnicalRoutes from './entities/technical';
import CompanyRoutes from './entities/company';
import { useAppSelector } from './config/store';
import { permissions } from './config/permission';
import { useEffect, useState } from 'react';

const loading = <div>loading ...</div>;

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => loading,
});

const Admin = Loadable({
  loader: () =>
    import(
      /* webpackChunkName: "administration" */ 'app/modules/administration'
    ),
  loading: () => loading,
});

const AppRoutes = () => {
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route path="login" element={<Login />} />
        <Route path="logout" element={<Logout />} />

        <Route
          path="*"
          element={
            <PrivateRoute
              hasAnyAuthorities={[
                AUTHORITIES.ADMIN,
                AUTHORITIES.USER,
                AUTHORITIES.ROLE_SUPER_ADMIN,
                AUTHORITIES.ROLE_DEPARTMENT_MANAGER,
                AUTHORITIES.ROLE_DIRECTOR,
                AUTHORITIES.ROLE_STAFF,
                AUTHORITIES.ROLE_WORKER,
              ]}
            >
              <TimeSheetRoutes />
              <LeaveRequestRoutes />
              <ProductionRoutes />
              <CustomersRoutes />
              <PriceListRoutes />
              <RequestPaymentRoutes />
              <ContractsRoutes />
              <OrdersRoutes />
              <PurchaseRoutes />
              <EmployeesRoutes />
              <AnnualLeaveRoutes />
              <OfficesRoutes />
              <LeaveRegisterRoutes />
              <RecruitmentRoutes />
              <DocumentaryRoutes />
              <UniformRoutes />
              <ReportRoutes />
              <UomRoutes />
              <WarehouseRoutes />
              <AuthoritiesRoutes />
              <IncomingInvoicesRoutes />
              <ItemsRoutes />
              <LogisticsRoutes />
              <SuppliesRequestRoutes />
              <SuppliesRoutes />
              <FactoriesRoutes />
              <CustomerServicesRoutes />
              <DeliverySchedule />
              <AssetRoutes />
              <TechnicalRoutes />
              <CompanyRoutes />
            </PrivateRoute>
          }
        />

        <Route path="account">
          <Route
            path="*"
            element={
              <PrivateRoute
                hasAnyAuthorities={[
                  AUTHORITIES.ADMIN, 
                  AUTHORITIES.USER, 
                  AUTHORITIES.ROLE_SUPER_ADMIN,
                  AUTHORITIES.ROLE_DIRECTOR,
                  AUTHORITIES.ROLE_DEPARTMENT_MANAGER,
                  AUTHORITIES.ROLE_WORKER,
                  AUTHORITIES.ROLE_STAFF,
                ]}
              >
                <Account />
              </PrivateRoute>
            }
          />
          <Route path="register/email" element={<RegisterEmail />} />
          <Route path="register" element={<Register />} />
          <Route path="activate" element={<Activate />} />
          <Route path="reset">
            <Route path="request" element={<PasswordResetInit />} />
            <Route path="finish" element={<PasswordResetFinish />} />
          </Route>
        </Route>
        <Route
          path="admin/*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
              <Admin />
            </PrivateRoute>
          }
        />
        <Route
          path="*"
          element={
            <PrivateRoute
              hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}
            >
              <EntitiesRoutes />
            </PrivateRoute>
          }
        />

        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
