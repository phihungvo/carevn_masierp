import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useUniform from 'app/hooks/use-uniform';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

const { useExportUniformOrderStockIn } = useUniform;
interface IUniformHeader {
  toggleCreate: () => void;
}

const UniformHeader = (props: IUniformHeader) => {
  const { toggleCreate } = props;

  const { trigger, data } = useExportUniformOrderStockIn();

  const handleDownload = () => {
    trigger();
  };

  useDownloadXlsx(data?.data, 'uniform-receipts', 'xlsx');

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
          <AuthGuard permissionKey='UNIFORM.EXPORT'>
            <Button onClick={handleDownload} color="primary">
              Xuất excel
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default UniformHeader;
