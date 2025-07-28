import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

interface IAnnualLeaveHeader {
  toggleCreate: () => void;
}

const AnnualLeaveHeader = (props: IAnnualLeaveHeader) => {
  const { toggleCreate } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
          <AuthGuard permissionKey='ANNUAL_LEAVE.EDIT'>
            <Button color="primary" onClick={toggleCreate}>
              Thay đổi thông số
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default AnnualLeaveHeader;
