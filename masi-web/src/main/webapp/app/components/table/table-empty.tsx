import React from 'react';
import Flex from '../flex/flex';

const TableEmpty = () => {
  return (
    <Flex justify="center" className="no-data">
      <span>Không có dữ liệu</span>
    </Flex>
  );
};

export default TableEmpty;
