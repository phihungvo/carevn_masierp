import Button from 'app/components/button/button';
import InputSearch from 'app/components/input/input-search';
import React from 'react';

interface IContractsHeaderProps {
  setSearchText: (searchText: string) => void;
  toggleFilter: () => void;
  toggleCreate: () => void;
}

const ContractsHeaderDeleted = (props: IContractsHeaderProps) => {
  const { setSearchText, toggleFilter, toggleCreate } = props;

  return (
    <div className="card-header-container">
      <InputSearch className="card-header-extra" onChange={e => setSearchText(e.target.value)} />
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
        {/* <Button color="primary" onClick={toggleCreate}>
          Tạo mới
        </Button> */}
      </div>
    </div>
  );
};

export default ContractsHeaderDeleted;
