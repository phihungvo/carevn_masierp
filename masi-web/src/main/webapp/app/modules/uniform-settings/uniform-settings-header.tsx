import ButtonIcon from 'app/components/button-icon/button-icon';
import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useUniform from 'app/hooks/use-uniform';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

const { useGetUniformsExportLazyQuery } = useUniform
interface IUniformSettingsHeaderProps {
  toggleCreate: () => void;
}

const UniformSettingsHeader = (props: IUniformSettingsHeaderProps) => {
  const { toggleCreate } = props;
  const { trigger, data } = useGetUniformsExportLazyQuery();
  useDownloadXlsx(data?.data, 'Danh sách đồng phục', 'xlsx');
  const handleDownload = () => {
    trigger();
  };
  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
          <AuthGuard permissionKey='UNIFORM_SETTINGS.CREATE'>
            <Button color="primary" onClick={toggleCreate}>
              Tạo mới
            </Button>
          </AuthGuard>

        <AuthGuard permissionKey='UNIFORM_SETTINGS.EXPORT'>
          <ButtonIcon
            onClick={handleDownload}
            icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
          />
        </AuthGuard>
      </div>
    </div>
  );
};

export default UniformSettingsHeader;
