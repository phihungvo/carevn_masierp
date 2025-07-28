import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

interface IWarehouseHeader {
  toggleCreate: () => void;
}

const WarehouseHeader = (props: IWarehouseHeader) => {
  const { toggleCreate } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
          <AuthGuard permissionKey='WAREHOUSES.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default WarehouseHeader;
