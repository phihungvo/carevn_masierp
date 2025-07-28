import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

interface IUomHeader {
  toggleCreate: () => void;
}

const UomHeader = (props: IUomHeader) => {
  const { toggleCreate } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
          <AuthGuard permissionKey='UOM.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default UomHeader;
