import Button from 'app/components/button/button';
import React from 'react';

interface IUomGroupHeader {
  toggleCreate: () => void;
}

const UomGroupHeader = (props: IUomGroupHeader) => {
  const { toggleCreate } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
        <Button color="primary" onClick={toggleCreate}>
          Tạo mới
        </Button>
      </div>
    </div>
  );
};

export default UomGroupHeader;
