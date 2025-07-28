import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useProductionRoutings from 'app/hooks/use-production-routings';
import useUom from 'app/hooks/use-uom';
import useWarehouse from 'app/hooks/use-warehouse';
import {
  IProductionRouting,
  IProductionRoutingParams,
} from 'app/shared/model/production-routing.model';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';

const { useGetProductionRoutingsQuery } = useProductionRoutings;

interface IProductionRoutingsTableProps {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  searchText: string;
  filter: IProductionRoutingParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionRoutingParams>>;
  setSelectedRecord: (id: string) => void;
}

const ProductionRoutingsTable = (props: IProductionRoutingsTableProps) => {
  const {
    toggleDelete,
    toggleUpdate,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data } = useGetProductionRoutingsQuery(filter);

  const handleUpdate = (id: string) => {
    setSelectedRecord(id);
    toggleUpdate();
  };

  const handleDelete = (id: string) => {
    setSelectedRecord(id);
    toggleDelete();
  };

  const columns = generateColumns(handleUpdate, handleDelete);

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<IProductionRouting>
      table_id="standards"
      columns={columns}
      data={data?.data || []}
      total_pages={data?.totalRecord || 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
};

export default ProductionRoutingsTable;
