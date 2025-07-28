import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { Button } from 'reactstrap';

interface IRecruitmentHeaderProps {
  toggleFilter: () => void;
  toggleCreate: () => void;
  setSearchText: (value: string) => void;
}

const RecruitmentHeader = (props: IRecruitmentHeaderProps) => {
  const { toggleFilter, toggleCreate, setSearchText } = props;

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
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>

          <AuthGuard permissionKey='RECRUITMENT.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default RecruitmentHeader;
