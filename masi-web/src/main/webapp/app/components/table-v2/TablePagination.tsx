import React, { ComponentProps } from 'react';
import Flex from '../flex/flex';
import PaginationV2 from '../PaginationV2/PaginationV2';
import SelectV2 from '../SelectV2/SelectV2';
import TableV2 from './Table';
import './Table.scss';

type Props<T extends object> = Omit<
  ComponentProps<typeof TableV2<T>>,
  'isBorder'
> &
  ComponentProps<typeof PaginationV2> &
  ComponentProps<typeof SelectV2>;

const TablePagination = <T extends object>(props: Props<T>) => {
  const {
    total_pages,
    itemsPerPage,
    handlePageClick,
    handlePageSizeChange,
    placeholder,
    options = [
      { value: '10', label: '10 Dòng' },
      { value: '20', label: '20 Dòng' },
      { value: '30', label: '30 Dòng' },
    ],
    ...rest
  } = props;

  return (
    <div className="table-v2__pagination">
      <TableV2<T> isBorder={false} {...rest} />
      <Flex justify="space-between" style={{ margin: '12px 24px' }}>
        <SelectV2
          placeholder={placeholder}
          options={options}
          onChange={handlePageSizeChange && (e => handlePageSizeChange(Number(e.target.value)))}
        />
        <PaginationV2
          total_pages={Math.max(total_pages || 0, 1)}
          itemsPerPage={itemsPerPage}
          handlePageClick={handlePageClick}
        />
      </Flex>
    </div>
  );
};

export default TablePagination;
