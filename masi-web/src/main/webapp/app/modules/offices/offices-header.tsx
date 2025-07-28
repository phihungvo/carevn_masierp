import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

interface IOfficesHeaderProps {
  setSearchText: (searchText: string) => void;
  toggleCreate: () => void;
}

const OfficesHeader = (props: IOfficesHeaderProps) => {
  const { setSearchText, toggleCreate } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra">
        <InputSearch
          className="card-header-extra"
          onChange={e => {
            setSearchText(e.target.value);
          }}
        />
      </div>
      <div className="card-header-extra">
          <AuthGuard permissionKey='OFFICES.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default OfficesHeader;
