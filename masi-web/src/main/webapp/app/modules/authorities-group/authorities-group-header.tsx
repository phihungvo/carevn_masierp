import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

interface IAuthoritiesGroupHeader {
  toggleCreate: () => void;
}

const AuthoritiesGroupHeader = (props: IAuthoritiesGroupHeader) => {
  const { toggleCreate } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra" />
      <div className="card-header-extra">
          <AuthGuard permissionKey='PERMISSIONS_GROUPS.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default AuthoritiesGroupHeader;
