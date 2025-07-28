import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { Action, PermissionResource, Role } from 'app/shared/model/permission.model';

export const checkPermission = (authorities: string[], resource: PermissionResource, action: Action, roles?: Role[]) => {
  if (!authorities || !authorities?.length) return false;

  const isAdmin = hasAnyAuthority(authorities, [Role.ROLE_ADMIN]);

  let isPermitted = authorities.some(authority => authority === `PERMISSION.${resource}.${action}`);

  if (isAdmin) return true;

  if (roles) {
    isPermitted = hasAnyAuthority(authorities, roles) && authorities.some(authority => authority === `PERMISSION.${resource}.${action}`);
  }

  return isPermitted;
};
