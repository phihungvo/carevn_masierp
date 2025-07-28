import { Permission } from 'app/config/permission';
import { useAppSelector } from 'app/config/store';
import { checkPermissionAction } from 'app/constants/common';
import React from 'react';
import { Path } from 'react-hook-form';

interface IAuthGuardProps {
  children: React.ReactNode;
  permissionKey?: Path<Permission>;
}

const AuthGuard = (props: IAuthGuardProps) => {
  const { children, permissionKey } = props;

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  return <>{checkPermissionAction(authorities, permissionKey) ? children : null}</>;
};

export default AuthGuard;
