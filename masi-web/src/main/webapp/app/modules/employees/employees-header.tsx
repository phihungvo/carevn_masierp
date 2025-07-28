import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { PATH } from 'app/constants/path';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useEmployee from 'app/hooks/use-employee';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { useNavigate } from 'react-router';
import { Button } from 'reactstrap';

const { useGetEmployeeExportXlsxLazyQuery } = useEmployee;

interface IEmployeesHeaderProps {
  setSearchText: (value: string) => void;
  toggleFilter: () => void;
  toggleImport: () => void;
}

const EmployeesHeader = (props: IEmployeesHeaderProps) => {
  const { toggleFilter, setSearchText, toggleImport } = props;

  const navigate = useNavigate();

  const { trigger, data } = useGetEmployeeExportXlsxLazyQuery();

  useDownloadXlsx(data?.data, 'employees', 'xlsx');

  return (
    <div className="card-header-container">
      <InputSearch
        className="card-header-extra"
        onChange={e => {
          setSearchText(e.target.value);
        }}
      />

      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" />
        </Button>
        <AuthGuard permissionKey="EMPLOYEES.CREATE">
          <Button
            color="primary"
            onClick={() => navigate(PATH.EMPLOYEES_CREATE)}
          >
            Tạo mới
          </Button>
        </AuthGuard>
        <AuthGuard permissionKey="EMPLOYEES.EDIT">
          <Button color="primary" onClick={toggleImport}>
            Tải lên NV
          </Button>
        </AuthGuard>
        <AuthGuard permissionKey="EMPLOYEES.EXPORT">
          <Button color="primary" onClick={trigger}>
            Xuất Excel
          </Button>
        </AuthGuard>
      </div>
    </div>
  );
};

export default EmployeesHeader;
