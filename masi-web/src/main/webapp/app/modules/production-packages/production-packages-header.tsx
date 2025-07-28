import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IProductionPackage } from 'app/shared/model/production-package.model';
import React from 'react';

interface IProductionPackagesHeader {
  setSearchText: (value: string) => void;
  toggleFilter: () => void;
  toggleCreate: () => void;
  toggleDelete: () => void;
  selectedRows: IProductionPackage[];
}

const ProductionPackagesHeader = (props: IProductionPackagesHeader) => {
  const { setSearchText, toggleFilter, toggleCreate, toggleDelete, selectedRows } = props;

  const disabledDelete = selectedRows?.length === 0;

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
          <Button className='default-buttton' color="primary" onClick={toggleDelete} disabled={disabledDelete}>
            Xoá
          </Button>
          <Button color="primary" onClick={toggleCreate}>
            Tạo mới
          </Button>
      </div>
    </div>
  );
};

export default ProductionPackagesHeader;
