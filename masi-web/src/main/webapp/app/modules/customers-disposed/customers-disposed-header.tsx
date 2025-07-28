import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import React from 'react';

interface ICustomerDisposedHeader {
  setSearchText: (value: string) => void;
  toggleFilter: () => void;
  toggleCreate: () => void;
}

const CustomerDisposedHeader = (props: ICustomerDisposedHeader) => {
  const { setSearchText, toggleCreate, toggleFilter } = props;

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
        <AuthGuard permissionKey='CUSTOMERS_DISPOSED.CREATE'>
          <Button color="primary" onClick={toggleCreate}>
            Tạo mới
          </Button>
        </AuthGuard>
      </div>
    </div>
  );
};

export default CustomerDisposedHeader;
