import Button from 'app/components/button/button';
// import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
// import { PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';
// import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IProductionProcess } from 'app/shared/model/production-process.model';
import React from 'react';

interface IProductionProcessHeader {
  toggleModalCreate: () => void;
  toggleModalDelete: () => void;
  toggleModalFilter: () => void;
  selectedRows: IProductionProcess[];
  setSearchText: (searchText: string) => void;
}

export const ProductionProcessHeader = (props: IProductionProcessHeader) => {
  const { toggleModalCreate, toggleModalDelete, toggleModalFilter, selectedRows, setSearchText } = props;

  // const disabledDelete = !selectedRows.length || selectedRows.some(row => row.status === PRODUCTION_PROCESS_STATUS.COMPLETED);

  return (
    <div className="card-header-container">
      <InputSearch className="card-header-extra" onChange={e => setSearchText(e.target.value)} />

      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleModalFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
      </div>
    </div>
  );
};
