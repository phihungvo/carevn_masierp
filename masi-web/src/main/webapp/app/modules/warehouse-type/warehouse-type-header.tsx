import React from 'react';

interface IWarehouseTypeHeader {
  toggleCreate: () => void;
}

const WarehouseTypeHeader = (props: IWarehouseTypeHeader) => {
  const { toggleCreate } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
        {/* <Button color="primary" onClick={toggleCreate}>
          Tạo mới
        </Button> */}
      </div>
    </div>
  );
};

export default WarehouseTypeHeader;
