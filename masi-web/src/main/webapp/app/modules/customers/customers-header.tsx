import ButtonIcon from 'app/components/button-icon/button-icon';
import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { PATH } from 'app/constants/path';
import useCustomers from 'app/hooks/use-customers';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { useLocation } from 'react-router';

const { useGetEnabledCustomersExportLazyQuery, useGetDisabledCustomersExportLazyQuery } = useCustomers;

interface ICustomersHeader {
  setSearchText: (value: string) => void;
  toggleFilter: () => void;
  toggleCreate: () => void;
}

const CustomersHeader = (props: ICustomersHeader) => {
  const { setSearchText, toggleCreate, toggleFilter } = props;

  const { pathname } = useLocation();

  const { trigger: exportEnabled, data: dataExportEnabled } = useGetEnabledCustomersExportLazyQuery();
  const { trigger: exportDisabled, data: dataExportDisabled } = useGetDisabledCustomersExportLazyQuery();
  pathname === PATH.CUSTOMERS
    ? useDownloadXlsx(dataExportEnabled?.data, 'customers', 'xlsx')
    : useDownloadXlsx(dataExportDisabled?.data, 'customers-disabled', 'xlsx');

  const handleTriggerExport = () => {
    if (pathname === PATH.CUSTOMERS) {
      exportEnabled();
    }

    if (pathname === PATH.CUSTOMERS_DISPOSED) {
      exportDisabled();
    }
  };

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
        {pathname === PATH.CUSTOMERS && (
            <AuthGuard permissionKey='CUSTOMERS.CREATE'>
              <Button color="primary" onClick={toggleCreate}>
                Tạo mới
              </Button>
            </AuthGuard>
        )}
       <AuthGuard permissionKey='CUSTOMERS.EXPORT'>
          <ButtonIcon
            onClick={handleTriggerExport}
            icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
          />
       </AuthGuard>
      </div>
    </div>
  );
};

export default CustomersHeader;
