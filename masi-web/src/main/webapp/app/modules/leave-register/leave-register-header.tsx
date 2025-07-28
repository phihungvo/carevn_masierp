import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import ButtonIcon from 'app/components/button-icon/button-icon';

interface ILeaveRegisterHeaderProps {
  toggleFilter: () => void;
  toggleCreate: () => void;
  toggleModalDownload: () => void;
}

const LeaveRegisterHeader = (props: ILeaveRegisterHeaderProps) => {
  const { toggleFilter, toggleCreate, toggleModalDownload } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>

          <AuthGuard permissionKey='LEAVE_REGISTER.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>
        <AuthGuard permissionKey='LEAVE_REGISTER.EXPORT'>
          <ButtonIcon
            onClick={toggleModalDownload}
            icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
          />
        </AuthGuard>
      </div>
    </div>
  );
};

export default LeaveRegisterHeader;
