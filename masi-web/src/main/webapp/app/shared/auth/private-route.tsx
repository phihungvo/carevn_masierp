import React from 'react';
import { useLocation, Navigate, PathRouteProps } from 'react-router-dom';
import { Translate } from 'react-jhipster';

import { useAppSelector } from 'app/config/store';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import MenuLeft from '../layout/menus-left/menus-left';
import Header from '../layout/header/header';
import { useMenu } from 'app/hooks/use-menu';

interface IOwnProps extends PathRouteProps {
  hasAnyAuthorities?: string[];
  children: React.ReactNode;
}

export const PrivateRoute = ({
  children,
  hasAnyAuthorities = [],
  ...rest
}: IOwnProps) => {
  const items = useMenu();
  const isAuthenticated = useAppSelector(
    state => state.authentication.isAuthenticated,
  );
  const sessionHasBeenFetched = useAppSelector(
    state => state.authentication.sessionHasBeenFetched,
  );
  const account = useAppSelector(state => state.authentication.account);
  const isAuthorized = hasAnyAuthority(account.authorities, hasAnyAuthorities);
  const pageLocation = useLocation();
  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const isAdmin = useAppSelector(state =>
    hasAnyAuthority(state.authentication.account.authorities, [
      AUTHORITIES.ADMIN,
    ]),
  );
  const ribbonEnv = useAppSelector(state => state.applicationProfile.ribbonEnv);
  const isInProduction = useAppSelector(
    state => state.applicationProfile.inProduction,
  );
  const isOpenAPIEnabled = useAppSelector(
    state => state.applicationProfile.isOpenAPIEnabled,
  );

  if (!children) {
    throw new Error(
      `A component needs to be specified for private route for path ${
        (rest as any).path
      }`,
    );
  }

  if (!sessionHasBeenFetched) {
    return <div></div>;
  }

  const paddingTop = '60px';
  if (isAuthenticated) {
    if (isAuthorized) {
      return (
        <div className="app-container container-fluid" style={{ paddingTop }}>
          <div className="left-menu" id="left-menu">
            <MenuLeft items={items} />
          </div>
          <div className="view-container" id="app-view-container">
            <ErrorBoundary>
              <Header
                isAuthenticated={isAuthenticated}
                isAdmin={isAdmin}
                currentLocale={currentLocale}
                ribbonEnv={ribbonEnv}
                isInProduction={isInProduction}
                isOpenAPIEnabled={isOpenAPIEnabled}
              />
            </ErrorBoundary>
            <ErrorBoundary>{children}</ErrorBoundary>
          </div>
        </div>
      );
    }

    return (
      <div className="insufficient-authority">
        <div className="alert alert-danger">
          <Translate contentKey="error.http.403">
            You are not authorized to access this page.
          </Translate>
        </div>
      </div>
    );
  }

  return (
    <Navigate
      to={{
        pathname: '/login',
        search: pageLocation.search,
      }}
      replace
      state={{ from: pageLocation }}
    />
  );
};

export const hasAnyAuthority = (
  authorities: string[],
  hasAnyAuthorities: string[],
) => {
  if (authorities && authorities.length !== 0) {
    if (hasAnyAuthorities.length === 0) {
      return true;
    }
    return hasAnyAuthorities.some(auth => authorities.includes(auth));
  }
  return false;
};

/**
 * Checks authentication before showing the children and redirects to the
 * login page if the user is not authenticated.
 * If hasAnyAuthorities is provided the authorization status is also
 * checked and an error message is shown if the user is not authorized.
 */
export default PrivateRoute;
