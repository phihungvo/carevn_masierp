import Button from 'app/components/button/button';
import InputSearch from 'app/components/input/input-search';
import React from 'react';

interface ICustomerTransferHeader {
  setSearchText: (value: string) => void;
  toggleFilter: () => void;
  toggleCreate: () => void;
}

const CustomerTransferHeader = (props: ICustomerTransferHeader) => {
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
        <Button color="primary" onClick={toggleCreate}>
          Tạo mới
        </Button>
      </div>
    </div>
  );
};

export default CustomerTransferHeader;
