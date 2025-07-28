import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useUniform from 'app/hooks/use-uniform';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

const { useExportUniformStockOut } = useUniform;

interface IUniformExportsHeader {
  setSearchText: (value: string) => void;
  toggleFilter: () => void;
  toggleCreate: () => void;
}

const UniformExportsHeader = (props: IUniformExportsHeader) => {
  const { setSearchText, toggleFilter, toggleCreate } = props;

  const { trigger, data } = useExportUniformStockOut();

  const handleDownload = () => {
    trigger();
  };

  useDownloadXlsx(data?.data, 'uniform-release', 'xlsx');

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
        {/* <Button color="primary" onClick={toggleCreate}>
          Hoàn ứng
        </Button> */}
          <AuthGuard permissionKey='UNIFORM_EXPORTS.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>
          <AuthGuard permissionKey='UNIFORM_EXPORTS.EXPORT'>
            <Button onClick={handleDownload} color="primary">
              Xuất excel
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default UniformExportsHeader;
